﻿//! \file ImageCache.java
//! \brief 图像对象缓存类
// Copyright (c) 2012-2013, https://github.com/rhcad/touchvg

package touchvg.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.v4.util.LruCache;
import android.view.View;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;
import com.larvalabs.svgandroid.SVGParseException;

//! 图像对象缓存类
/*! \ingroup GROUP_ANDROID
 */
public class ImageCache extends Object {
    public static final String BITMAP_PREFIX = "bmp:";
    public static final String SVG_PREFIX = "svg:";
    private static final int CACHE_SIZE = 2 * 1024 * 1024;    // 2MB
    private LruCache<String, Drawable> mCache;
    private String mPath;
    
    public ImageCache() {
        mCache = new LruCache<String, Drawable>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Drawable d) {
                int size = 1; // TODO: SVG size?
                if (d.getClass().isInstance(BitmapDrawable.class)) {
                    size = ((BitmapDrawable)d).getBitmap().getByteCount();
                }
                return size;
            }
        };
    }
    
    //! 设置图像文件的默认路径，自动加载时用
    public void setImagePath(String path) {
        this.mPath = path;
    }
    
    public static int getWidth(Drawable drawable) {
        try {
            return ((BitmapDrawable)drawable).getBitmap().getWidth();
        } catch (ClassCastException e) {}
        
        try {
            return ((PictureDrawable)drawable).getPicture().getWidth();
        } catch (ClassCastException e) {}
        
        return 0;
    }
    
    public static int getHeight(Drawable drawable) {
        try {
            return ((BitmapDrawable)drawable).getBitmap().getHeight();
        } catch (ClassCastException e) {}
        
        try {
            return ((PictureDrawable)drawable).getPicture().getHeight();
        } catch (ClassCastException e) {}
        
        return 0;
    }
    
    //! 清除所有资源
    public void clear() {
        mCache.evictAll();
    }
    
    //! 查找图像对象
    public Drawable getImage(View view, String name) {
        Drawable drawable = mCache.get(name);
        
        if (drawable == null && view != null) {
            if (name.indexOf(BITMAP_PREFIX) == 0) { // R.drawable.resName
                final String resName = name.substring(BITMAP_PREFIX.length());
                int id = view.getResources().getIdentifier(resName,
                        "drawable", view.getContext().getPackageName());
                drawable = this.addBitmap(view.getResources(), id, name);
            }
            else if (name.indexOf(SVG_PREFIX) == 0) { // R.raw.resName
                final String resName = name.substring(SVG_PREFIX.length());
                int id = view.getResources().getIdentifier(resName,
                        "raw", view.getContext().getPackageName());
                drawable = this.addSVG(view.getResources(), id, name);
            }
            else if (name.endsWith(".svg")) {
                if (mPath != null && !mPath.isEmpty()) {
                    drawable = this.addSVGFile(new File(mPath, name).getPath(), name);
                }
            }
            else if (mPath != null && !mPath.isEmpty()) {
                drawable = this.addBitmapFile(view.getResources(),
                        new File(mPath, name).getPath(), name);
            }
        }
        
        return drawable;
    }
    
    //! 插入一个程序资源中的位图图像
    public Drawable addBitmap(Resources res, int id, String name) {
        Drawable drawable = mCache.get(name);

        if (drawable == null && id != 0) {
            final Bitmap bitmap = BitmapFactory.decodeResource(res, id);
            
            if (bitmap != null && bitmap.getWidth() > 0) {
                drawable = new BitmapDrawable(res, bitmap);
                mCache.put(name, drawable);
            }
        }
        
        return drawable;
    }
    
    //! 插入一个程序资源中的SVG图像
    public Drawable addSVG(Resources res, int id, String name) {
        Drawable drawable = mCache.get(name);
        if (drawable == null && id != 0) {
            drawable = addSVG(new SVGBuilder().readFromResource(res, id), name);
        }
        return drawable;
    }
    
    //! 插入一个PNG等图像文件
    public Drawable addBitmapFile(Resources res, String filename, String name) {
        Drawable drawable = mCache.get(name);

        if (drawable == null && new File(filename).exists()) {
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filename, opts);
            opts.inJustDecodeBounds = false;
            
            if (opts.outWidth > 1600 || opts.outHeight > 1600) {
                opts.inSampleSize = 4;
            } else if (opts.outWidth > 600 || opts.outHeight > 600) {
                opts.inSampleSize = 2;
            }
            
            final Bitmap bitmap = BitmapFactory.decodeFile(filename, opts);
            
            if (bitmap != null && bitmap.getWidth() > 0) {
                drawable = new BitmapDrawable(res, bitmap);
                mCache.put(name, drawable);
            }
        }
        
        return drawable;
    }
    
    //! 插入一个SVG文件的图像
    public Drawable addSVGFile(String filename, String name) {
        Drawable drawable = mCache.get(name);

        if (drawable == null && name.endsWith(".svg")) {
            try {
                InputStream data = new FileInputStream(new File(filename));
                drawable = addSVG(new SVGBuilder().readFromInputStream(data), name);
                data.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return drawable;
    }
    
    public class SubPath {
        public Drawable drawable;
        public String id;
        public RectF bounds;
    }
    public List<SubPath> subPictures = new ArrayList<SubPath>();
    
    private Drawable addSVG(SVGBuilder builder, String name) {
        Drawable drawable = null;
        
        try {
            final SVG svg = builder.build(new SVGBuilder.PathCallback() {
                private int n = 0;
                private Paint paint = new Paint();
                private int[] colors = new int[] { Color.BLUE, Color.RED, Color.CYAN, Color.MAGENTA };
                
                @Override
                public void onPathParsed(Path path, String id, Matrix tramsform) {
                    final SubPath subpath = new SubPath();
                    final Picture picture = new Picture();
                    
                    subpath.id = id;
                    subpath.bounds = new RectF();
                    path.computeBounds(subpath.bounds, false);
                    tramsform.mapRect(subpath.bounds);
                    
                    final Canvas c = picture.beginRecording(
                            (int)subpath.bounds.width(), (int)subpath.bounds.height());
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(colors[++n % colors.length]);
                    c.concat(tramsform);
                    c.drawPath(path, paint);
                    picture.endRecording();
                    
                    if (picture.getWidth() > 0) {
                        subpath.drawable = new PictureDrawable(picture);
                        subPictures.add(subpath);
                    }
                }
            });
            
            final Picture picture = svg.getPicture();
            
            if (picture != null && picture.getWidth() > 0) {
                drawable = svg.getDrawable();
                mCache.put(name, drawable);
            }
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        
        return drawable;
    }
    
    //! 插入一个可绘制图像
    public boolean addDrawable(Drawable drawable, String name) {
        boolean ret = (drawable != null && mCache.get(name) == null);
        if (ret) {
            mCache.put(name, drawable);
        }
        return ret;
    }
}
