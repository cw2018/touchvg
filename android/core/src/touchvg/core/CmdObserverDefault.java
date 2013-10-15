/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package touchvg.core;

public class CmdObserverDefault extends CmdObserver {
  private long swigCPtr;

  protected CmdObserverDefault(long cPtr, boolean cMemoryOwn) {
    super(touchvgJNI.CmdObserverDefault_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CmdObserverDefault obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        touchvgJNI.delete_CmdObserverDefault(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    touchvgJNI.CmdObserverDefault_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    touchvgJNI.CmdObserverDefault_change_ownership(this, swigCPtr, true);
  }

  public CmdObserverDefault() {
    this(touchvgJNI.new_CmdObserverDefault(), true);
    touchvgJNI.CmdObserverDefault_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public void onDocLoaded(MgMotion sender) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onDocLoaded(swigCPtr, this, MgMotion.getCPtr(sender), sender); else touchvgJNI.CmdObserverDefault_onDocLoadedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender);
  }

  public void onEnterSelectCommand(MgMotion sender) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onEnterSelectCommand(swigCPtr, this, MgMotion.getCPtr(sender), sender); else touchvgJNI.CmdObserverDefault_onEnterSelectCommandSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender);
  }

  public void onUnloadCommands(MgCmdManager sender) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onUnloadCommands(swigCPtr, this, MgCmdManager.getCPtr(sender), sender); else touchvgJNI.CmdObserverDefault_onUnloadCommandsSwigExplicitCmdObserverDefault(swigCPtr, this, MgCmdManager.getCPtr(sender), sender);
  }

  public boolean selectActionsNeedHided(MgMotion sender) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_selectActionsNeedHided(swigCPtr, this, MgMotion.getCPtr(sender), sender) : touchvgJNI.CmdObserverDefault_selectActionsNeedHidedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender);
  }

  public boolean doAction(MgMotion sender, int action) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_doAction(swigCPtr, this, MgMotion.getCPtr(sender), sender, action) : touchvgJNI.CmdObserverDefault_doActionSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, action);
  }

  public boolean doEndAction(MgMotion sender, int action) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_doEndAction(swigCPtr, this, MgMotion.getCPtr(sender), sender, action) : touchvgJNI.CmdObserverDefault_doEndActionSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, action);
  }

  public void drawInShapeCommand(MgMotion sender, MgCommand cmd, GiGraphics gs) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_drawInShapeCommand(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgCommand.getCPtr(cmd), cmd, GiGraphics.getCPtr(gs), gs); else touchvgJNI.CmdObserverDefault_drawInShapeCommandSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgCommand.getCPtr(cmd), cmd, GiGraphics.getCPtr(gs), gs);
  }

  public void drawInSelectCommand(MgMotion sender, MgShape sp, int handleIndex, GiGraphics gs) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_drawInSelectCommand(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp, handleIndex, GiGraphics.getCPtr(gs), gs); else touchvgJNI.CmdObserverDefault_drawInSelectCommandSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp, handleIndex, GiGraphics.getCPtr(gs), gs);
  }

  public boolean onShapeWillAdded(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeWillAdded(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeWillAddedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public void onShapeAdded(MgMotion sender, MgShape sp) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onShapeAdded(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp); else touchvgJNI.CmdObserverDefault_onShapeAddedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public boolean onShapeWillDeleted(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeWillDeleted(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeWillDeletedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public void onShapeDeleted(MgMotion sender, MgShape sp) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onShapeDeleted(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp); else touchvgJNI.CmdObserverDefault_onShapeDeletedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public boolean onShapeCanRotated(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeCanRotated(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeCanRotatedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public boolean onShapeCanTransform(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeCanTransform(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeCanTransformSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public boolean onShapeCanUnlock(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeCanUnlock(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeCanUnlockSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public boolean onShapeCanUngroup(MgMotion sender, MgShape sp) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_onShapeCanUngroup(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp) : touchvgJNI.CmdObserverDefault_onShapeCanUngroupSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp);
  }

  public void onShapeMoved(MgMotion sender, MgShape sp, int segment) {
    if (getClass() == CmdObserverDefault.class) touchvgJNI.CmdObserverDefault_onShapeMoved(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp, segment); else touchvgJNI.CmdObserverDefault_onShapeMovedSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, MgShape.getCPtr(sp), sp, segment);
  }

  public MgBaseShape createShape(MgMotion sender, int type) {
    long cPtr = (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_createShape(swigCPtr, this, MgMotion.getCPtr(sender), sender, type) : touchvgJNI.CmdObserverDefault_createShapeSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, type);
    return (cPtr == 0) ? null : new MgBaseShape(cPtr, false);
  }

  public MgCommand createCommand(MgMotion sender, String name) {
    long cPtr = (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_createCommand(swigCPtr, this, MgMotion.getCPtr(sender), sender, name) : touchvgJNI.CmdObserverDefault_createCommandSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(sender), sender, name);
    return (cPtr == 0) ? null : new MgCommand(cPtr, false);
  }

  public int addShapeActions(MgMotion arg0, Ints arg1, int n, MgShape arg3) {
    return (getClass() == CmdObserverDefault.class) ? touchvgJNI.CmdObserverDefault_addShapeActions(swigCPtr, this, MgMotion.getCPtr(arg0), arg0, Ints.getCPtr(arg1), arg1, n, MgShape.getCPtr(arg3), arg3) : touchvgJNI.CmdObserverDefault_addShapeActionsSwigExplicitCmdObserverDefault(swigCPtr, this, MgMotion.getCPtr(arg0), arg0, Ints.getCPtr(arg1), arg1, n, MgShape.getCPtr(arg3), arg3);
  }

}