//! \file recordshapes.h
//! \brief 定义矢量录屏类 MgRecordShapes
// Copyright (c) 2013, Zhang Yungui
// License: LGPL, https://github.com/rhcad/touchvg

#ifndef TOUCHVG_RECORD_SHAPES_H_
#define TOUCHVG_RECORD_SHAPES_H_

struct MgCoreView;

//! 矢量录屏类
/*! \ingroup CORE_STORAGE
 */
class MgRecordShapes
{
public:
    enum { DYNSHAPEID = 1000000,
        RECORD_ADD = 1, RECORD_EDIT = 2, RECORD_DEL = 4,
    };
    MgRecordShapes(const char* path, MgCoreView* coreView, bool forUndo);
    ~MgRecordShapes();
    
    bool recordStep();              //!< 录制一步操作，在线程中循环调用
    void stopRecord();              //!< 停止录制
    int getFileCount() const;       //!< 得到已录制的文件数
    
    bool canUndo() const;           //!< 是否能撤销
    bool canRedo() const;           //!< 是否能重做
    bool undo();                    //!< 撤销一步
    bool redo();                    //!< 重做一步
    
    bool play(int index);           //!< 播放一帧图形
    
private:
    struct Impl;
    Impl* _im;
};

#endif // TOUCHVG_RECORD_SHAPES_H_
