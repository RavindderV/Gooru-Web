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
package org.ednovo.gooru.client.mvp.classpages.studentView;

import java.util.ArrayList;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.classpages.studentView.StudentAssignmentPresenter.IsStudentAssignmentProxy;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.play.collection.GwtUUIDGenerator;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.ErrorPopup;
import org.ednovo.gooru.client.service.ClasspageServiceAsync;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.model.content.AssignmentsListDo;
import org.ednovo.gooru.shared.model.content.ClasspageDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;


public class StudentAssignmentPresenter extends BasePlacePresenter<IsStudentAssignmentView, IsStudentAssignmentProxy> implements StudentAssignmentUiHandlers {

	
	@Inject
	private ClasspageServiceAsync classpageServiceAsync;
	
	private SimpleAsyncCallback<CollectionDo> collectionAsyncCallback;
	
	private SimpleAsyncCallback<AssignmentsListDo> assignmentsListAsyncCallback;	
	
	SignUpPresenter signUpViewPresenter = null;
	private Integer offset=0;
	private Integer limit=5;
	private Integer defaultOffsetForPath=0;
	private Integer defaultLimitForPath=30;
	
	@ProxyCodeSplit
	@NameToken(PlaceTokens.STUDENT)
	public interface IsStudentAssignmentProxy extends ProxyPlace<StudentAssignmentPresenter> {
	}
	
	//IndirectProvider<AssignmentPresenter> assignmentFactory;
	
	@Inject
	public StudentAssignmentPresenter(IsStudentAssignmentView view,IsStudentAssignmentProxy proxy,SignUpPresenter signUpViewPresenter) {
		super(view, proxy);
		this.signUpViewPresenter = signUpViewPresenter;
		getView().setUiHandlers(this);	
		
	}
	
	@Override
	public void onBind() {
		super.onBind();
	}	
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		AppClientFactory.setBrowserWindowTitle(SeoTokens.STUDY_TITLE);
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
//		if (!AppClientFactory.isAnonymous()){
//			getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.INLINE);
//			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
//		}
		
		if(AppClientFactory.getPlaceManager().getRequestParameter("b") != null){
			if (AppClientFactory.getPlaceManager().getRequestParameter("b").equalsIgnoreCase("true")){
				getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.INLINE);
				getView().getBackToEditPanel().setVisible(true);
				AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
				AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
			}else{
				getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.NONE);
				getView().getBackToEditPanel().setVisible(false);
				AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
				AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
			}
		}else{
			getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.NONE);
			getView().getBackToEditPanel().setVisible(false);
			AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
		}
		//Call Event for Setting Confirm popup
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
	}
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		if (AppClientFactory.getPlaceManager().refreshPlace()) {
			initParam();
		}
	}
	private void initParam() {
		getView().clearAll();
		getClasspage();
	}
	public void getClasspage(){
		String classpageId=getPlaceManager().getRequestParameter("id");
		this.classpageServiceAsync.getClasspage(classpageId, new SimpleAsyncCallback<ClasspageDo>() {
			@Override
			public void onSuccess(ClasspageDo classpageDo) {
				if(classpageDo!=null && classpageDo.getClasspageId() != null){
						offset=0;
						limit=5;
						getClasspageItems(classpageDo.getClasspageId(),offset.toString(),limit.toString(),false);
						getClasspageItems(classpageDo.getClasspageId(),""+defaultOffsetForPath,""+defaultLimitForPath,true);
						getView().setClasspageData(classpageDo);
						triggerClassPageNewDataLogStartStopEvent(classpageDo.getClasspageId(), classpageDo.getClasspageCode());
						
				}else{
					ErrorPopup error = new ErrorPopup(GL1632);
					error.center();
					error.show();
				}
			}
		});
	}
	public void getClasspageItems(String classpageId,String offset,String limit,final boolean isForAssignmentPath){
		this.classpageServiceAsync.getClassPageItems(classpageId, offset, limit, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {
			@Override
			public void onSuccess(ArrayList<ClasspageItemDo> classpageItemsList) {
				if(classpageItemsList!=null){
					if(isForAssignmentPath){
						getView().showClasspageItemsForAssignmentPath(classpageItemsList);
					}else{
						getView().showClasspageItems(classpageItemsList);
					}
				}
			}
		});
	}
	@Override
	public void getNextClasspageItems(Integer offset,Integer limit) {
		String classpageId=getPlaceManager().getRequestParameter("id");
		getClasspageItems( classpageId,offset.toString(),limit.toString(),false);
	}


	@Override
	protected void onReset() {
		super.onReset();
		if(AppClientFactory.getPlaceManager().getRequestParameter("b") != null){
			if (AppClientFactory.getPlaceManager().getRequestParameter("b").equalsIgnoreCase("true")){
				getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.INLINE);
				getView().getBackToEditPanel().setVisible(true);
				AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
				AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
			}else{
				getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.NONE);
				getView().getBackToEditPanel().setVisible(false);
				AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
				AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
			}
		}else{
			getView().getBackToEditPanel().getElement().getStyle().setDisplay(Display.NONE);
			getView().getBackToEditPanel().setVisible(false);
			AppClientFactory.setBrowserWindowTitle(SeoTokens.TEACH_TITLE);
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.TEACH));
		}
		//Call Event for Setting Confirm popup
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(true));
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		
		if (getPlaceManager().getRequestParameter("callback") != null && getPlaceManager().getRequestParameter("callback").equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			Window.enableScrolling(false);
			AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
			String type = getPlaceManager().getRequestParameter("type") ;
			int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
			signUpViewPresenter.displayPopup(displayScreen);
			addToPopupSlot(signUpViewPresenter);
		}
		
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.STUDENT;
	}
	
	public void setcollectionAsyncCallback(SimpleAsyncCallback<CollectionDo> collectionAsyncCallback) {
		this.collectionAsyncCallback = collectionAsyncCallback;
	}
	
	/** 
	 * This method is to get the assignmentListAsyncCallback
	 */
	public SimpleAsyncCallback<CollectionDo> getcollectionAsyncCallback() {
		return collectionAsyncCallback;
	}
	
	public ClasspageServiceAsync getclasspageServiceAsync() {
		return classpageServiceAsync;
	}
	
	public void asyMethodCall(){
		this.getclasspageServiceAsync().v2GetClasspageById(AppClientFactory.getPlaceManager().getRequestParameter("id"), getcollectionAsyncCallback());
		
	}
	public void getAssignmentsList(String pageNum,String pageSize){
		this.getclasspageServiceAsync().v2GetAssignemtsByClasspageId(AppClientFactory.getPlaceManager().getRequestParameter("id"), pageSize, pageNum, getAssignmentsListAsyncCallback());
	}

	public SimpleAsyncCallback<AssignmentsListDo> getAssignmentsListAsyncCallback() {
		return assignmentsListAsyncCallback;
	}

	public void setAssignmentsListAsyncCallback(
			SimpleAsyncCallback<AssignmentsListDo> assignmentsListAsyncCallback) {
		this.assignmentsListAsyncCallback = assignmentsListAsyncCallback;
	}

	public void triggerClassPageNewDataLogStartStopEvent(String classpageId, String classCode){
		JSONObject classpageDataLog=new JSONObject();
		String classpageEventId=GwtUUIDGenerator.uuid();
		AppClientFactory.getPlaceManager().setClasspageEventId(classpageEventId);
		classpageDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(classpageEventId));
		classpageDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.CLASSPAGE_VIEW));
		classpageDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(null));
		classpageDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		classpageDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(System.currentTimeMillis()));
		classpageDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(System.currentTimeMillis()));
		classpageDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(classpageId, "", "", "", "","",null,classpageId,"study"));
		classpageDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		classpageDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(0L, 0));
		classpageDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getClassPagePayLoadObject(classCode));
		PlayerDataLogEvents.collectionStartStopEvent(classpageDataLog);
	}
	@Override
	public void removeUserFromClass(ClasspageDo classpageDo, String emailId){
		AppClientFactory.getInjector().getClasspageService().removeStudentFromClass(classpageDo.getClasspageCode(), classpageDo.getSharing(), emailId, new SimpleAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				
			}
		});
		
	}

}
