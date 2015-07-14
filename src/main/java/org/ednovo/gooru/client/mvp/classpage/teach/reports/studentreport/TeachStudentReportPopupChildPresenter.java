/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
/**
 * 
 */
package org.ednovo.gooru.client.mvp.classpage.teach.reports.studentreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.application.client.child.ChildPresenter;
import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.shared.model.classpages.PlanProgressDo;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.UrlNavigationTokens;

/**
 * @author Search Team
 * 
 */
public class TeachStudentReportPopupChildPresenter extends ChildPresenter<TeachStudentReportPopupChildPresenter, IsTeachStudentReportPopupView> implements TeachStudentReportPopupUiHandlers{

	public TeachStudentReportPopupChildPresenter(IsTeachStudentReportPopupView childView) {
		super(childView);
	}

	@Override
	public void getStudentReportData(String gooruUId) {
		String pageType = AppClientFactory.getPlaceManager().getRequestParameter(UrlNavigationTokens.TEACHER_CLASSPAGE_REPORT_TYPE, UrlNavigationTokens.STUDENT_CLASSPAGE_COURSE_VIEW);
		String classUId = AppClientFactory.getPlaceManager().getRequestParameter(UrlNavigationTokens.TEACHER_CLASS_PAGE_ID, null);
		String classGooruOid = AppClientFactory.getPlaceManager().getRequestParameter(UrlNavigationTokens.STUDENT_CLASSPAGE_COURSE_ID, null);
		String unitId = AppClientFactory.getPlaceManager().getRequestParameter(UrlNavigationTokens.STUDENT_CLASSPAGE_UNIT_ID, null);
		String lessonId = AppClientFactory.getPlaceManager().getRequestParameter(UrlNavigationTokens.STUDENT_CLASSPAGE_LESSON_ID, null);
		Map<String,String> queryParams = new HashMap<String, String>();
		queryParams.put("userUid", gooruUId);
		System.out.println("classUId "+classUId);
		System.out.println("classGooruOid "+classGooruOid);
		if(pageType.equalsIgnoreCase(UrlNavigationTokens.STUDENT_CLASSPAGE_COURSE_VIEW)) {
			if(classGooruOid!=null) {
				AppClientFactory.getInjector().getClasspageService().getStudentPlanProgressData(classUId, classGooruOid, null, null, "progress", queryParams, new SimpleAsyncCallback<ArrayList<PlanProgressDo>>() {
					@Override
					public void onSuccess(ArrayList<PlanProgressDo> dataList) {
						getView().setReportData(dataList);
					}
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		} else if(pageType.equalsIgnoreCase(UrlNavigationTokens.STUDENT_CLASSPAGE_UNIT_VIEW)) {
			if(classGooruOid!=null&&unitId!=null) {
				AppClientFactory.getInjector().getClasspageService().getStudentPlanProgressData(classUId, classGooruOid, unitId, null, "progress", queryParams, new SimpleAsyncCallback<ArrayList<PlanProgressDo>>() {
					@Override
					public void onSuccess(ArrayList<PlanProgressDo> dataList) {
						getView().setReportData(dataList);
					}
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		}
	}
}