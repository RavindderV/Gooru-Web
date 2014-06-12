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
package org.ednovo.gooru.client.mvp.shelf.collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.shelf.ShelfCBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc.SuccessPopupViewVc;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionAssignShareEvent;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionEditShareEvent;
import org.ednovo.gooru.client.mvp.shelf.event.CollectionEditShareHandler;
import org.ednovo.gooru.client.mvp.shelf.event.EmbedEnableEvent;
import org.ednovo.gooru.client.mvp.socialshare.SocialShareLinksView;
import org.ednovo.gooru.client.mvp.socialshare.SocialShareView;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.ShareViewUc;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.model.content.ClassPageCollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.social.SocialShareDo;
import org.ednovo.gooru.shared.model.user.SettingDo;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
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
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class CollectionShareTabVc extends Composite implements MessageProperties{

	private static CollectionShareTabVcUiBinder uiBinder = GWT
			.create(CollectionShareTabVcUiBinder.class);
	
	@UiField
	HTMLEventPanel publicShareFloPanel;

	@UiField
	HTMLEventPanel privateShareFloPanel,finalTeacherTipLabelContainer;

	@UiField
	HTMLEventPanel linkShareFloPanel;

	@UiField
	FlowPanel shareLinkFloPanel,mainShareContainer;

	@UiField
	FlowPanel socialShareLinksViewContainer;
	
	@UiField
	HTMLPanel contentpanel, loadingImageLabel,textAreaContianer;
	
	@UiField TextArea teacherTipTextarea;
	
	@UiField Button addTeacherTip, cancelTeacherTip;
	
	@UiField HTMLPanel rbPublicPanel,publishedPanel;
	
	@UiField Button rbPublic;
	
	@UiField Label lblPublishPending,lblPublish;

	private FlowPanel publicFocPanel;

	private FlowPanel privateFocPanel;

	private FlowPanel linkFocPanel;

	private SimpleAsyncCallback<Map<String, String>> shareUrlGenerationAsyncCallback;

	private SimpleAsyncCallback<Map<String, String>> shareUrlWithGenerationAsyncCallback;
	
	private CollectionDo collection;
	
	private CollectionDo collectionDo;

	private SocialShareDo shareDo;

	private ShareViewUc shareViewPublicUc, shareViewShareableUc, shareViewPrivateUc;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();

	private HTMLPanel /*rbPublicPanel, rbPublic, */rbPrivatePanel, rbPrivate, rbShareablePanel, rbShareable,shareViaText;
	
	
	@UiField Label visibilityText,visibilityOptiontext,userTeacherTipText,simplePencilPanel,errorLabelForTeacherTip,shareCollectiontext,visibilityTextTeacherTip,visibilityOptiontextTeacherTip;
	
	private String rawUrl, embedLink;
	
	private boolean isSharable;
	
	private static final String GOORU_UID = "gooruuid";
	
	private static String CONFIRM_MESSAGE = GL1490+GL_SPL_EXCLAMATION;
	
	private CollectionShareAlertPopup collectionShareAlertPopup;
	
	private CollectionConfirmationPopup collectionConfirmationPopup;
	
	public SocialShareLinksView socialShareLinksView = null;
	
	private boolean isShowDescPencil=true;
	
//	List<ClassPageCollectionDo> classPageCollectionDo = null;
	
	interface CollectionShareTabVcUiBinder extends
			UiBinder<Widget, CollectionShareTabVc> {
	}

	/**
	 * Class constructor , set collection object and collection shorten url
	 * 
	 * @param collection
	 *            instance of {@link CollectionDo}
	 */
	public CollectionShareTabVc(final CollectionDo collection) {
		this.collection = collection;
		initWidget(uiBinder.createAndBindUi(this));
		shareViaText = new HTMLPanel("");
		visibilityText.setText(GL0842);
		visibilityTextTeacherTip.setText(GL1658);
		visibilityOptiontext.setText(GL0843);
		
		visibilityOptiontextTeacherTip.setText(GL1659);
		

		
		//teacherTipTextarea.addKeyUpHandler(new OnKeyUpHandler());
		
//here
		setDefaults(collection.getKeyPoints());
		simplePencilPanel.addClickHandler(new OpenCollectionEditDescription());
		userTeacherTipText.addClickHandler(new OpenCollectionEditDescription());
		finalTeacherTipLabelContainer
		.addMouseOverHandler(new OnCollectionDescriptionClick());
		finalTeacherTipLabelContainer
		.addMouseOutHandler(new OnCollectionDescriptionOut());
		
		shareCollectiontext.setText(GL0545.toUpperCase());
		shareViaText.getElement().setInnerHTML(GL0638);
		//GL0638
		socialShareLinksView = new SocialShareLinksView();
		this.setData(collection);
		
		if (collection.getSharing().equalsIgnoreCase("private")) {
			shareLinkFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			socialShareLinksView.getshareLinkTxtBox().addStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
		}
		
		addTeacherTip.addBlurHandler(new BlurHandler() {
		
		@Override
		public void onBlur(BlurEvent event) {
			if(teacherTipTextarea.getText().length()>0)
			{
			errorLabelForTeacherTip.setVisible(false);
			}
			
			}
		});
		
		teacherTipTextarea.addKeyUpHandler(new DirectionsKeyUpHandler());
		teacherTipTextarea.getElement().setAttribute("maxlength", "500");
		
		teacherTipTextarea.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				String directionText=teacherTipTextarea.getText().trim();
				if(directionText.equalsIgnoreCase(GL1709)){
					teacherTipTextarea.setText("");
				}
				teacherTipTextarea.getElement().getStyle().setColor("black");
			}
		});
		
		teacherTipTextarea.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(teacherTipTextarea.getText().length() > 501)
				{
					teacherTipTextarea.cancelKey();
				}			
			}
		});
		teacherTipTextarea.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(teacherTipTextarea.getText().length() == 0)
				{
					teacherTipTextarea.setText(GL1709);
					teacherTipTextarea.getElement().getStyle().setColor("#999");
				}
				else
				{
					Map<String, String> parms = new HashMap<String, String>();
					parms.put("text", teacherTipTextarea.getText());
					AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean value) {
							errorLabelForTeacherTip.setText("");
							SetStyleForProfanity.SetStyleForProfanityForTextArea(teacherTipTextarea, errorLabelForTeacherTip, value);
							errorLabelForTeacherTip
							.addStyleName("titleAlertMessageActive");
					errorLabelForTeacherTip
							.removeStyleName("titleAlertMessageDeActive");
								
						}
					});
				}
		

			}
		});
		

//		rbPublic = new Button("Publish");
		rbShareable = new HTMLPanel("");
		rbPrivate = new HTMLPanel("");
//		rbPublicPanel = new HTMLPanel("");
		rbShareablePanel = new HTMLPanel("");
		rbPrivatePanel = new HTMLPanel("");
//		rbPublicPanel.add(rbPublic);
		rbPrivatePanel.add(rbPrivate);
		rbShareablePanel.add(rbShareable);
//		rbPublicPanel.setStyleName(ShelfCBundle.INSTANCE.css().shareVisibilityRadioBtnStyle());
		rbPrivatePanel.setStyleName(ShelfCBundle.INSTANCE.css().shareVisibilityRadioBtnStyle());
		rbShareablePanel.setStyleName(ShelfCBundle.INSTANCE.css().shareVisibilityRadioBtnStyle());
		
		publicFocPanel = new FlowPanel();
		socialShareLinksView.getshareLinkTxtBox().setReadOnly(true);
		shareViewPublicUc = new ShareViewUc(GL0329, GL0330);
		shareViewPublicUc.setTitleDescriptionStyle(52, 44);
		publicFocPanel.add(shareViewPublicUc);
//		publicFocPanel.add(rbPublicPanel);
		publicShareFloPanel.add(publicFocPanel);
		linkFocPanel = new FlowPanel();
		shareViewShareableUc = new ShareViewUc(GL0331, GL0332);
		shareViewShareableUc.setTitleDescriptionStyle(12, 44);
		linkFocPanel.add(shareViewShareableUc);
		linkFocPanel.add(rbShareablePanel);
		linkShareFloPanel.add(linkFocPanel);
		
		rbPublic.setVisible(true);
		lblPublishPending.setVisible(false);
		publishedPanel.setVisible(false);
		lblPublishPending.setText(GL1924);
		lblPublish.setText(GL1942);
		
		privateFocPanel = new FlowPanel();
		shareViewPrivateUc = new ShareViewUc(GL0333, GL0334); 
		shareViewPrivateUc.setTitleDescriptionStyle(40, 44);
		privateFocPanel.add(shareViewPrivateUc);
		privateFocPanel.add(rbPrivatePanel);
		privateShareFloPanel.add(privateFocPanel);
		
		disableAllVisiblePanels();
		
		CollectionEditShareHandler handler = new CollectionEditShareHandler() {
			@Override
			public void updateShareType(String shareType) {
//				collection.setSharing(shareType);
				updateCollectionItem(shareType);
			}
		};
		
		AppClientFactory.getEventBus().addHandler(CollectionEditShareEvent.TYPE, handler);

		if (collection.getSharing().equals("public")) {
			publicShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			privateShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			linkShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			isSharable = true;
			if(collection.getPublishStatus()!=null){
				if(collection.getPublishStatus().getValue().equalsIgnoreCase("reviewed")){
					rbPublic.setVisible(false);
					selectPrivateResource("public");
					lblPublishPending.setVisible(false);
					publishedPanel.setVisible(true);
				}
			}
		} else if (collection.getSharing().equals("private")) {
			privateShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			publicShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			linkShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			isSharable = false;
			if(collection.getPublishStatus()!=null){
				if(collection.getPublishStatus().getValue().equalsIgnoreCase("pending")){
					selectPrivateResource("pending");
					rbPublic.setVisible(false);
					lblPublishPending.setVisible(true);
					publishedPanel.setVisible(false);
				}else{
					selectPrivateResource("private");
				}
			}else{
				selectPrivateResource("private");
			}
			
		} else {
			linkShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			privateShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			publicShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			isSharable = true;
			if(collection.getPublishStatus()!=null){
				if(collection.getPublishStatus().getValue().equalsIgnoreCase("pending")){
					selectPrivateResource("pending");
					rbPublic.setVisible(false);
					lblPublishPending.setVisible(true);
					publishedPanel.setVisible(false);
				}else{
					selectPrivateResource("shareable");
				}
			}else{
				selectPrivateResource("shareable");
			}
			
		}

		setShareUrlGenerationAsyncCallback(new SimpleAsyncCallback<Map<String, String>>() {
			@Override
			public void onSuccess(Map<String, String> shortenUrl) {
				contentpanel.clear();
				setData(shortenUrl);
			}
		});

		setShareUrlWithGenerationAsyncCallback(new SimpleAsyncCallback<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> result) {
				if (result != null && result.containsKey("shortenUrl")) {
					socialShareLinksView.setEmbedBitlyLink(result.get("decodeRawUrl"));
				}
				rawUrl=result.get("rawUrl").toString();
			}
		});
		
		String params = "/#"+PlaceTokens.PREVIEW_PLAY+"&id="+this.collection.getGooruOid();
		AppClientFactory.getInjector().getSearchService().getCollectionPlayDirectLink(params, new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
			}
		});
		
		if(AppClientFactory.getLoggedInUser().getConfirmStatus()==1){
			publicShareFloPanel.addClickHandler(new OnPublicClick());
			rbPublic.addClickHandler(new OnPublicClick());
		}else{
			publicShareFloPanel.addMouseOverHandler(new MouseOverHandler() {
				
				@Override
				public void onMouseOver(MouseOverEvent event) {
					toolTipPopupPanel.clear();
					toolTipPopupPanel.setWidget(new GlobalToolTip(CONFIRM_MESSAGE));
					toolTipPopupPanel.setStyleName("");
					toolTipPopupPanel.setPopupPosition(publicShareFloPanel.getElement().getAbsoluteLeft()+105, publicShareFloPanel.getElement().getAbsoluteTop()+25);
					toolTipPopupPanel.show();
				}
			});
			
			publicShareFloPanel.addMouseOutHandler(new MouseOutHandler() {
				
				@Override
				public void onMouseOut(MouseOutEvent event) {
				toolTipPopupPanel.hide();
				}
			});
		}
		privateShareFloPanel.addClickHandler(new OnPrivateClick());
		linkShareFloPanel.addClickHandler(new OnLinkClick());
		loadingImageLabel.setVisible(true);
		mainShareContainer.setVisible(false);
		socialShareLinksViewContainer.add(socialShareLinksView);
		getUserType();
	}
	
	public class OnCollectionDescriptionClick implements MouseOverHandler {
		@Override
		public void onMouseOver(MouseOverEvent event) {
		
			simplePencilPanel.setVisible(true);
		}
	}

	public class OnCollectionDescriptionOut implements MouseOutHandler {
		@Override
		public void onMouseOut(MouseOutEvent event) {
			simplePencilPanel.setVisible(false);
		}
	}
	
	public void getUserType() {
			AppClientFactory.getInjector().getUserService().getUserProfileDetails(GOORU_UID, new AsyncCallback<SettingDo>() {
				@Override
				public void onSuccess(SettingDo result) {
					if(result.getUser().getAccountTypeId()!=null) {
						if(result.getUser().getAccountTypeId()==2) {
							publicShareFloPanel.getElement().getStyle().setDisplay(Display.NONE);
							linkShareFloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
							privateShareFloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
						} else {
							displayAllVisiblePanels();					}
					} else {
						displayAllVisiblePanels();
					}
				}
			
				@Override
				public void onFailure(Throwable caught) {
				}
		});
	}

	private void displayAllVisiblePanels() {
		publicShareFloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		linkShareFloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		privateShareFloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
	}

	private void disableAllVisiblePanels() {
		publicShareFloPanel.getElement().getStyle().setDisplay(Display.NONE);
		linkShareFloPanel.getElement().getStyle().setDisplay(Display.NONE);
		privateShareFloPanel.getElement().getStyle().setDisplay(Display.NONE);
	}

	public void setSharingUi(String shareType){
		if (shareType.equalsIgnoreCase("private")) {
			socialShareLinksView.getshareLinkTxtBox().addStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
			shareLinkFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			isSharable = false;
		} else {
			socialShareLinksView.getshareLinkTxtBox().removeStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
			shareLinkFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
			isSharable = true;
		}
	}
	private void updateCollectionItem(String shareType) {
		this.collection.setSharing(shareType);
		this.setData(collection);
		setSharingUi(shareType);
	}

	/**
	 * @author Search Team Updated sharing type , change the collection as
	 *         public
	 * 
	 */
	private class OnPublicClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			MixpanelUtil.Organize_Visibility_Public();
			clickOnPublic();
		}
	}

	/**
	 * @author Search Team Updated sharing type , change the collection as
	 *         private
	 */
	private class OnPrivateClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			MixpanelUtil.Organize_Visibility_Private();
			final String collectionId=AppClientFactory.getPlaceManager().getRequestParameter("id");
			if(privateShareFloPanel.getStyleName().contains(ShelfCBundle.INSTANCE.css().inActiveClass())) {
			AppClientFactory.getInjector().getClasspageService().getCollectionClasspageAssoc(collectionId, new SimpleAsyncCallback<List<ClassPageCollectionDo>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(List<ClassPageCollectionDo> result) {

					if(result.size()!=0){

						StringBuffer sb = new StringBuffer();
						for(int i=0;i<result.size();i++){
							if (result.size() >1){
								if(i==result.size()-1){
									sb.append(" "+GL_GRR_AND+" ");
								}else{
									if (i==0){
									}else{
										sb.append(GL_GRR_COMMA+" ");
									}
								}
							}
							sb.append(result.get(i).getTitle());
						}
						if(result.size()>1){
							sb.append(" "+GL1154+".");
						}else{
							sb.append(" "+GL0102+".");
						}
						
						 String titles=sb.toString();
						 collectionConfirmationPopup=new CollectionConfirmationPopup();
						 collectionConfirmationPopup.getClassPageNames().setText(titles);
						 collectionConfirmationPopup.getOkButtonMethod().addClickHandler(new ClickHandler() {
							 
							@Override
							public void onClick(ClickEvent event) {
								AppClientFactory.getInjector().getClasspageService().deleteCollectionAssocInAssignment(collectionId, new SimpleAsyncCallback<Void>() {

									@Override
									public void onSuccess(Void result) {
										collectionConfirmationPopup.hide();
										privateShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
										publicShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
										linkShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
										updateShare("private");
										selectPrivateResource("private");
									}
								});
							}
						});
						 
						 
					}else{
						collectionShareAlertPopup = new CollectionShareAlertPopup() {
							@Override
							public void setPublicFromAlert() {
								
							}
						};
						collectionShareAlertPopup.setPrivateMsgData();
						privateShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
						publicShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
						linkShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
						updateShare("private");
						selectPrivateResource("private");
					}
					
				      
					
				}
			});
			
			}
		}
	}
	
	/**
	 * This class is used to edit collection description
	 */
	public class OpenCollectionEditDescription implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			/*collectionDescriptionUc.switchToEdit();
			collectionDescriptionUc.setText(collectionDo.getGoals());*/

			finalTeacherTipLabelContainer.setVisible(false);
			textAreaContianer.setVisible(true);
			teacherTipTextarea.setFocus(true);
			
			/*
			 * editSelfCollectionSaveButton.getElement().getStyle().setDisplay(
			 * Display.NONE);
			 * editSelfCollectionSaveButtonCancel.getElement().getStyle
			 * ().setDisplay(Display.NONE);
			 */
			simplePencilPanel.setVisible(false);
			isShowDescPencil=false;

		}
	}

	/**
	 * @author Search Team Updated sharing type , change the collection as
	 *         anyonewithlink
	 */
	private class OnLinkClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			MixpanelUtil.Organize_Visibility_Shareable();
			if(linkShareFloPanel.getStyleName().contains(ShelfCBundle.INSTANCE.css().inActiveClass())) {
				collectionShareAlertPopup = new CollectionShareAlertPopup() {
					@Override
					public void setPublicFromAlert() {
						
					}
				};
				collectionShareAlertPopup.setShareableMsgData();
				linkShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
				privateShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
				publicShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
				updateShare("anyonewithlink");
				selectPrivateResource("shareable");
			}
		}
	}

	/**
	 * update collection sharing as sharing or public or anyonewithlink
	 * 
	 * @param share
	 */
	private void updateShare(final String share) {
		
		AppClientFactory.getInjector().getResourceService().updateCollectionMetadata(collection.getGooruOid(), null, null,
						null, share, null, null, null, null, null, new SimpleAsyncCallback<CollectionDo>() {
							@Override
							public void onSuccess(CollectionDo result) {
								final boolean isSharable;
								if (result.getSharing().equalsIgnoreCase("private")) {
									socialShareLinksView.getshareLinkTxtBox().addStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
									shareLinkFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
									isSharable = false;
								}else if (result.getSharing().equalsIgnoreCase("anyonewithlink")) {
									socialShareLinksView.getshareLinkTxtBox().removeStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
									shareLinkFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
									isSharable = true;
								}else {
									socialShareLinksView.getshareLinkTxtBox().removeStyleName(ShelfCBundle.INSTANCE.css().shareLinkBoxDisabled());
									shareLinkFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
									isSharable = true;
								}
								if(result.getSharing().equalsIgnoreCase("private") || result.getSharing().equalsIgnoreCase("anyonewithlink")){
									if(result!=null && result.getPublishStatus()!=null && result.getPublishStatus().getValue()!=null){
										if(!share.equalsIgnoreCase("public"))
										    publishedPanel.setVisible(false);
											rbPublic.setVisible(true);
											lblPublishPending.setVisible(false);
									}
								}
								
								if(result!=null && result.getPublishStatus()!=null && result.getPublishStatus().getValue()!=null){
									AppClientFactory.fireEvent(new CollectionAssignShareEvent("",result.getPublishStatus().getValue(),true));
								}
								AppClientFactory.fireEvent(new EmbedEnableEvent(isSharable));

								contentpanel.clear();
								addSocialResource(result.getSharing());
								AppClientFactory.fireEvent(new CollectionAssignShareEvent(result.getSharing(),"",false));
							}
				});
	}

	/**
	 * set the value of collection
	 * 
	 * @param collection
	 *            instance of {@link CollectionDo}
	 */
	public void setData(CollectionDo collection) {
		this.collection = collection;
	}
	
	public void setDefaults(String teacherTipLatest)
	{
		
		textAreaContianer.setVisible(false);
		finalTeacherTipLabelContainer.setVisible(true);
		if(teacherTipLatest != null && !teacherTipLatest.isEmpty())
		{
		userTeacherTipText.setText(teacherTipLatest);
		userTeacherTipText.getElement().removeAttribute("style");
		}
		else
		{
		userTeacherTipText.setText(GL1709);
		userTeacherTipText.getElement().setAttribute("style", "color:#999;");
		}

		
		
		
	}
	public void setEdittable(String teacherTipLatest)
	{
		textAreaContianer.setVisible(false);
		finalTeacherTipLabelContainer.setVisible(true);
		textAreaContianer.setVisible(false);
		finalTeacherTipLabelContainer.setVisible(true);
		if(teacherTipLatest != null && !teacherTipLatest.isEmpty())
		{
		userTeacherTipText.setText(teacherTipLatest);
		userTeacherTipText.getElement().removeAttribute("style");
		}
		else
		{
		userTeacherTipText.setText(GL1709);
		userTeacherTipText.getElement().setAttribute("style", "color:#999;");
		}
	
		
	}

	public void onReveal() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "!collection-search");
		params.put("shareType", "share");
		if (socialShareLinksView.getshareLinkTxtBox().getText().length() < 4) {
			AppClientFactory.getInjector().getSearchService().getShortenShareUrl(collection.getGooruOid(), params, getShareShortenUrlAsyncCallback());
		}

		if (!AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.PROFILE_PAGE)) {
			Map<String, String> paramsEmbed = new HashMap<String, String>();
			paramsEmbed.put("type", "!collection-search");
			paramsEmbed.put("shareType", "embed");
			AppClientFactory.getInjector().getSearchService().getShortenShareUrl(collection.getGooruOid(), paramsEmbed, getShareUrlWithGenerationAsyncCallback());
		}

	}
	
	/**
	 * set the Share of collection
	 * 
	 */
	private void addSocialResource() {
		shareDo = new SocialShareDo();
		shareDo.setBitlylink(rawUrl);
		shareDo.setRawUrl(rawUrl);
		shareDo.setTitle(collection.getTitle());
		shareDo.setShareType(collection.getSharing());
		shareDo.setDescription(collection.getGoals());
		shareDo.setThumbnailurl(collection.getThumbnailUrl());
		shareDo.setCategoryType(collection.getCategory());
		shareDo.setDecodeRawUrl(socialShareLinksView.getshareLinkTxtBox().getText());
		shareDo.setOnlyIcon(false);
		shareDo.setIsSearchShare(false);
		contentpanel.clear();
		SocialShareView socialView = new SocialShareView(shareDo);
		contentpanel.add(socialView);
	}

	/**
	 * set the Share of collection
	 * 
	 * @param shareType
	 */
	private void addSocialResource(String shareType) {
		shareDo = new SocialShareDo();
		shareDo.setBitlylink(rawUrl);
		shareDo.setRawUrl(rawUrl);
		shareDo.setTitle(collection.getTitle());
		shareDo.setShareType(shareType);
		shareDo.setDescription(collection.getGoals());
		shareDo.setThumbnailurl(collection.getThumbnailUrl());
		shareDo.setCategoryType(collection.getCategory());
		shareDo.setOnlyIcon(false);
		shareDo.setDecodeRawUrl(socialShareLinksView.getshareLinkTxtBox().getText());
		shareDo.setIsSearchShare(false);
		contentpanel.clear();
		SocialShareView socialView = new SocialShareView(shareDo);
		contentpanel.add(socialView);
	}
	
	/**
	 * the collection of the shorten url is set into link text box
	 * 
	 * @param shortenUrl
	 *            collection shorten url , used to share with others
	 */
	private void setData(Map<String, String> shortenUrl) {
		getCollectionTeacherTipInfo(collection.getGooruOid());
		socialShareLinksView.setData(shortenUrl);
		rawUrl = shortenUrl.get("rawUrl").toString();
		addSocialResource();
		loadingImageLabel.setVisible(false);
		mainShareContainer.setVisible(true);
	}
	
	public void setShareUrlGenerationAsyncCallback(
			SimpleAsyncCallback<Map<String, String>> shareShortenUrlAsyncCallback) {
		this.shareUrlGenerationAsyncCallback = shareShortenUrlAsyncCallback;
	}

	public SimpleAsyncCallback<Map<String, String>> getShareShortenUrlAsyncCallback() {
		return shareUrlGenerationAsyncCallback;
	}

	/**
	 * This method is to get the shareUrlWithGenerationAsyncCallback
	 */
	public SimpleAsyncCallback<Map<String, String>> getShareUrlWithGenerationAsyncCallback() {
		return shareUrlWithGenerationAsyncCallback;
	}

	/**
	 * This method is to set the shareUrlWithGenerationAsyncCallback
	 */
	public void setShareUrlWithGenerationAsyncCallback(
			SimpleAsyncCallback<Map<String, String>> shareUrlWithGenerationAsyncCallback) {
		this.shareUrlWithGenerationAsyncCallback = shareUrlWithGenerationAsyncCallback;
	}
	
	@UiHandler("addTeacherTip")
	public void onClickAddTeacherTip(ClickEvent clickEvent){

		if (teacherTipTextarea.getText().length()>0){
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", teacherTipTextarea.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean value) {
				
				if(!value)
				{
					updateCollectionTeacherTipInfo(collection, teacherTipTextarea.getText());
					setDefaults(teacherTipTextarea.getText());

				}
				else
				{
					errorLabelForTeacherTip.setText("");
					SetStyleForProfanity.SetStyleForProfanityForTextArea(teacherTipTextarea, errorLabelForTeacherTip, value);
					errorLabelForTeacherTip
					.addStyleName("titleAlertMessageActive");
			errorLabelForTeacherTip
					.removeStyleName("titleAlertMessageDeActive");
				}
					
			}
		});
		}
	}
	@UiHandler("cancelTeacherTip")
	public void onClickcancelTeacherTip(ClickEvent clickEvent){
		teacherTipTextarea.setText(collectionDo.getKeyPoints());
		setEdittable(collectionDo.getKeyPoints());		
	}
	
	public void updateCollectionTeacherTipInfo(CollectionDo collectionDo, String teacherTip) {
		if (teacherTip.length() > 0) {
			AppClientFactory
					.getInjector()
					.getResourceService()
					.updateCollectionInfo(collectionDo, teacherTip,
							new AsyncCallback<CollectionDo>() {

								@Override
								public void onSuccess(CollectionDo result) {
									setExistingTeacherTip(result);
									// getView().onPostCourseUpdate(result);
								}

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}
							});
		} else {
			displayErrorMsgTeacherTip();
		}

	}
	
	public void setExistingTeacherTip(CollectionDo collectionDo) {
		this.collectionDo = collectionDo;
		teacherTipTextarea.setText(collectionDo.getKeyPoints());
		if(collectionDo.getKeyPoints() != null && !collectionDo.getKeyPoints().isEmpty())
		{
			userTeacherTipText.setText(collectionDo.getKeyPoints());
			userTeacherTipText.getElement().removeAttribute("style");
		}
		else
		{
			userTeacherTipText.setText(GL1709);
			userTeacherTipText.getElement().setAttribute("style", "color:#999;");
		}
		
	}
	

	public void displayErrorMsgTeacherTip(){

		errorLabelForTeacherTip.setVisible(true);
		errorLabelForTeacherTip.setText(MessageProperties.GL1116);
		
	}
	
	public void getCollectionTeacherTipInfo(String collectionId) {

		AppClientFactory.getInjector().getResourceService().getCollectionInfoV2API(collectionId, new AsyncCallback<CollectionDo>() {

			@Override
			public void onSuccess(CollectionDo result) {
				setExistingTeacherTip(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});


	}
	
	private class DirectionsKeyUpHandler implements KeyUpHandler {
		public void onKeyUp(KeyUpEvent event) {
			errorLabelForTeacherTip.setVisible(false);
			if (teacherTipTextarea.getText().length() >=500) {
				teacherTipTextarea.setText(teacherTipTextarea.getText().trim()
						.substring(0, 500));
				errorLabelForTeacherTip.setText("");
				errorLabelForTeacherTip.setText(MessageProperties.GL0143);
				errorLabelForTeacherTip.setVisible(true);
			}
			else
			{
				errorLabelForTeacherTip.setVisible(false);
			}

		}
	}
	
	public void clickOnPublic(){
		if(publicShareFloPanel.getStyleName().contains(ShelfCBundle.INSTANCE.css().inActiveClass())) {
			collectionShareAlertPopup = new CollectionShareAlertPopup() {
				@Override
				public void setPublicFromAlert() {
					/*publicShareFloPanel.removeStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
					privateShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());
					linkShareFloPanel.addStyleName(ShelfCBundle.INSTANCE.css().inActiveClass());*/
					SuccessPopupViewVc success = new SuccessPopupViewVc() {

						@Override
						public void onClickPositiveButton(ClickEvent event) {
							rbPublic.setVisible(false);
							lblPublishPending.setVisible(true);
							this.hide();
							updateShare("public");
							selectPrivateResource("public");
							Window.enableScrolling(true);
						}
						
					};
					success.setPopupTitle(GL1921);
                    success.setDescText(GL1917+"<br>"+GL1918);
                    success.setPositiveButtonText(GL0190);
					
				}
			};
			collectionShareAlertPopup.setPublicMsgData(collection);
		}
	}
	
	private void selectPrivateResource(String visibilityType) {
		if(visibilityType.equalsIgnoreCase("public")) {
//			rbPublic.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButtonSelected());
			rbPrivate.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbShareable.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			socialShareLinksView.setIsPrivate(false);
		} else if(visibilityType.equalsIgnoreCase("private")) {
//			rbPublic.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbPrivate.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButtonSelected());
			rbShareable.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			socialShareLinksView.setIsPrivate(true);
		} else if(visibilityType.equalsIgnoreCase("pending")) {
//			rbPublic.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbPrivate.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbShareable.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			socialShareLinksView.setIsPrivate(true);
		}else {
//			rbPublic.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbPrivate.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButton());
			rbShareable.setStyleName(ShelfCBundle.INSTANCE.css().visibilityRadioButtonSelected());
			socialShareLinksView.setIsPrivate(false);
		}
	}
}
