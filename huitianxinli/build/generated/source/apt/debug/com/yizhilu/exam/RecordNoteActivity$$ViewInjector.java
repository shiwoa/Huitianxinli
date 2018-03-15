// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class RecordNoteActivity$$ViewInjector<T extends com.yizhilu.exam.RecordNoteActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362464, "field 'sideMenu'");
    target.sideMenu = finder.castView(view, 2131362464, "field 'sideMenu'");
    view = finder.findRequiredView(source, 2131362463, "field 'leftLayout'");
    target.leftLayout = finder.castView(view, 2131362463, "field 'leftLayout'");
    view = finder.findRequiredView(source, 2131361886, "field 'title'");
    target.title = finder.castView(view, 2131361886, "field 'title'");
    view = finder.findRequiredView(source, 2131362468, "field 'rightLayoutTv'");
    target.rightLayoutTv = finder.castView(view, 2131362468, "field 'rightLayoutTv'");
    view = finder.findRequiredView(source, 2131362466, "field 'rightLayout'");
    target.rightLayout = finder.castView(view, 2131362466, "field 'rightLayout'");
    view = finder.findRequiredView(source, 2131362116, "field 'nodeContent'");
    target.nodeContent = finder.castView(view, 2131362116, "field 'nodeContent'");
  }

  @Override public void reset(T target) {
    target.sideMenu = null;
    target.leftLayout = null;
    target.title = null;
    target.rightLayoutTv = null;
    target.rightLayout = null;
    target.nodeContent = null;
  }
}
