// Generated code from Butter Knife. Do not modify!
package com.yizhilu.exam;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PracticeReportActivity$$ViewInjector<T extends com.yizhilu.exam.PracticeReportActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362464, "field 'sideMenu'");
    target.sideMenu = finder.castView(view, 2131362464, "field 'sideMenu'");
    view = finder.findRequiredView(source, 2131362463, "field 'leftLayout'");
    target.leftLayout = finder.castView(view, 2131362463, "field 'leftLayout'");
    view = finder.findRequiredView(source, 2131361886, "field 'title'");
    target.title = finder.castView(view, 2131361886, "field 'title'");
    view = finder.findRequiredView(source, 2131362105, "field 'reportType'");
    target.reportType = finder.castView(view, 2131362105, "field 'reportType'");
    view = finder.findRequiredView(source, 2131362107, "field 'submitTime'");
    target.submitTime = finder.castView(view, 2131362107, "field 'submitTime'");
    view = finder.findRequiredView(source, 2131361812, "field 'rightNumber'");
    target.rightNumber = finder.castView(view, 2131361812, "field 'rightNumber'");
    view = finder.findRequiredView(source, 2131362110, "field 'zongNumber'");
    target.zongNumber = finder.castView(view, 2131362110, "field 'zongNumber'");
    view = finder.findRequiredView(source, 2131362111, "field 'answerTime'");
    target.answerTime = finder.castView(view, 2131362111, "field 'answerTime'");
    view = finder.findRequiredView(source, 2131362112, "field 'averageTime'");
    target.averageTime = finder.castView(view, 2131362112, "field 'averageTime'");
    view = finder.findRequiredView(source, 2131362113, "field 'correctRate'");
    target.correctRate = finder.castView(view, 2131362113, "field 'correctRate'");
    view = finder.findRequiredView(source, 2131362114, "field 'reportListView'");
    target.reportListView = finder.castView(view, 2131362114, "field 'reportListView'");
    view = finder.findRequiredView(source, 2131362115, "field 'lookParser'");
    target.lookParser = finder.castView(view, 2131362115, "field 'lookParser'");
  }

  @Override public void reset(T target) {
    target.sideMenu = null;
    target.leftLayout = null;
    target.title = null;
    target.reportType = null;
    target.submitTime = null;
    target.rightNumber = null;
    target.zongNumber = null;
    target.answerTime = null;
    target.averageTime = null;
    target.correctRate = null;
    target.reportListView = null;
    target.lookParser = null;
  }
}
