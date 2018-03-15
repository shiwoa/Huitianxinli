// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class KnowledgePointActivity$$ViewInjector<T extends com.yizhilu.exam.KnowledgePointActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362002, "field 'pointListView'");
    target.pointListView = finder.castView(view, 2131362002, "field 'pointListView'");
    view = finder.findRequiredView(source, 2131362003, "field 'fifteenText'");
    target.fifteenText = finder.castView(view, 2131362003, "field 'fifteenText'");
    view = finder.findRequiredView(source, 2131362004, "field 'orderText'");
    target.orderText = finder.castView(view, 2131362004, "field 'orderText'");
    view = finder.findRequiredView(source, 2131362001, "field 'backImage'");
    target.backImage = finder.castView(view, 2131362001, "field 'backImage'");
  }

  @Override public void reset(T target) {
    target.pointListView = null;
    target.fifteenText = null;
    target.orderText = null;
    target.backImage = null;
  }
}
