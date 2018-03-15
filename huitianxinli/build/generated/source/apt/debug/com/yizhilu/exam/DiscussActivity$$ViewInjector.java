// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class DiscussActivity$$ViewInjector<T extends com.yizhilu.exam.DiscussActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361947, "field 'diacussNum'");
    target.diacussNum = finder.castView(view, 2131361947, "field 'diacussNum'");
    view = finder.findRequiredView(source, 2131361835, "field 'radioGroup'");
    target.radioGroup = finder.castView(view, 2131361835, "field 'radioGroup'");
    view = finder.findRequiredView(source, 2131361842, "field 'cancel'");
    target.cancel = finder.castView(view, 2131361842, "field 'cancel'");
    view = finder.findRequiredView(source, 2131361843, "field 'startAnswer'");
    target.startAnswer = finder.castView(view, 2131361843, "field 'startAnswer'");
    view = finder.findRequiredView(source, 2131361948, "field 'oneButton'");
    target.oneButton = finder.castView(view, 2131361948, "field 'oneButton'");
    view = finder.findRequiredView(source, 2131361949, "field 'twoButton'");
    target.twoButton = finder.castView(view, 2131361949, "field 'twoButton'");
  }

  @Override public void reset(T target) {
    target.diacussNum = null;
    target.radioGroup = null;
    target.cancel = null;
    target.startAnswer = null;
    target.oneButton = null;
    target.twoButton = null;
  }
}
