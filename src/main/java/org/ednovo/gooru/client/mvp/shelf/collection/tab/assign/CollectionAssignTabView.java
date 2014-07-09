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
import org.ednovo.gooru.client.mvp.shelf.event.RefreshUserShelfCollectionsEvent;
import org.ednovo.gooru.client.uc.BlueButtonUc;
import org.ednovo.gooru.client.uc.DateBoxUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.ResourceDo;
import org.ednovo.gooru.shared.model.content.TaskResourceAssocDo;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
public class CollectionAssignTabView extends BaseViewWithHandlers<CollectionAssignTabUiHandlers> implements IsCollectionAssignTab,MessageProperties {

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
	
	String assignmentId=null;
	
	boolean isMoreThanLimit=false;	//Limit = 10

	private static CollectionAssignViewTabUiBinder uiBinder = GWT.create(CollectionAssignViewTabUiBinder.class);

	interface CollectionAssignViewTabUiBinder extends UiBinder<Widget, CollectionAssignTabView> {
	}

	
	//Labels
	@UiField Label lblAssignCollectionTitle,lblClasspages,lblClasspagePlaceHolder, lblClasspagesArrow,lblDirections,directionsErrorLbl,directionsErrorLength;
	
	@UiField Label lblAssignCollectionPrivate,lblDuedate,remainderLbl;
	
	@UiField BlueButtonUc btnAssign;
	
	@UiField ScrollPanel spanelClasspagesPanel;
	
	@UiField HTMLPanel htmlClasspagesListContainer,duedateContainer;
	
	@UiField HTMLEventPanel htmlEvenPanelContainer;
	
	@UiField HTMLPanel panelNoClasspages, panelLoading;
    @UiField Label lblNoClasspages; 
    @UiField HTML htmlTab, htmlGoto,ancTeach;
    
    @UiField TextArea textAreaVal;
    
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
		
		btnAssign.getElement().setAttribute("style", "margin-right:25px;");
		
		lblDirections.setText(GL1166+" "+GL1167);
		lblDirections.getElement().setId("lblDirections");
		lblDirections.getElement().setAttribute("alt",GL1166+" "+GL1167);
		lblDirections.getElement().setAttribute("title",GL1166+" "+GL1167);
		
		lblDuedate.setText(GL1168+" "+GL1167);
		lblDuedate.getElement().setId("lblDuedate");
		lblDuedate.getElement().setAttribute("alt",GL1168+" "+GL1167);
		lblDuedate.getElement().setAttribute("title",GL1168+" "+GL1167);
		
		dateBoxUc = new DateBoxUc(false, false,false);
		duedateContainer.add(dateBoxUc);
		dateBoxUc.getDoneButton().addClickHandler(new OnDoneClick());
		textAreaVal.setText(GL1389);
		textAreaVal.getElement().setId("tatTextAreaVal");
		textAreaVal.getElement().setAttribute("alt",GL1389);
		textAreaVal.getElement().setAttribute("title",GL1389);
		
		textAreaVal.getElement().getStyle().setColor("#999");
		textAreaVal.getElement().setAttribute("maxlength", "400");
		directionsErrorLength.setVisible(false);
		textAreaVal.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if(textAreaVal.getText().equalsIgnoreCase(GL1389))
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
					directionsErrorLength.setText(GL0143);
					directionsErrorLength.getElement().setAttribute("alt",GL0143);
					directionsErrorLength.getElement().setAttribute("title",GL0143);
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
					textAreaVal.setText(GL1461);
					textAreaVal.getElement().setAttribute("alt",GL1461);
					textAreaVal.getElement().setAttribute("title",GL1461);
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

	}
	
	@Override
	public void onUnload() {
		setCollectionDo(null);
		
		setLabelsAndIds();
		
		showHideScrollPanel(false);
		
		toClear= true;
		
		//htmlAssignmentsListContainer.clear();
		htmlClasspagesListContainer.clear();
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
		
		remainderLbl.setText(GL1889);
		remainderLbl.getElement().setId("lblRemainderLbl");
		remainderLbl.getElement().setAttribute("alt",GL1889);
		remainderLbl.getElement().setAttribute("title",GL1889);
		
		lblAssignCollectionPrivate.setText(GL0112);
		lblAssignCollectionPrivate.getElement().setId("lblAssignCollectionPrivate");
		lblAssignCollectionPrivate.getElement().setAttribute("alt",GL0112);
		lblAssignCollectionPrivate.getElement().setAttribute("title",GL0112);
		
		lblAssignCollectionPrivate.setVisible(false);
		
		lblAssignCollectionTitle.setText(GL0101);
		lblAssignCollectionTitle.getElement().setId("lblAssignCollectionTitle");
		lblAssignCollectionTitle.getElement().setAttribute("alt",GL0101);
		lblAssignCollectionTitle.getElement().setAttribute("title",GL0101);
		
		lblClasspages.setText(GL0102);
		lblClasspages.getElement().setId("lblClasspages");
		lblClasspages.getElement().setAttribute("alt",GL0102);
		lblClasspages.getElement().setAttribute("title",GL0102);
		//lblAssignments.setText(GL0103);
		
		btnAssign.setText(GL0104);
		btnAssign.getElement().setAttribute("alt",GL0104);
		btnAssign.getElement().setAttribute("title",GL0104);
		
		lblClasspagePlaceHolder.setText(GL0105);
		lblClasspagePlaceHolder.getElement().setId("lblClasspagePlaceHolder");
		lblClasspagePlaceHolder.getElement().setAttribute("alt",GL0105);
		lblClasspagePlaceHolder.getElement().setAttribute("title",GL0105);
		//lblAssignmentsPlaceHolder.setText(GL0105);
		
		lblNoClasspages.setText(GL0106);
		lblNoClasspages.getElement().setId("lblNoClasspages");
		lblNoClasspages.getElement().setAttribute("alt",GL0106);
		lblNoClasspages.getElement().setAttribute("title",GL0106);
//		htmlGoto.setHTML(MessageProperties.GL0107);
//		ancTeach.setText(MessageProperties.GL0108);
		htmlTab.setHTML(GL0109);
		htmlTab.getElement().setId("htmlTab");
		htmlTab.getElement().setAttribute("alt",GL0109);
		htmlTab.getElement().setAttribute("title",GL0109);
		//ancTeach.setHref("#"+PlaceTokens.TEACH);
		
		//Ids
		btnAssign.getElement().setAttribute("id", "btnAssign");
		btnAssign.setStyleName(res.css().disableAssignButon());
		btnAssign.setText(GL0104);
		btnAssign.getElement().setAttribute("alt",GL0104);
		btnAssign.getElement().setAttribute("title",GL0104);
		btnAssign.setEnabled(false);
		btnAssign.setStyleName(CollectionAssignCBundle.INSTANCE.css().disableAssignButon());

		panelLoading.getElement().setId("pnlPanelLoading");
		panelNoClasspages.getElement().setId("pnlPanelNoClasspages");
		htmlGoto.getElement().setId("htmlGoto");
		ancTeach.getElement().setId("htmlAncTeach");
		htmlEvenPanelContainer.getElement().setId("epnlHtmlEvenPanelContainer");
		lblClasspagesArrow.getElement().setId("lblClasspagesArrow");
		spanelClasspagesPanel.getElement().setId("sbSpanelClasspagesPanel");
		htmlClasspagesListContainer.getElement().setId("pnlHtmlClasspagesListContainer");
		duedateContainer.getElement().setId("pnlDuedateContainer");
		directionsErrorLength.getElement().setId("lblDirectionsErrorLength");
		directionsErrorLbl.getElement().setId("lblDirectionsErrorLbl");
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
					btnAssign.setText(GL1172);
					btnAssign.getElement().setAttribute("alt",GL1172);
					btnAssign.getElement().setAttribute("title",GL1172);
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
					if(textAreaVal.getText().equalsIgnoreCase(GL1389))
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
					
					
					AppClientFactory.getInjector().getClasspageService().createClassPageItem(classpageId, collectionDo.getGooruOid() ,dueDateVal, directionsVal, new SimpleAsyncCallback<ClasspageItemDo>() {
						@Override
						public void onSuccess(ClasspageItemDo result) {
							
							if(!AppClientFactory.getCurrentPlaceToken().equals(PlaceTokens.SHELF)) {
								AppClientFactory.fireEvent(new RefreshCollectionInShelfListEvent(collectionDo, RefreshType.INSERT));
							}
							
							btnAssign.setText(GL0104);
							btnAssign.getElement().setAttribute("alt",GL0104);
							btnAssign.getElement().setAttribute("title",GL0104);
							SuccessPopupVc successPopupVc = new SuccessPopupVc(classpageId, collectionDo.getTitle(), lblClasspagePlaceHolder.getText()) {
								
								@Override
								public void closePoup() {
						
									
									lblClasspagePlaceHolder.setText(GL0105);
									lblClasspagePlaceHolder.getElement().setAttribute("alt",GL0105);
									lblClasspagePlaceHolder.getElement().setAttribute("title",GL0105);
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
									btnAssign.setText(GL0104);
									btnAssign.getElement().setAttribute("alt",GL0104);
									btnAssign.getElement().setAttribute("title",GL0104);
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
					});
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


	
	
}
