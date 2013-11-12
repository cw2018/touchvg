// recordshapes.cpp
// Copyright (c) 2013, Zhang Yungui
// License: LGPL, https://github.com/rhcad/touchvg

#include "recordshapes.h"
#include "mgcoreview.h"
#include "mgshapes.h"
#include "mgshapeslock.h"
#include "mgjsonstorage.h"
#include "mgstorage.h"
#include "mglog.h"
#include "mgview.h"
#include "mgcmd.h"
#include <sstream>
#include <cstdio>
#include <vector>
#include <map>
#include <sys/time.h>

struct MgRecordShapes::Impl
{
    std::string     path;
    MgCoreView      *coreView;
    std::map<int, int>  id2ver;
    bool            forUndo;
    volatile int    lastChangeCount;
    volatile int    lastDrawCount;
    volatile int    fileCount;
    volatile int    maxCount;
    volatile long   loading;
    FILE            *fp;
    MgJsonStorage   *storage;
    MgStorage       *s;
    struct timeval  startTime;
    long            tick;
    
    Impl() : lastChangeCount(-1), lastDrawCount(0), fileCount(0), maxCount(0), loading(0)
        , fp(NULL), storage(NULL), s(NULL) {}
    bool recordShapes(const MgShapes* shapes);
    bool recordDynamicShapes();
    bool beginJsonFile();
    bool saveJsonFile(bool save);
    bool loadUndoFile(int index);
    void loadPlayShapes(MgShapes* shapes, MgStorage* s, MgShape*& newsp);
};

MgRecordShapes::MgRecordShapes(const char* path, MgCoreView* coreView, bool forUndo)
{
    _im = new Impl();
    _im->coreView = coreView;
    _im->forUndo = forUndo;
    _im->path = path;
    if (*_im->path.rbegin() != '/' && *_im->path.rbegin() != '\\') {
        _im->path += '/';
    }
}

MgRecordShapes::~MgRecordShapes()
{
    delete _im;
}

int MgRecordShapes::getFileCount() const
{
    return _im->fileCount;
}

bool MgRecordShapes::canUndo() const
{
    return _im->fileCount > 1;
}

bool MgRecordShapes::canRedo() const
{
    return _im->fileCount < _im->maxCount;
}

bool MgRecordShapes::undo()
{
    MgShapesLock locker(MgShapesLock::Load, _im->coreView->viewAdapter());
    bool ret = false;
    
    giInterlockedIncrement(&_im->loading);
    if (locker.locked() && canUndo()) {
        ret = _im->loadUndoFile(_im->fileCount - 2);    // 加载倒数第二个文件
        if (ret) {
            _im->fileCount--;
        }
        locker.unlock();                                // 立即更新ChangeCount
        _im->lastChangeCount = _im->coreView->getChangeCount();
    }
    giInterlockedDecrement(&_im->loading);
    
    return ret;
}

bool MgRecordShapes::redo()
{
    MgShapesLock locker(MgShapesLock::Load, _im->coreView->viewAdapter());
    bool ret = false;
    
    giInterlockedIncrement(&_im->loading);
    if (locker.locked() && canRedo()) {
        ret = _im->loadUndoFile(_im->fileCount);
        if (ret) {
            _im->fileCount++;
        }
        locker.unlock();
        _im->lastChangeCount = _im->coreView->getChangeCount();
    }
    giInterlockedDecrement(&_im->loading);
    
    return ret;
}

bool MgRecordShapes::recordStep()
{
    bool save = false;
    
    if (_im->loading) {
        return false;
    }
    if (_im->lastChangeCount != _im->coreView->getChangeCount()) {
        MgShapesLock locker(MgShapesLock::ReadOnly, _im->coreView->viewAdapter());
        if (locker.locked()
            && _im->lastChangeCount != _im->coreView->getChangeCount()  // 再比较一次，避免Lock等待中改变
            && _im->beginJsonFile())
        {
            _im->lastChangeCount = _im->coreView->getChangeCount();
            save = _im->recordShapes(MgShapes::fromHandle(_im->coreView->shapesHandle()));
        }
    }
    if (!_im->forUndo && _im->lastDrawCount != _im->coreView->getDrawCount()) {
        _im->lastDrawCount = _im->coreView->getDrawCount();
        MgDynShapeLock locker(false, _im->coreView->viewAdapter());
        if (locker.locked() && _im->beginJsonFile()) {
            save = _im->recordDynamicShapes() || save;
        }
    }
    
    return !_im->loading && _im->storage && _im->saveJsonFile(save);
}

void MgRecordShapes::stopRecord()
{
    if (!_im->forUndo) {
    }
}

bool MgRecordShapes::Impl::recordShapes(const MgShapes* shapes)
{
    MgShapeIterator it(shapes);
    
    if (fileCount == 0) {                                               // 第一次全部记录ID和版本
        id2ver.clear();
        while (MgShape* sp = it.getNext()) {
            id2ver[sp->getID()] = sp->shapec()->getChangeCount();
        }
        return true;
    }
    
    std::map<int, int> tmpids(id2ver);
    std::map<int, int>::iterator i;
    int index = 0;
    int updateFlags = 0;
    int sid = 0;
    
    s->writeNode("shapes", shapes->getIndex(), false);
    while (MgShape* sp = it.getNext()) {
        sid = sp->getID();
        i = id2ver.find(sid);                                           // 查找是否之前已存在
        if (i == id2ver.end()) {                                        // 是新增的图形
            id2ver[sid] = sp->shapec()->getChangeCount();               // 增加记录版本
            shapes->saveShape(s, sp, index++);
            updateFlags |= updateFlags ? RECORD_EDIT : RECORD_ADD;
        } else {
            tmpids.erase(tmpids.find(sid));                             // 标记是已有图形
            if (i->second != sp->shapec()->getChangeCount()) {          // 改变的图形
                i->second = sp->shapec()->getChangeCount();
                id2ver[sid] = sp->shapec()->getChangeCount();           // 更新版本
                shapes->saveShape(s, sp, index++);
                updateFlags |= RECORD_EDIT;
            }
        }
    }
    s->writeNode("shapes", shapes->getIndex(), true);
    
    if (!tmpids.empty()) {                                              // 之前存在，现在已删除
        updateFlags |= RECORD_DEL;
        s->writeNode("delete", -1, false);
        index = 0;
        for (i = tmpids.begin(); i != tmpids.end(); ++i) {
            id2ver.erase(id2ver.find(i->first));
            
            std::stringstream ss;
            ss << "d" << index++;
            s->writeInt(ss.str().c_str(), i->first);                    // 记下删除的图形的ID
        }
        s->writeNode("delete", -1, true);
    }
    s->writeInt("updateFlags", updateFlags);
    if (updateFlags == RECORD_ADD)
        s->writeInt("addID", sid);
    
    return updateFlags != 0;
}

bool MgRecordShapes::Impl::recordDynamicShapes()
{
    MgCommand* cmd = coreView->viewAdapter()->getCommand();
    int n = 0;
    
    if (cmd) {
        MgShapes* shapes = MgShapes::create();
        
        shapes->setNewShapeID(DYNSHAPEID);
        n = cmd->gatherShapes(coreView->viewAdapter()->motion(), shapes);
        if (n > 0) {
            s->writeNode("dynamic", -1, false);
            shapes->save(s);
            s->writeNode("dynamic", -1, true);
        }
        shapes->release();
    }
    
    return n > 0;
}

static long diffms(struct timeval& from , struct timeval& to)
{
    return (to.tv_sec - from.tv_sec) * 1000 + (to.tv_usec - from.tv_usec) / 1000;
}

bool MgRecordShapes::Impl::beginJsonFile()
{
    if (!storage) {
        storage = new MgJsonStorage();
        s = storage->storageForWrite();
        s->writeNode("record", -1, false);
        
        struct timeval nowTime;
        gettimeofday(&nowTime, NULL);
        if (maxCount == 0)
            startTime = nowTime;
        tick = diffms(startTime, nowTime);
        s->writeInt("tick", tick);
    }
    return true;
}

bool MgRecordShapes::Impl::saveJsonFile(bool save)
{
    bool ret = false;
    std::stringstream ss;
    
    if (maxCount == 0 || forUndo) {
        ss << path << fileCount << ".vg";
        maxCount = ++fileCount;
        ret = coreView->saveToFile(ss.str().c_str(), false);
    }
    else if (save) {
        ss << path << fileCount << ".vgr";
        fp = mgopenfile(ss.str().c_str(), "wt");
        ret = s->writeNode("record", -1, true) && storage->save(fp, false);
        if (ret) {
            maxCount = ++fileCount;
        } else {
            LOGE("Fail to record shapes: %s", ss.str().c_str());
        }
        if (fp) {
            fclose(fp);
            fp = NULL;
        }
    }
    
    delete storage;
    storage = NULL;
    s = NULL;
    
    return ret;
}

bool MgRecordShapes::Impl::loadUndoFile(int index)
{
    std::stringstream ss;
    ss << path << index << ".vg";
    
    return coreView->loadFromFile(ss.str().c_str(), false, false);
}

void MgRecordShapes::Impl::loadPlayShapes(MgShapes* shapes, MgStorage* s, MgShape*& newsp)
{
    if (s->readInt("updateFlags", 0) & (RECORD_ADD | RECORD_EDIT)) {
        shapes->load(coreView->viewAdapter()->getShapeFactory(), s, true);
        //newsp = shapes->findShape(s->readInt("addID", 0));  // shapeAdded增量显示方式容易冲突
    }
    if (s->readNode("delete", -1, false)) {
        for (int i = 0, index = 0; ; i++) {
            std::stringstream ss;
            ss << "d" << index++;
            int sid = s->readInt(ss.str().c_str(), 0);
            if (sid == 0)
                break;
            MgShape* sp = shapes->removeShape(sid, false);
            if (sp) {
                sp->release();
            }
        }
        s->readNode("delete", -1, true);
    }
}

bool MgRecordShapes::play(int index)
{
    std::stringstream ss;
    
    if (index == 0) {
        ss << _im->path << index << ".vg";
        return _im->coreView->loadFromFile(ss.str().c_str(), true);
    }
    
    ss << _im->path << index << ".vgr";
    FILE* fp = mgopenfile(ss.str().c_str(), "rt");
    MgJsonStorage* storage = new MgJsonStorage();
    MgStorage* s = fp ? storage->storageForRead(fp) : NULL;
    bool ret = false;
    
    if (fp) {
        fclose(fp);
        fp = NULL;
    }
    
    if (s && s->readNode("record", -1, false)) {
        int updateFlags = s->readInt("updateFlags", 0);
        MgShapesLock locker(MgShapesLock::Load, updateFlags ? _im->coreView->viewAdapter() : NULL, 2000);
        
        if (locker.locked()) {
            MgShapes* shapes = MgShapes::fromHandle(_im->coreView->shapesHandle());
            MgShape* newsp = NULL;
            _im->loadPlayShapes(shapes, s, newsp);
            
            if (newsp) {
                _im->coreView->viewAdapter()->shapeAdded(newsp);
            } else {
                _im->coreView->viewAdapter()->regenAll();
            }
            ret = true;
            giSleep(50);
        }
        
        if (s->readNode("dynamic", -1, false)) {
            ret = _im->coreView->loadDynamicShapes(s) || ret;
            s->readNode("dynamic", -1, true);
        }
        
        s->readNode("record", -1, true);
    }
    
    delete storage;
    storage = NULL;
    s = NULL;
    
    return ret;
}
