// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AssemblyExamActivity$$ViewInjector<T extends com.yizhilu.exam.AssemblyExamActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361835, "field 'radioGroup'");
    target.radioGroup = finder.castView(view, 2131361835, "field 'radioGroup'");
    view = finder.findRequiredView(source, 2131361842, "field 'cancel'");
    target.cancel = finder.castView(view, 2131361842, "field 'cancel'");
    view = finder.findRequiredView(source, 2131361843, "field 'startAnswer'");
    target.startAnswer = finder.castView(view, 2131361843, "field 'startAnswer'");
    view = finder.findRequiredView(source, 2131361839, "field 'cb1'");
    target.cb1 = finder.castView(view, 2131361839, "field 'cb1'");
    view = finder.findRequiredView(source, 2131361840, "field 'cb2'");
    target.cb2 = finder.castView(view, 2131361840, "field 'cb2'");
    view = finder.findRequiredView(source, 2131361841, "field 'cb3'");
    target.cb3 = finder.castView(view, 2131361841, "field 'cb3'");
  }

  @Override public void reset(T target) {
    target.radioGroup = null;
    target.cancel = null;
    target.startAnswer = null;
    target.cb1 = null;
    target.cb2 = null;
    target.cb3 = null;
  }
}
