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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.assign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionEditShareEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListEvent;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.DateBoxUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.i18n.MessageProperties;
import org.ednovo.gooru.shared.model.content.ClassDo;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.ResourceDo;
import org.ednovo.gooru.shared.model.content.TaskResourceAssocDo;
import org.ednovo.gooru.shared.util.StringUtil;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @fileName : CollectionAssignViewTab.java
 *
 * @description : 
 *	This class is used to set the Editing collection to Assignment under Classpages.
 *
 * @version : 1.0
 *
 * @date: Jul 30, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class CollectionAssignTabView extends BaseViewWithHandlers<CollectionAssignTabUiHandlers> implements IsCollectionAssignTab {

	@UiField(provided = true)
	CollectionAssignCBundle res;
	
	boolean toClear=true;
	
	boolean isAdded = false;
	
	List<String> collectionsList = new ArrayList<String>();

	boolean toClearAssignment = true;
	
	boolean isAssignmentsEnabled = false;
	
	boolean isBadWord = false;

	private CollectionDo collectionDo;
	
	String classpageId=null;
	
	String unitId=null;
	
	String assignmentId=null;
	
	String limit="10";//pagesize	
	int classpageUnitOffSet=0;
	
	String minScore=null;
	String from = null;
	String to = null;
	
	boolean isMoreThanLimit=false;	//Limit = 10

	private static CollectionAssignViewTabUiBinder uiBinder = GWT.create(CollectionAssignViewTabUiBinder.class);
	
	private MessageProperties i18n = GWT.create(MessageProperties.class);

	interface CollectionAssignViewTabUiBinder extends UiBinder<Widget, CollectionAssignTabView> {
	}

	
	//Labels
	@UiField Label lblAssignCollectionTitle,lblClasspages,lblClasspagesUnit,lblClasspagePlaceHolder,lblClasspageUnitPlaceHolder, lblClasspagesArrow,lblClasspagesUnitArrow,lblDirections,directionsErrorLbl,directionsErrorLength;
	
	@UiField Label lblAssignCollectionPrivate,lblDuedate,remainderLbl,errorLabel,fromLbl,scoreLbl;
	
	@UiField BlueButtonUc btnAssign;
	
	@UiField ScrollPanel spanelClasspagesPanel,spanelClasspagesUnitPanel;
	
	@UiField HTMLPanel htmlClasspagesListContainer,duedateContainer,htmlClasspagesUnitListContainer,minsText,secondsText;
	
	@UiField HTMLEventPanel htmlEvenPanelContainer;
	
	@UiField HTMLPanel panelNoClasspages, panelLoading;
    @UiField Label lblNoClasspages,scoreErrorLabel,suggestTimeErrorLabel; 
    @UiField HTML htmlTab, htmlGoto,ancTeach;
    
    @UiField TextArea textAreaVal;
    
    @UiField TextBox fromTxt,toTxt,scoreTxt;
    
    //@UiField Image imgNotFriendly;
    
	private DateBoxUc dateBoxUc;
    
	/**
	 * Class constructor
	 */
	public CollectionAssignTabView() {
		
		res = CollectionAssignCBundle.INSTANCE;
		CollectionAssignCBundle.INSTANCE.css().ensureInjected();
		setWidget(uiBinder.createAndBindUi(this));
		
		setLabelsAndIds();
		
		showHideScrollPanel(false);
		
		hideContainers();
		
		fromTxt.getElement().setId("txtFromTxt");
		fromTxt.setFocus(true);
		toTxt.setFocus(true);
		toTxt.getElement().setId("txtToTxt");
		
		fromTxt.getElement().setAttribute("maxlength", "4");
		toTxt.getElement().setAttribute("maxlength", "4");
		fromTxt.addKeyPressHandler(new NumbersOnly());
		toTxt.addKeyPressHandler(new NumbersOnly());
		
		toTxt.addBlurHandler(new SuggestedTimeHandler());
		
		scoreTxt.getElement().setAttribute("placeholder", i18n.GL2179());
		scoreTxt.addBlurHandler(new ScoreHandler());
		
		btnAssign.getElement().setAttribute("style", "margin-right:25px;");
		
		lblDirections.setText(i18n.GL1166()+" "+i18n.GL1167());
		lblDirections.getElement().setId("lblDirections");
		lblDirections.getElement().setAttribute("alt",i18n.GL1166()+" "+i18n.GL1167());
		lblDirections.getElement().setAttribute("title",i18n.GL1166()+" "+i18n.GL1167());
		
		lblDuedate.setText(i18n.GL1168()+" "+i18n.GL1167());
		lblDuedate.getElement().setId("lblDuedate");
		lblDuedate.getElement().setAttribute("alt",i18n.GL1168()+" "+i18n.GL1167());
		lblDuedate.getElement().setAttribute("title",i18n.GL1168()+" "+i18n.GL1167());
		
		errorLabel.setVisible(false);
		scoreErrorLabel.setVisible(false);
		suggestTimeErrorLabel.setVisible(false);
		
		dateBoxUc = new DateBoxUc(false, false,false);
		duedateContainer.add(dateBoxUc);
		dateBoxUc.getDoneButton().addClickHandler(new OnDoneClick());
		textAreaVal.setText(i18n.GL1389());
		textAreaVal.getElement().setId("tatTextAreaVal");
		textAreaVal.getElement().setAttribute("alt",i18n.GL1389());
		textAreaVal.getElement().setAttribute("title",i18n.GL1389());
		StringUtil.setAttributes(textAreaVal, true);
		
		textAreaVal.getElement().getStyle().setColor("#999");
		textAreaVal.getElement().setAttribute("maxlength", "400");
		directionsErrorLength.setVisible(false);
		textAreaVal.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if(textAreaVal.getText().equalsIgnoreCase(i18n.GL1389()))
				{
					textAreaVal.setText("");
					textAreaVal.getElement().setAttribute("alt","");
					textAreaVal.getElement().setAttribute("title","");
				}
				textAreaVal.getElement().getStyle().setColor("black");
			}
		});
		
		/*textAreaVal.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(textAreaVal.getText().length() > 415)
				{
					textAreaVal.cancelKey();
				}
			}
		});*/
		textAreaVal.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(textAreaVal.getText().length() >=400){
					directionsErrorLength.setText(i18n.GL0143());
					directionsErrorLength.getElement().setAttribute("alt",i18n.GL0143());
					directionsErrorLength.getElement().setAttribute("title",i18n.GL0143());
					directionsErrorLength.setVisible(true);
				}else{
					directionsErrorLength.setVisible(false);
				}
				if(directionsErrorLbl.isVisible()){
					textAreaVal.getElement().getStyle().setBorderColor("#ccc");
					directionsErrorLbl.setVisible(false);
				}
				
			}
		});
		
		
		textAreaVal.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(textAreaVal.getText().length() == 0)
				{
					textAreaVal.setText(i18n.GL1461());
					textAreaVal.getElement().setAttribute("alt",i18n.GL1461());
					textAreaVal.getElement().setAttribute("title",i18n.GL1461());
					textAreaVal.getElement().getStyle().setColor("#999");
				}
				Map<String, String> parms = new HashMap<String, String>();
				parms.put("text", textAreaVal.getText());
				AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean value) {
						     isBadWord=value;
							SetStyleForProfanity.SetStyleForProfanityForTextArea(textAreaVal, directionsErrorLbl, value);
							directionsErrorLbl.setStyleName(res.css().directionsErrorLbl());
					}
				});
			}
		});
		
		spanelClasspagesPanel.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				if (spanelClasspagesPanel.getVerticalScrollPosition() == spanelClasspagesPanel.getMaximumVerticalScrollPosition()){
					toClear = false;
					getUiHandlers().getNextClasspages();
				}
			}
		});
		
		spanelClasspagesUnitPanel.addScrollHandler(new ScrollHandler() {
			
			@Override
			public void onScroll(ScrollEvent event) {
				if(spanelClasspagesUnitPanel.getVerticalScrollPosition() == spanelClasspagesUnitPanel.getMaximumHorizontalScrollPosition()){
					toClear = false;
					getNextClasspagesUnit();
				}
			}
		});

	}
	
	@Override
	public void onUnload() {
		setCollectionDo(null);
		
		setLabelsAndIds();
		
		showHideScrollPanel(false);
		
		toClear= true;
		
		//htmlAssignmentsListContainer.clear();
		htmlClasspagesListContainer.clear();
		htmlClasspagesUnitListContainer.clear();
		//lblAssignmentErrorMsg.setVisible(false);
	}

	@Override
	public void reset() {
		super.reset();
		//lblAssignmentErrorMsg.setVisible(false);
	}
	
	/**
	 * 
	 * @function setLabelsAndIds 
	 * 
	 * @created_date : Jul 30, 2013
	 * 
	 * @description
	 * 	To set the default values for labels, button and id for button.
	 * 
	 * @parm(s) : NONE
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setLabelsAndIds(){
		
		remainderLbl.setText(i18n.GL1889());
		remainderLbl.getElement().setId("lblRemainderLbl");
		remainderLbl.getElement().setAttribute("alt",i18n.GL1889());
		remainderLbl.getElement().setAttribute("title",i18n.GL1889());
		
		lblAssignCollectionPrivate.setText(i18n.GL0112());
		lblAssignCollectionPrivate.getElement().setId("lblAssignCollectionPrivate");
		lblAssignCollectionPrivate.getElement().setAttribute("alt",i18n.GL0112());
		lblAssignCollectionPrivate.getElement().setAttribute("title",i18n.GL0112());
		
		lblAssignCollectionPrivate.setVisible(false);
		
		lblAssignCollectionTitle.setText(i18n.GL0101());
		lblAssignCollectionTitle.getElement().setId("lblAssignCollectionTitle");
		lblAssignCollectionTitle.getElement().setAttribute("alt",i18n.GL0101());
		lblAssignCollectionTitle.getElement().setAttribute("title",i18n.GL0101());
		
		lblClasspages.setText(i18n.GL0102());
		lblClasspages.getElement().setId("lblClasspages");
		lblClasspages.getElement().setAttribute("alt",i18n.GL0102());
		lblClasspages.getElement().setAttribute("title",i18n.GL0102());
		
		lblClasspagesUnit.setText(i18n.GL2175());
		lblClasspagesUnit.getElement().setId("lblClasspagesUnit");
		lblClasspagesUnit.getElement().setAttribute("alt",i18n.GL2175());
		lblClasspagesUnit.getElement().setAttribute("title",i18n.GL2175());
		
		//lblAssignments.setText(i18n.GL0103);
		
		btnAssign.setText(i18n.GL0104());
		btnAssign.getElement().setAttribute("alt",i18n.GL0104());
		btnAssign.getElement().setAttribute("title",i18n.GL0104());
		
		lblClasspagePlaceHolder.setText(i18n.GL0105());
		lblClasspagePlaceHolder.getElement().setId("lblClasspagePlaceHolder");
		lblClasspagePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
		lblClasspagePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
		//lblAssignmentsPlaceHolder.setText(i18n.GL0105);
		
		lblClasspageUnitPlaceHolder.setText(i18n.GL0105());
		lblClasspageUnitPlaceHolder.getElement().setId("lblClasspagePlaceHolder");
		lblClasspageUnitPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
		lblClasspageUnitPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
		
		fromLbl.getElement().setId("lblFromLbl");
		fromLbl.setText(i18n.GL2180());
		
		scoreLbl.getElement().setId("lblScoreLbl");
		scoreLbl.setText(i18n.GL2181());
		
		minsText.getElement().setInnerHTML(i18n.GL1436());
		minsText.getElement().setId("pnlMinsText");
		minsText.getElement().setAttribute("alt", i18n.GL1436());
		minsText.getElement().setAttribute("title", i18n.GL1436());
		
		secondsText.getElement().setInnerHTML(i18n.GL0958());
		secondsText.getElement().setId("pnlSecondsText");
		secondsText.getElement().setAttribute("alt", i18n.GL0958());
		secondsText.getElement().setAttribute("title", i18n.GL0958());
		
		
		lblNoClasspages.setText(i18n.GL0106());
		lblNoClasspages.getElement().setId("lblNoClasspages");
		lblNoClasspages.getElement().setAttribute("alt",i18n.GL0106());
		lblNoClasspages.getElement().setAttribute("title",i18n.GL0106());
//		htmlGoto.setHTML(MessageProperties.i18n.GL0107);
//		ancTeach.setText(MessageProperties.i18n.GL0108);
		htmlTab.setHTML(i18n.GL0109());
		htmlTab.getElement().setId("htmlTab");
		htmlTab.getElement().setAttribute("alt",i18n.GL0109());
		htmlTab.getElement().setAttribute("title",i18n.GL0109());
		//ancTeach.setHref("#"+PlaceTokens.TEACH);
		
		//Ids
		btnAssign.getElement().setAttribute("id", "btnAssign");
		btnAssign.setStyleName(res.css().disableAssignButon());
		btnAssign.setText(i18n.GL0104());
		btnAssign.getElement().setAttribute("alt",i18n.GL0104());
		btnAssign.getElement().setAttribute("title",i18n.GL0104());
		btnAssign.setEnabled(false);
		btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());

		panelLoading.getElement().setId("pnlPanelLoading");
		panelNoClasspages.getElement().setId("pnlPanelNoClasspages");
		htmlGoto.getElement().setId("htmlGoto");
		ancTeach.getElement().setId("htmlAncTeach");
		htmlEvenPanelContainer.getElement().setId("epnlHtmlEvenPanelContainer");
		lblClasspagesArrow.getElement().setId("lblClasspagesArrow");
		lblClasspagesUnitArrow.getElement().setId("lblClasspagesUnitArrow");
		spanelClasspagesPanel.getElement().setId("sbSpanelClasspagesPanel");
		spanelClasspagesUnitPanel.getElement().setId("sbspanelClasspagesUnitPanel");
		htmlClasspagesListContainer.getElement().setId("pnlHtmlClasspagesListContainer");
		htmlClasspagesUnitListContainer.getElement().setId("pnlHtmlClasspagesUnitListContainer");
		duedateContainer.getElement().setId("pnlDuedateContainer");
		directionsErrorLength.getElement().setId("lblDirectionsErrorLength");
		directionsErrorLbl.getElement().setId("lblDirectionsErrorLbl");
		
		
		/*imgNotFriendly.setTitle(i18n.GL0732());
		imgNotFriendly.getElement().setId("moveCollectionLbl");
		imgNotFriendly.getElement().setAttribute("alt",i18n.GL0732());
		imgNotFriendly.getElement().setAttribute("title",i18n.GL0732());
		imgNotFriendly.setAltText(i18n.GL0732());
		imgNotFriendly.setUrl("images/mos/questionmark.png");*/
	}
	/**
	 * 
	 * @function showHideScrollPanel 
	 * 
	 * @created_date : Jul 30, 2013
	 * 
	 * @description
	 * 	Set the visibility based on params
	 * 
	 * @parm(s) : @param visible
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void showHideScrollPanel(boolean visible){
		spanelClasspagesPanel.setVisible(visible);
		spanelClasspagesUnitPanel.setVisible(visible);
	}
	
	//UI Handlers
	
	@UiHandler("lblClasspagePlaceHolder")
	public void OnClickClasspagePlaceHolder(ClickEvent event){
		OpenClasspageContainer();
	}
	@UiHandler("lblClasspagesArrow")
	public void OnClickClasspageArrow(ClickEvent event){
		OpenClasspageContainer();
	}
	public void OpenClasspageContainer(){
		spanelClasspagesPanel.setVisible(!spanelClasspagesPanel.isVisible());
	}
	
	@UiHandler("lblClasspageUnitPlaceHolder")
	public void OnClickClasspageUnitPlaceHolder(ClickEvent event){
		OpenClasspageUnitContainer();
	}
	
	@UiHandler("lblClasspagesUnitArrow")
	public void OnClickClasspageUnitArrow(ClickEvent event){
		OpenClasspageUnitContainer();
	}
	
	public void OpenClasspageUnitContainer(){
		spanelClasspagesUnitPanel.setVisible(!spanelClasspagesUnitPanel.isVisible());
	}

	public void updateShare(String shareType) {
		AppClientFactory.getInjector().getResourceService().updateCollectionMetadata(collectionDo.getGooruOid(), null, null, null, shareType, null, null, null, null, null, new SimpleAsyncCallback<CollectionDo>() {

			@Override
			public void onSuccess(CollectionDo result) {
				collectionDo = result;
				AppClientFactory.fireEvent(new CollectionEditShareEvent(result.getSharing()));
			}
		});
	}
	
	@UiHandler("btnAssign")
	public void OnClickAssign(ClickEvent event){
		btnAssign.setEnabled(false);
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", textAreaVal.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean value) {
				if(value){
					SetStyleForProfanity.SetStyleForProfanityForTextArea(textAreaVal, directionsErrorLbl, value);
					directionsErrorLbl.setStyleName(res.css().directionsErrorLbl());
					btnAssign.setEnabled(true);
				}
				else
				{		
					btnAssign.getElement().setAttribute("id", "btnAssign");
					btnAssign.setStyleName(res.css().disableAssignButon());
					btnAssign.setText(i18n.GL1172());
					btnAssign.getElement().setAttribute("alt",i18n.GL1172());
					btnAssign.getElement().setAttribute("title",i18n.GL1172());
					//btnAssign.getElement().getStyle().setMarginRight(17, Unit.PCT);
				
					btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
					
					TaskResourceAssocDo taskResourceAssocDo = new TaskResourceAssocDo();
					ResourceDo resourceDo = new ResourceDo();
					resourceDo.setGooruOid(collectionDo.getGooruOid());
					taskResourceAssocDo.setResource(resourceDo);
					
					//Track Mixpanel
					MixpanelUtil.Click_Assign_Click();
					// Api call for adding Collection to Assignment
					
					if (collectionDo.getSharing().equalsIgnoreCase("private")){
						updateShare("anyonewithlink");
						getUiHandlers().setShareType("anyonewithlink");
						lblAssignCollectionPrivate.setVisible(false);
						collectionDo.setSharing("anyonewithlink");
					}else{
						
					}
					//Track Mixpanel event if user is Collaborator.
					if (collectionDo.getMeta()!=null && collectionDo.getMeta().isIsCollaborator()){
						MixpanelUtil.mixpanelEvent("Collaborator_assigns_collection");
					}
					String directionsVal = textAreaVal.getText();
					if(textAreaVal.getText().equalsIgnoreCase(i18n.GL1389()))
					{
						directionsVal = "";
					}
					if(directionsVal.isEmpty())
					{
						directionsVal = null;
					}
					String dueDateVal = dateBoxUc.getDateBox().getValue();
					if(dueDateVal.isEmpty())
					{
						dueDateVal = null;
					}
					AppClientFactory.getInjector().getClasspageService().v2AssignCollectionTOPathway(classpageId, unitId, collectionDo.getGooruOid(),from,minScore,dueDateVal, directionsVal, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {

						@Override
						public void onSuccess(ArrayList<ClasspageItemDo> result) {
							if(!AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
								AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(collectionDo, RefreshType.INSERT));
							}
							
							btnAssign.setText(i18n.GL0104());
							btnAssign.getElement().setAttribute("alt",i18n.GL0104());
							btnAssign.getElement().setAttribute("title",i18n.GL0104());
							SuccessPopupVc successPopupVc = new SuccessPopupVc(classpageId, collectionDo.getTitle(), lblClasspagePlaceHolder.getText()) {
								
								@Override
								public void closePoup() {
						
									
									lblClasspagePlaceHolder.setText(i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
									lblClasspagePlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().placeHolderText());
									lblAssignCollectionPrivate.setVisible(false);
									
									
									lblClasspageUnitPlaceHolder.setText(i18n.GL0105());
									lblClasspageUnitPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
									lblClasspageUnitPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
									lblClasspageUnitPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().placeHolderText());
									
									scoreTxt.setText("");
									scoreTxt.getElement().setAttribute("placeholder", i18n.GL2179());
									
									fromTxt.setText("");
									toTxt.setText("");
									htmlClasspagesListContainer.clear();
									htmlClasspagesUnitListContainer.clear();
									getUiHandlers().getAllClasspages("10", "0");
									
									Window.enableScrolling(true);
									AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
									
									textAreaVal.setText("");
									textAreaVal.getElement().setAttribute("alt","");
									textAreaVal.getElement().setAttribute("title","");
									dateBoxUc.getDateBox().setValue("");
								
									btnAssign.getElement().setAttribute("id", "btnAssign");
									btnAssign.setStyleName(res.css().disableAssignButon());
									btnAssign.setText(i18n.GL0104());
									btnAssign.getElement().setAttribute("alt",i18n.GL0104());
									btnAssign.getElement().setAttribute("title",i18n.GL0104());
									btnAssign.setEnabled(false);
									btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
									
							        this.hide();
								}
							};
							successPopupVc.center();
							successPopupVc.show();
							
						}
					});
					
					/*AppClientFactory.getInjector().getClasspageService().assignItemToClass(classpageId, collectionDo.getGooruOid(),dueDateVal, directionsVal, new SimpleAsyncCallback<ArrayList<ClasspageItemDo>>() {

						@Override
						public void onSuccess(ArrayList<ClasspageItemDo> result) {
							// TODO Auto-generated method stub
							if(!AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
								AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(collectionDo, RefreshType.INSERT));
							}
							
							btnAssign.setText(i18n.GL0104());
							btnAssign.getElement().setAttribute("alt",i18n.GL0104());
							btnAssign.getElement().setAttribute("title",i18n.GL0104());
							SuccessPopupVc successPopupVc = new SuccessPopupVc(classpageId, collectionDo.getTitle(), lblClasspagePlaceHolder.getText()) {
								
								@Override
								public void closePoup() {
						
									
									lblClasspagePlaceHolder.setText(i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
									lblClasspagePlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().placeHolderText());
									lblAssignCollectionPrivate.setVisible(false);
									
									htmlClasspagesListContainer.clear();
									getUiHandlers().getAllClasspages("10", "0");
									
									Window.enableScrolling(true);
									AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
									
									textAreaVal.setText("");
									textAreaVal.getElement().setAttribute("alt","");
									textAreaVal.getElement().setAttribute("title","");
									dateBoxUc.getDateBox().setValue("");
								
									btnAssign.getElement().setAttribute("id", "btnAssign");
									btnAssign.setStyleName(res.css().disableAssignButon());
									btnAssign.setText(i18n.GL0104());
									btnAssign.getElement().setAttribute("alt",i18n.GL0104());
									btnAssign.getElement().setAttribute("title",i18n.GL0104());
									btnAssign.setEnabled(false);
									btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
									
							        this.hide();
								}
							};
							successPopupVc.center();
							successPopupVc.show();
							
						}
					});*/
					
					
				/*	AppClientFactory.getInjector().getClasspageService().createClassPageItem(classpageId, collectionDo.getGooruOid() ,dueDateVal, directionsVal, new SimpleAsyncCallback<ClasspageItemDo>() {
						@Override
						public void onSuccess(ClasspageItemDo result) {
							
							if(!AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
								AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(collectionDo, RefreshType.INSERT));
							}
							
							btnAssign.setText(i18n.GL0104());
							btnAssign.getElement().setAttribute("alt",i18n.GL0104());
							btnAssign.getElement().setAttribute("title",i18n.GL0104());
							SuccessPopupVc successPopupVc = new SuccessPopupVc(classpageId, collectionDo.getTitle(), lblClasspagePlaceHolder.getText()) {
								
								@Override
								public void closePoup() {
						
									
									lblClasspagePlaceHolder.setText(i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
									lblClasspagePlaceHolder.getElement().setAttribute("title",i18n.GL0105());
									lblClasspagePlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().placeHolderText());
									lblAssignCollectionPrivate.setVisible(false);
									
									htmlClasspagesListContainer.clear();
									getUiHandlers().getAllClasspages("10", "0");
									
									Window.enableScrolling(true);
									AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
									
									textAreaVal.setText("");
									textAreaVal.getElement().setAttribute("alt","");
									textAreaVal.getElement().setAttribute("title","");
									dateBoxUc.getDateBox().setValue("");
								
									btnAssign.getElement().setAttribute("id", "btnAssign");
									btnAssign.setStyleName(res.css().disableAssignButon());
									btnAssign.setText(i18n.GL0104());
									btnAssign.getElement().setAttribute("alt",i18n.GL0104());
									btnAssign.getElement().setAttribute("title",i18n.GL0104());
									btnAssign.setEnabled(false);
									btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
									
							        this.hide();
								}
							};
							successPopupVc.center();
							successPopupVc.show();
							
							//Need to handle if Collection is already added.
			//				{"code":500,"status":"Resource already associated"}
						}
			//			@Override
			//			public void onFailure(Throwable caught) {
			//				
			//			}
					});*/
		}
		}
		});
	}
	
	
	@UiHandler("htmlEvenPanelContainer")
	public void OnClickEventPanel(ClickEvent event){
//		spanelAssignmentsPanel.setVisible(false);
//		spanelClasspagesPanel.setVisible(false);
	}
	
	
	/**
	 * 
	 * @function setClasspageData 
	 * 
	 * @created_date : Jul 31, 2013
	 * 
	 * @description
	 * 		Create Classpage (title) label and set to Classpage list box
	 * 
	 * @parm(s) : @param classpageListDo
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	@Override
	public void setClasspageData(ClasspageListDo classpageListDo){
		panelLoading.setVisible(false);
		int resultSize = classpageListDo.getSearchResults().size();
		if (resultSize > 0){
			htmlEvenPanelContainer.setVisible(true);
			if (toClear){
				htmlClasspagesListContainer.clear();
				toClear=false;
			}
			for (int i = 0; i < resultSize; i++) {
				String classpageTitle = classpageListDo.getSearchResults().get(i).getTitle();
				classpageId = classpageListDo.getSearchResults().get(i).getGooruOid();
				final Label titleLabel = new Label(classpageTitle);
				titleLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				titleLabel.getElement()
						.setAttribute("id", classpageId);
				//Set Click event for title
				titleLabel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {						
						lblClasspagePlaceHolder.setText(titleLabel.getText());
						lblClasspagePlaceHolder.getElement().setAttribute("alt",titleLabel.getText());
						lblClasspagePlaceHolder.getElement().setAttribute("title",titleLabel.getText());
						lblClasspagePlaceHolder.getElement().setId(titleLabel.getElement().getId());
						lblClasspagePlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
						
						classpageId = titleLabel.getElement().getId();
						
						btnAssign.setEnabled(true);
						btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
						//btnAssign.setStyleName(AssignPopUpCBundle.INSTANCE.css().activeAssignButton());

						
						AppClientFactory.getInjector().getClasspageService().v2GetPathwaysOptimized(classpageId, limit, String.valueOf(classpageUnitOffSet), new SimpleAsyncCallback<ClassDo>() {

							@Override
							public void onSuccess(ClassDo result) {
								htmlClasspagesUnitListContainer.clear();
								setUnitList(result);
							}
						});
						
						//Hide the scroll container
						spanelClasspagesPanel.setVisible(false);
					}
				});
				htmlClasspagesListContainer.add(titleLabel);
				
			}
		}else{
			//Set if there are not classpages.
			if (toClear){
				panelNoClasspages.setVisible(true);
			}
		}
	}
	
	public void setUnitList(ClassDo classpageListDo) {
		Label unitLabel = null;
		int resultSize = classpageListDo.getSearchResults().size();
		errorLabel.setVisible(false);
		if (resultSize > 0){
			//htmlClasspagesUnitListContainer.clear();
			for(int i=0;i<resultSize;i++){
				unitId = classpageListDo.getSearchResults().get(i).getResource().getGooruOid();
				String unitTitle = classpageListDo.getSearchResults().get(i).getResource().getTitle();
				unitLabel = new Label(unitTitle);
				unitLabel.setStyleName(CollectionAssignCBundle.INSTANCE.css().classpageTitleText());
				unitLabel.getElement().setAttribute("id", unitId);
				unitLabel.addClickHandler(new CpuTitleLabelClick(unitLabel));
				htmlClasspagesUnitListContainer.add(unitLabel);
			}
			lblClasspageUnitPlaceHolder.setText(classpageListDo.getSearchResults().get(0).getResource().getTitle());
			lblClasspageUnitPlaceHolder.getElement().setId(classpageListDo.getSearchResults().get(0).getResource().getGooruOid());
			lblClasspageUnitPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
			
			unitId = classpageListDo.getSearchResults().get(0).getResource().getGooruOid();
			
			btnAssign.setEnabled(true);
			btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());

			
			//Hide the scroll container
			spanelClasspagesUnitPanel.setVisible(false);
		}else{
				htmlClasspagesUnitListContainer.clear();
				lblClasspageUnitPlaceHolder.setText(i18n.GL0105());
				lblClasspageUnitPlaceHolder.getElement().setId("lblClasspagePlaceHolder");
				lblClasspageUnitPlaceHolder.getElement().setAttribute("alt",i18n.GL0105());
				lblClasspageUnitPlaceHolder.getElement().setAttribute("title",i18n.GL0105());
				lblClasspageUnitPlaceHolder.removeStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
				lblClasspageUnitPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().placeHolderText());
				errorLabel.setVisible(true);
				errorLabel.setText(i18n.GL2176());
				btnAssign.setEnabled(false);
				btnAssign.removeStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
				btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
		}
	}
	
public class CpuTitleLabelClick implements ClickHandler{
		
		private Label unitLabel;
		public CpuTitleLabelClick(Label unitLabel){
			this.unitLabel = unitLabel;
		}


		@Override
		public void onClick(ClickEvent event) {
			lblClasspageUnitPlaceHolder.setText(unitLabel.getText());
			lblClasspageUnitPlaceHolder.getElement().setId(unitLabel.getElement().getId());
			lblClasspageUnitPlaceHolder.setStyleName(CollectionAssignCBundle.INSTANCE.css().selectedClasspageText());
			
			unitId = unitLabel.getElement().getId();
			
			
			btnAssign.setEnabled(true);
			btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());

			
			//Hide the scroll container
			spanelClasspagesUnitPanel.setVisible(false);
		}
		
	}
	
	private class OnDoneClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if (dateBoxUc.dateValidation()){
				if (!(dateBoxUc.getValue() == null || dateBoxUc.getDateBox()
						.getText().isEmpty())
						&& dateBoxUc.hasValidateDate()) {
				Date date = dateBoxUc.getValue();
				
				} else {
					dateBoxUc.getDatePickerUc().hide();
				}
			}
		}
	}

	
	
	/** 
	 * This method is to get the toClear
	 */
	public boolean isToClear() {
		return toClear;
	}

	/** 
	 * This method is to set the toClear
	 */
	public void setToClear(boolean toClear) {
		this.toClear = toClear;
	}
	/** 
	 * This method is to get the collectionDo
	 */
	public CollectionDo getCollectionDo() {
		return collectionDo;
	}

	/** 
	 * This method is to set the collectionDo
	 */
	@Override
	public void setCollectionDo(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
	}

	
	@Override
	public void hideContainers(){
		htmlClasspagesListContainer.clear();
		htmlEvenPanelContainer.setVisible(false);
		panelNoClasspages.setVisible(false);
		panelLoading.setVisible(true);
	}
	
	@Override
	public void setPrivateLableVisibility(boolean visibility) {
		lblAssignCollectionPrivate.setVisible(visibility);
	}

	public void getNextClasspagesUnit(){
		classpageUnitOffSet = classpageUnitOffSet+10;
		getAllClasspagesUnit(limit,String.valueOf(classpageUnitOffSet));
	}
	
	public void getAllClasspagesUnit(String limit,String offset){
		AppClientFactory.getInjector().getClasspageService().v2GetPathwaysOptimized(classpageId, limit, String.valueOf(classpageUnitOffSet), new SimpleAsyncCallback<ClassDo>() {

			@Override
			public void onSuccess(ClassDo result) {
				setUnitList(result);
			}
		});
	}
	private class NumbersOnly implements KeyPressHandler {
	      
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
	private class ScoreHandler implements BlurHandler{

		
		@Override
		public void onBlur(BlurEvent event) {
			String score = scoreTxt.getText();
			if(score != null || score!=""){
				if(Integer.parseInt(score) >100 || Integer.parseInt(score) <=0){
					scoreErrorLabel.setText(i18n.GL2178());
					scoreErrorLabel.setVisible(true);
					btnAssign.setEnabled(false);
					btnAssign.removeStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
					btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
				}else{
					minScore=score;
					scoreErrorLabel.setVisible(false);
					btnAssign.setEnabled(true);
					btnAssign.removeStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
					btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
				}
			}
		}
		
	}
	private class SuggestedTimeHandler implements BlurHandler{

		
		@Override
		public void onBlur(BlurEvent event) {
			if (fromTxt.getText().length() > 0 && toTxt.getText().length() > 0) {
				from = fromTxt.getText();
				fromTxt.setText(from);
				fromTxt.getElement().setAttribute("alt", from);
				fromTxt.getElement().setAttribute("title", from);
				from = toTxt.getText();
				toTxt.setText(from);
				toTxt.getElement().setAttribute("alt", from);
				toTxt.getElement().setAttribute("title", from);
				String startTimeTxtMin = null;
				String startTimeTxtSec = null;
				if (fromTxt.getText().length() < 2) {
					startTimeTxtMin = "0" + fromTxt.getText();
	
				} else {
					startTimeTxtMin = fromTxt.getText();
				}
				if (toTxt.getText().length() < 2) {
					startTimeTxtSec = "0" + toTxt.getText();
				} else if(Integer.parseInt(toTxt.getText()) > 59) {
					startTimeTxtSec = "0" + toTxt.getText();
					suggestTimeErrorLabel.setText(i18n.GL0970());
					suggestTimeErrorLabel.setVisible(true);
					btnAssign.setEnabled(false);
					btnAssign.removeStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
					btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
				}else{
					startTimeTxtSec = toTxt.getText();
					from = startTimeTxtMin+"hrs" + " " + startTimeTxtSec+"mins";
					suggestTimeErrorLabel.setVisible(false);
					btnAssign.setEnabled(true);
					btnAssign.removeStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());
					btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().activeAssignButton());
				}
			}
		}
		
	}
}
