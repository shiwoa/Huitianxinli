// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LookParserActivity$$ViewInjector<T extends com.yizhilu.exam.LookParserActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362464, "field 'sideMenu'");
    target.sideMenu = finder.castView(view, 2131362464, "field 'sideMenu'");
    view = finder.findRequiredView(source, 2131362463, "field 'leftLayout'");
    target.leftLayout = finder.castView(view, 2131362463, "field 'leftLayout'");
    view = finder.findRequiredView(source, 2131361886, "field 'title'");
    target.title = finder.castView(view, 2131361886, "field 'title'");
    view = finder.findRequiredView(source, 2131361896, "field 'gridView'");
    target.gridView = finder.castView(view, 2131361896, "field 'gridView'");
    view = finder.findRequiredView(source, 2131362437, "field 'examType'");
    target.examType = finder.castView(view, 2131362437, "field 'examType'");
    view = finder.findRequiredView(source, 2131362441, "field 'currentNumber'");
    target.currentNumber = finder.castView(view, 2131362441, "field 'currentNumber'");
    view = finder.findRequiredView(source, 2131362442, "field 'allNumber'");
    target.allNumber = finder.castView(view, 2131362442, "field 'allNumber'");
    view = finder.findRequiredView(source, 2131361874, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131361874, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131362018, "field 'errorViewPager'");
    target.errorViewPager = finder.castView(view, 2131362018, "field 'errorViewPager'");
    view = finder.findRequiredView(source, 2131362438, "field 'typeNumber'");
    target.typeNumber = finder.castView(view, 2131362438, "field 'typeNumber'");
    view = finder.findRequiredView(source, 2131362439, "field 'typeAllnumber'");
    target.typeAllnumber = finder.castView(view, 2131362439, "field 'typeAllnumber'");
    view = finder.findRequiredView(source, 2131362440, "field 'bracketText'");
    target.bracketText = finder.castView(view, 2131362440, "field 'bracketText'");
  }

  @Override public void reset(T target) {
    target.sideMenu = null;
    target.leftLayout = null;
    target.title = null;
    target.gridView = null;
    target.examType = null;
    target.currentNumber = null;
    target.allNumber = null;
    target.viewPager = null;
    target.errorViewPager = null;
    target.typeNumber = null;
    target.typeAllnumber = null;
    target.bracketText = null;
  }
}
