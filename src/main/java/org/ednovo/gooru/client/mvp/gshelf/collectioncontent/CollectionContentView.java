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
package org.ednovo.gooru.client.mvp.gshelf.collectioncontent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.application.shared.i18n.MessageProperties;
import org.ednovo.gooru.application.shared.model.code.CodeDo;
import org.ednovo.gooru.application.shared.model.content.CollectionDo;
import org.ednovo.gooru.application.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.application.shared.model.content.CollectionQuestionItemDo;
import org.ednovo.gooru.application.shared.model.folder.FolderDo;
import org.ednovo.gooru.client.mvp.gshelf.util.ContentResourceWidgetWithMove;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;

import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.EditQuestionPopupVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.EditResourcePopupVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.EditUserOwnResourcePopupVc;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item.UpdateQuestionImageView;
import org.ednovo.gooru.client.mvp.shelf.event.GetEditPageHeightEvent;
import org.ednovo.gooru.client.mvp.shelf.event.InsertCollectionItemInAddResourceEvent;

import org.ednovo.gooru.client.mvp.shelf.event.RefreshType;
import org.ednovo.gooru.client.uc.ConfirmationPopupVc;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CollectionContentView extends BaseViewWithHandlers<CollectionContentUiHandlers> implements IsCollectionContentView  {

	private static CollectionContentViewUiBinder uiBinder = GWT
			.create(CollectionContentViewUiBinder.class);

	interface CollectionContentViewUiBinder extends
			UiBinder<Widget, CollectionContentView> {
	}

	@UiField HTMLPanel pnlContentContainer;
	@UiField VerticalPanel pnlReosurceList;
	@UiField Button btnAddResources, btnAddQuestions;
	@UiField Anchor ancAddResource, ancAddQuestion;
	@UiField InlineLabel lblSpanOr;

	CollectionContentPresenter collectionContentPresenter;
	CollectionDo listOfContent=null;

	private EditQuestionPopupWidget editQuestionPopupWidget;

	private EditResourcePopupVc editResoruce;

	private EditUserOwnResourcePopupVc ownResourcePopupVc;

	ConfirmationPopupVc deleteConfirmationPopupVc = null;

	private HandlerRegistration handlerRegistration=null;

	private static MessageProperties i18n = GWT.create(MessageProperties.class);

	int index=0;
	String type;
	private String clickType;
	String title,description,category,thumbnailUrl;

	private static final String MESSAGE_HEADER = i18n.GL0748();
	private static final String MESSAGE_CONTENT = i18n.GL0891();

	public CollectionContentView() {
		setWidget(uiBinder.createAndBindUi(this));
		pnlContentContainer.getElement().setId("resourceEdit");

		StringUtil.setAttributes(btnAddResources.getElement(), "btnAddResources", i18n.GL_SPL_PLUS() +" "+ i18n.GL0174(), i18n.GL_SPL_PLUS() +" "+ i18n.GL0174());
		StringUtil.setAttributes(btnAddQuestions.getElement(), "btnAddQuestions", i18n.GL_SPL_PLUS() +" "+ i18n.GL1042(), i18n.GL_SPL_PLUS() +" "+ i18n.GL1042());

		lblSpanOr.setText(i18n.GL0209().toLowerCase());

		//Adding Click Handlers.
		btnAddResources.addClickHandler(new NewResourceClickEvent());
		btnAddQuestions.addClickHandler(new NewQuestionClickEvent());

		ancAddResource.addClickHandler(new NewResourceClickEvent());
		ancAddQuestion.addClickHandler(new NewQuestionClickEvent());

	}

	@Override
	public void setData(CollectionDo listOfContent,FolderDo folderDo, RefreshType type){
		this.listOfContent = listOfContent;

		if(listOfContent.getCollectionItems().size()>0){
			index=0;
			for (CollectionItemDo collectionItem : listOfContent.getCollectionItems()) {
				setDisplayResourceItem(collectionItem, type, index);
				index++;
			}
			setLastWidgetArrowVisiblity(false);
		}else{
			AppClientFactory.printInfoLogger("TODO -- No Resources...");
			pnlReosurceList.clear();
			pnlReosurceList.add(new Label(i18n.GL0854()));
		}
	}

	@Override
	public void setDisplayResourceItem(CollectionItemDo collectionItem,RefreshType type, int index){
		int tmpIndex = index;
		Window.enableScrolling(true);

		if (tmpIndex ==-1){
			index = listOfContent != null && listOfContent.getCollectionItems() != null ? listOfContent.getCollectionItems().size() : 0;
		}
		if (index == 0){
			pnlReosurceList.clear();
		}

		if (type.equals(RefreshType.INSERT)){
			ContentResourceWidgetWithMove widgetMove=new ContentResourceWidgetWithMove(index,collectionItem) {
				@Override
				public void moveWidgetPosition(String movingPosition,String currentWidgetPosition, boolean isDownArrow, String moveId) {
					int movingIndex= Integer.parseInt(movingPosition);
					if(pnlReosurceList.getWidgetCount()>=movingIndex){
						//Based on the position it will insert the widget in the vertical panel
						String itemSequence=pnlReosurceList.getWidget(movingIndex-1).getElement().getAttribute("itemSequence");
						getUiHandlers().reorderWidgetPositions(moveId, Integer.parseInt(itemSequence));
						if(!isDownArrow){
							movingIndex= (movingIndex-1);
							int currentIndex= Integer.parseInt(currentWidgetPosition);
							pnlReosurceList.insert(pnlReosurceList.getWidget(currentIndex), movingIndex);
						}else{
							int currentIndex= Integer.parseInt(currentWidgetPosition);
							pnlReosurceList.insert(pnlReosurceList.getWidget(currentIndex), movingIndex);
						}
					}
				}
				@Override
				public void updateNarration(CollectionItemDo collectionItem,String narration) {
					getUiHandlers().updateNarrationItem(collectionItem, narration);
				}
				@Override
				public void editResource(final CollectionItemDo collectionItem) {

					AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99,false));
					if (collectionItem.getResource().getCategory().equalsIgnoreCase("Question")) {
						getUiHandlers().showEditQuestionResourcePopup(collectionItem);
					} else if(collectionItem.getResource().getResourceType().getName().equals("resource/url") || collectionItem.getResource().getResourceType().getName().equals("video/youtube")
							|| collectionItem.getResource().getResourceType().getName().equals("vimeo/video")){
						editResoruce = new EditResourcePopupVc(collectionItem) {

						@Override
						public void updateResource(CollectionItemDo collectionItemDo,List<String> tagList) {
							getUiHandlers().updateResourceInfo(collectionItemDo,tagList);
						}

						@Override
						public void resourceImageUpload() {
							getUiHandlers().imageEditResourceUpload();
						}

						@Override
						public void onSelection(
								SelectionEvent<Suggestion> event) {
							super.onSelection(event);

						}

						@Override
						public void browseStandardsInfo(boolean val,boolean userResource) {
							getUiHandlers().getBrowseStandardsInfo(val,userResource);
						}

						@Override
						public void closeStandardsPopup() {
							getUiHandlers().closeBrowseStandardsPopup();
						}
						};
					}

					else {
						MixpanelUtil.Resource_Action_Edit_Info();
						ownResourcePopupVc = new EditUserOwnResourcePopupVc(collectionItem) {
							@Override
							public void resourceImageUpload() {
								getUiHandlers().imageEditUserOwnResourceUpload();
							}

							@Override
							public void updateUserOwnResource(String resourceFilePath,String resMediaFileName,String resOriginalFileName,String titleStr, String desc,String categoryStr,String thumbnailUrlStr,CollectionItemDo collectionItemDo, List<String> tagList) {
								title=titleStr;
								description = desc;
								category = categoryStr;
								thumbnailUrl = thumbnailUrlStr;
								JSONObject jsonObject = setEditUserResourceJsonObject(resOriginalFileName,resMediaFileName, title, desc, category, thumbnailUrlStr,collectionItemDo,tagList);
								getUiHandlers().editUserOwnResource(jsonObject.toString(),collectionItemDo.getCollectionItemId());
							}
							@Override
							public void browseStandardsInfo(boolean val, boolean userResource) {
								getUiHandlers().getBrowseStandardsInfo(val,userResource);
							}
							@Override
							public void closeStandardsPopup() {
								getUiHandlers().closeBrowseStandardsPopup();
							}
							@Override
							public void onSelection(SelectionEvent<Suggestion> event) {
								super.onSelection(event);
							}
						};
					}

				}
			};
			widgetMove.setPresenter(collectionContentPresenter);
			widgetMove.getElement().setAttribute("itemSequence", collectionItem.getItemSequence()+"");
			pnlReosurceList.insert(widgetMove, index);
		}else{
			AppClientFactory.printInfoLogger("collectionItem.getItemSequence() : "+collectionItem.getItemSequence());
			pnlReosurceList.remove(collectionItem.getItemSequence() - 1);
			AppClientFactory.printInfoLogger("1");
			listOfContent.getCollectionItems().remove(collectionItem.getItemSequence()-1);
			AppClientFactory.printInfoLogger("2");
			listOfContent.getCollectionItems().set((collectionItem.getItemSequence()-1), collectionItem);
			AppClientFactory.printInfoLogger("3");
			setDisplayResourceItem(collectionItem, RefreshType.INSERT, (collectionItem.getItemSequence()-1));
			AppClientFactory.printInfoLogger("4");
		}

		if (tmpIndex ==-1){
			setLastWidgetArrowVisiblity(false);
			resetWidgetPositions();
		}
	}

	/**
	 * On pagination it will enable the previous widget down arrow for move functionality
	 * @param isVisible
	 */
	public void setLastWidgetArrowVisiblity(boolean isVisible){
		ContentResourceWidgetWithMove lastwidget=(ContentResourceWidgetWithMove) pnlReosurceList.getWidget(pnlReosurceList.getWidgetCount()-1);
		lastwidget.getDownArrow().setVisible(isVisible);
	}
	/**
	 * This method is used to reset the widget positions with default text.
	 */
	@Override
	public void resetWidgetPositions(){
		Iterator<Widget> widgets=pnlReosurceList.iterator();
		int index=0;
		while (widgets.hasNext()){
			Widget widget=widgets.next();
			if(widget instanceof ContentResourceWidgetWithMove){
				ContentResourceWidgetWithMove contentWidgetWithMove=(ContentResourceWidgetWithMove) widget;
				contentWidgetWithMove.getElement().setAttribute("itemSequence",Integer.toString((index+1)));
				contentWidgetWithMove.getItemSequenceLabel().setText(Integer.toString((index+1)));
				contentWidgetWithMove.getTextBox().setText((index+1)+"");
				contentWidgetWithMove.getTextBox().getElement().setAttribute("index",index+"");
				if(index==0){
					//If this is the first widget we are hiding the up arrow
					contentWidgetWithMove.getTopArrow().setVisible(false);
				}else if(index==(pnlReosurceList.getWidgetCount()-1)){
					//If this the last widget hiding the down arrow
					contentWidgetWithMove.getDownArrow().setVisible(false);
				}else{
					contentWidgetWithMove.getTopArrow().setVisible(true);
					contentWidgetWithMove.getDownArrow().setVisible(true);
				}
				index++;
			}
		}
	}

	@Override
	public void setCollectionContentPresenter(CollectionContentPresenter collectionContentPresenter){
		this.collectionContentPresenter=collectionContentPresenter;
	}


	/*
	 * To handle click event for New Resource
	 */
	private class NewResourceClickEvent implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			MixpanelUtil.Click_On_AddResource();
			clickType = "Url";
			if (listOfContent.getCollectionItems().size() >= 25) {

			} else {
				MixpanelUtil.Click_Add_NewResource();
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				displayNewResourcePopup();
			}
		}
	}

	/*
	 * To handle click event for New Resource
	 */
	private class NewQuestionClickEvent implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			try{
				MixpanelUtil.Click_On_AddQuestion();
				clickType = "Question";
				if (listOfContent.getCollectionItems().size() >= 25) {

				} else {
					Window.enableScrolling(false);
					AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
					displayNewResourcePopup();
				}
			}catch(Exception e){
				AppClientFactory.printSevereLogger(e.getMessage());
			}
		}

	}

	@Override
	public void displayNewResourcePopup() {
		getUiHandlers().addResourcePopup(listOfContent, clickType);
	}

	@Override
	public void updateDeleteItem(String collectionItemId, int itemSequence) {
		//This method will delete the collectionItemId and reset the widget orders.
		listOfContent.getCollectionItems().remove(itemSequence);
		resetWidgetPositions();
	}

	@Override
	public void hideUpdateResourcePopup() {
		editResoruce.hide();
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	}

	@Override
	public void updateCollectionItem(CollectionItemDo collectionItem) {
		if(collectionItem != null){
			AppClientFactory.fireEvent(new InsertCollectionItemInAddResourceEvent(collectionItem, RefreshType.UPDATE));
		}
	}

	@Override
	public void OnBrowseStandardsClickEvent(Button addStandardsBtn) {
		// TODO Auto-generated method stub
		if(handlerRegistration!=null){
			handlerRegistration.removeHandler();
		}
		handlerRegistration=addStandardsBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().addUpdatedBrowseStandards();
			}
		});
	}

	@Override
	public void setUpdatedStandardsCode(String setStandardsVal, Integer codeId,String setStandardDesc, boolean value, boolean userResource) {
		if(value == false){
			if(userResource){
				ownResourcePopupVc.setUpdatedBrowseStandardsVal(setStandardsVal,codeId,setStandardDesc);
			}else{
				editResoruce.setUpdatedBrowseStandardsVal(setStandardsVal,codeId,setStandardDesc);
			}

		}else{
			editQuestionPopupWidget.setUpdatedBrowseStandardsVal(setStandardsVal,codeId,setStandardDesc);
		}

	}

	private JSONObject setEditUserResourceJsonObject(String originalFilename,String mediaFileName, String editedTitle, String editedDescription, String editedCategory,String editedThumbnailUrl, CollectionItemDo collectionItemDo, List<String> tagList) {
		JSONObject file = new JSONObject();
		 if(originalFilename!=null && mediaFileName!=null){
			 file.put("filename", new JSONString(originalFilename));
			 file.put("mediaFilename", new JSONString(mediaFileName));
		 }


		JSONObject attach = new JSONObject();
        attach.put("title", new JSONString(editedTitle));
        attach.put("description", new JSONString(editedDescription));
        JSONObject resourceFormat = new JSONObject();
        resourceFormat.put("value", new JSONString(editedCategory));
        attach.put("resourceFormat", resourceFormat);
        if(editedThumbnailUrl!=null){
        	 attach.put("thumbnail", new JSONString(editedThumbnailUrl));
        }
        if(originalFilename!=null && mediaFileName!=null){
        	 attach.put("attach", file);

        }

        List<CodeDo> codeDoList = new ArrayList<CodeDo>(collectionItemDo.getResource().getTaxonomySet());

        JSONArray standardsJsonArray = new JSONArray();
        JSONArray momentsOfLearningArrValue = new JSONArray();
        JSONArray educatUseArrValue = new JSONArray();
        JSONArray tagsArrValue = new JSONArray();

        for(int i=0;i<codeDoList.size();i++){
        	JSONObject code = new JSONObject();
        	code.put("code",new JSONString(codeDoList.get(i).getCode()));
        	code.put("codeId",new JSONNumber(codeDoList.get(i).getCodeId()));
        	standardsJsonArray.set(i,code);
        }
        attach.put("taxonomySet", standardsJsonArray);

        for(int i=0;i<collectionItemDo.getResource().getMomentsOfLearning().size();i++){
        	JSONObject momentsOfLearningJsonObj = new JSONObject();
        	momentsOfLearningJsonObj.put("selected",JSONBoolean.getInstance(collectionItemDo.getResource().getMomentsOfLearning().get(i).isSelected()));
        	momentsOfLearningJsonObj.put("value",new JSONString(collectionItemDo.getResource().getMomentsOfLearning().get(i).getValue()));
            momentsOfLearningArrValue.set(i, momentsOfLearningJsonObj);
        }
        attach.put("momentsOfLearning", momentsOfLearningArrValue);

        for(int i=0;i<collectionItemDo.getResource().getEducationalUse().size();i++){
        	JSONObject educatUseJsonObj = new JSONObject();
        	educatUseJsonObj.put("selected",JSONBoolean.getInstance(collectionItemDo.getResource().getEducationalUse().get(i).isSelected()));
        	educatUseJsonObj.put("value", new JSONString(collectionItemDo.getResource().getEducationalUse().get(i).getValue()));
        	educatUseArrValue.set(i, educatUseJsonObj);
        }
        attach.put("educationalUse", educatUseArrValue);

        for(int i=0;i<tagList.size();i++){
        	tagsArrValue.set(i, new JSONString(tagList.get(i)));
        }

        JSONObject resource = new JSONObject();
        resource.put("resourceTags",tagsArrValue);
        resource.put("resource", attach);

		return resource;
	}

	@Override
	public void hideUpdateOwnResourcePopup() {
		ownResourcePopupVc.hide();
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	}

	public class EditQuestionPopupWidget extends EditQuestionPopupVc {
		private String collectionItemId;

		public EditQuestionPopupWidget(CollectionItemDo collectionItemDo) {
			super(collectionItemDo);
			this.collectionItemId = collectionItemDo.getCollectionItemId();
			AppClientFactory.fireEvent(new GetEditPageHeightEvent(this, false));
		}

		public void show() {
			super.show();
			this.center();
		}

		@Override
		public void updateQuestionResource(String collectionItemId,
				CollectionQuestionItemDo collectionQuestionItemDo) {

			if (editQuestionPopupWidget.getQuestionImageContainer()
					.getElement().hasChildNodes()) {
				UpdateQuestionImageView updateQuestionImage = (UpdateQuestionImageView) editQuestionPopupWidget
						.getQuestionImageContainer().getWidget(0);
				String thumbnailUrl = updateQuestionImage
						.getThumbnailImageUrl();
				if (thumbnailUrl != null) {
					getUiHandlers().updateQuestionResource(collectionItemId,
							collectionQuestionItemDo,
							"asset-question_" + thumbnailUrl);

				} else {
					getUiHandlers().updateQuestionResource(collectionItemId,
							collectionQuestionItemDo, null);
				}

			} else {
				getUiHandlers().updateQuestionResource(collectionItemId,
						collectionQuestionItemDo, null);
			}

		}

		@Override
		public void callBrowseStandardsInfo(boolean val,boolean userResource) {
			getUiHandlers().getBrowseStandardsInfo(val,userResource);
		}

		public void setUpdatedBrowseStandardsVal(String setStandardsVal,Integer codeId, String setStandardDesc) {
			super.setUpdatedBrowseStandards(setStandardsVal,codeId,setStandardDesc);

		}

		@Override
		public void closeBrowseStandardsPopup() {
			getUiHandlers().closeBrowseStandardsPopup();
		}

	}
}
