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
package org.ednovo.gooru.client.mvp.classpages.unitdetails.personalize.AssignmentGoal;

import org.ednovo.gooru.client.child.ChildView;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollaboratorsDo;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @fileName : AssignmentGoalView.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: 10-Sep-2014
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class AssignmentGoalView extends ChildView<AssignmentGoalPresenter> implements IsAssignmentGoalView{

	private ClasspageItemDo classpageItemDo=null;
	CollaboratorsDo collaboratorsDo = null;
	private static AssignmentGoalUiBinder uiBinder = GWT.create(AssignmentGoalUiBinder.class);
	
	MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField Label lblStudentsList, lblPleaseWait;
	
	@UiField HTMLPanel panelAssignmentList;
	
	public interface AssignmentGoalUiBinder extends UiBinder<Widget, AssignmentGoalView> {}
	
	public AssignmentGoalView(CollaboratorsDo collaboratorsDo){
		initWidget(uiBinder.createAndBindUi(this));
		this.collaboratorsDo = collaboratorsDo; 
		setStaticTexts();
		setPresenter(new AssignmentGoalPresenter(this));
		
		AssignmentGoalCBundle.INSTANCE.css().ensureInjected();
		
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent event) {
	        
	        }
	    });
		
	}
	
	public void setStaticTexts(){
		lblStudentsList.setText(collaboratorsDo.getFirstName() + " " + collaboratorsDo.getLastName());
		StringUtil.setAttributes(lblStudentsList.getElement(), collaboratorsDo.getGooruOid(), null, null);
		
//		setAssignments();
	}
	@Override
	public void setAssignments(){
		lblPleaseWait.setVisible(false);
		for (int i=0; i<1; i++){
			GoalViewVc goalsVc = new GoalViewVc(""+(i+1)) {
			};
			panelAssignmentList.add(goalsVc);
		}
	}
	
}
