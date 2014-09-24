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
package org.ednovo.gooru.client.mvp.classpages.unitdetails;



import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClassDo;
import org.ednovo.gooru.shared.model.content.ClassUnitsListDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
*
* @description : This class used to show student score in Assignments
*
* @version :1.1
*
* @date: Sep 16 2014
   	
* @Author Gooru Team
* 
* Reviewer Gooru Team
*
*/
public class ScoreHedingView extends Composite {

	private static ScoreHedingViewUiBinder uiBinder = GWT
			.create(ScoreHedingViewUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	interface ScoreHedingViewUiBinder extends UiBinder<Widget, ScoreHedingView> {
	}
	
	@UiField Label lblTitle,lblControl,lblRedControl,lblScore;
	
	@UiField TextBox txtScore;
	
	@UiField Button btnSetGoal;
	
	String collectionItemId;
	
	ClassUnitsListDo classUnitsListDo;
	
	private ClassDo classDo;
	
	private int redScore, finalScore;
	
	private String SETGOAL= i18n.GL2197();
	
	private String EDITGOAL= i18n.GL2196();
	
	

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public ScoreHedingView(ClassUnitsListDo classUnitsListDo) {
		this.classUnitsListDo=classUnitsListDo;
		initWidget(uiBinder.createAndBindUi(this));
		lblTitle.setText(i18n.GL2195());
		btnSetGoal.setText(SETGOAL);
		txtScore.addBlurHandler(new ScoreHandler());
		txtScore.addKeyPressHandler(new HasNumbersOnly());
		txtScore.setMaxLength(3);
		lblControl.getElement().setId("controll");
		lblScore.setVisible(false);
	}
	
	
	public class ScoreHandler implements BlurHandler{
		
		@Override
		public void onBlur(BlurEvent event) {
			String score = txtScore.getText();
			 if(score != null || score != ""){
				try{
					if(Integer.parseInt(score) >100 || Integer.parseInt(score)<0){
						lblControl.getElement().setId("controll");
						txtScore.setText(getValidationScore(score));
					}else{
						int scoreValue= Integer.parseInt(txtScore.getText());
						finalScore=((scoreValue*176)/100);
						finalScore=finalScore-176;
					    redScore=((50*66)/100);
						redScore=redScore-119;
						lblControl.getElement().setAttribute("style", "-webkit-transform: rotate("+finalScore+"deg);");
					}
				}catch(NumberFormatException numberFormatException){
					numberFormatException.printStackTrace();
				}
				
			}
		}

	}
	/**
	 * Get the valid assignments average score
	 * @param score
	 * @return valid score
	 */
	
	private String getValidationScore(String score) {
		// TODO Auto-generated method stub
		if(Integer.parseInt(score) >100){
			return "100";
		}
		if(Integer.parseInt(score)<0){
			
			return "0";
		}
		return null;
	}
	
	public class HasNumbersOnly implements KeyPressHandler {
	      
		@Override
		public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode()) 
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB 
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_SHIFT
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_ENTER
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_LEFT
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_RIGHT
	                    && event.getNativeEvent().getKeyCode() != KeyCodes.KEY_DELETE){
	                ((TextBox) event.getSource()).cancelKey();
	            }
					
		}
    }
	
	@UiHandler("btnSetGoal")
	public void clickOnSetGoal(ClickEvent clickEvent){
		
		if(txtScore.getText()!=null){
			if(btnSetGoal.getText().equals(SETGOAL)){
				System.out.println("redScore:"+redScore);
				showAndHideTextBox();
				lblScore.setText(txtScore.getText());
				btnSetGoal.setStyleName("secondary");
				btnSetGoal.setText(EDITGOAL);
				lblRedControl.getElement().setId("redControll");
				if(Integer.parseInt(txtScore.getText())<=50){
					lblRedControl.getElement().setAttribute("style", "-webkit-transform: rotate("+redScore+"deg); background: none repeat scroll 0 0 #a0c79a;");
					lblScore.getElement().setAttribute("style", "color:#a0c79a");
				}else{
					lblRedControl.getElement().setAttribute("style", "-webkit-transform: rotate("+redScore+"deg);");
					lblScore.getElement().setAttribute("style", "color:#fb7c73");
				}
				collectionItemId=classUnitsListDo.getCollectionItemId();
				if(collectionItemId!=null){
					if(getLblTitle().getText().equals(i18n.GL2195())){
						updateUnitstatus(collectionItemId, txtScore.getText(), "", "");
					}else{
						updateUnitstatus(collectionItemId, "", txtScore.getText(), "");
					}
				}
				
				
			}else{
				showAndHideTextBox();
				btnSetGoal.setStyleName("primary");
				btnSetGoal.setText(SETGOAL);
				lblRedControl.getElement().setId("redControll");
			}
			
		}
	}
	/**
	 * show and hide text boxes
	 */
	public void showAndHideTextBox(){
		if(btnSetGoal.getText().equals(SETGOAL)){
			txtScore.setVisible(false);
			lblScore.setVisible(true);
		}else{
			txtScore.setVisible(true);
			lblScore.setVisible(false);
		}
	}
	public Label getLblTitle() {
		return lblTitle;
	}
	
	public void updateUnitstatus(String collectionItemId, String minimumScoreByuser, String assignmentStatus, String time){
		AppClientFactory.getInjector().getClasspageService().updateUnitStatus(collectionItemId, minimumScoreByuser,assignmentStatus,time, new SimpleAsyncCallback<ClasspageItemDo>() {

			@Override
			public void onSuccess(ClasspageItemDo result) {
				// TODO Auto-generated method stub
				System.out.println("mini::::::"+result.getMinimumScoreByUser());
				
			}
		});
	}
	
	
}
