// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class StageCommentActivity$$ViewInjector<T extends com.yizhilu.exam.StageCommentActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362463, "field 'leftLayout'");
    target.leftLayout = finder.castView(view, 2131362463, "field 'leftLayout'");
    view = finder.findRequiredView(source, 2131361886, "field 'title'");
    target.title = finder.castView(view, 2131361886, "field 'title'");
    view = finder.findRequiredView(source, 2131362164, "field 'commentListView'");
    target.commentListView = finder.castView(view, 2131362164, "field 'commentListView'");
    view = finder.findRequiredView(source, 2131362464, "field 'sideMenu'");
    target.sideMenu = finder.castView(view, 2131362464, "field 'sideMenu'");
    view = finder.findRequiredView(source, 2131362103, "field 'nullLayout'");
    target.nullLayout = finder.castView(view, 2131362103, "field 'nullLayout'");
  }

  @Override public void reset(T target) {
    target.leftLayout = null;
    target.title = null;
    target.commentListView = null;
    target.sideMenu = null;
    target.nullLayout = null;
  }
}
