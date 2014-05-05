package org.ednovo.gooru.client.mvp.shelf.collection.folders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.event.InvokeLoginEvent;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.client.mvp.folders.FoldersWelcomePage;
import org.ednovo.gooru.client.mvp.folders.event.RefreshFolderType;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.FolderStyleBundle;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.OpenParentFolderEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.events.RefreshFolderItemEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.item.ShelfFolderItemChildView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderDeleteView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.uc.FolderPopupUc;
import org.ednovo.gooru.client.uc.EditableLabelUc;
import org.ednovo.gooru.client.uc.HTMLEventPanel;
import org.ednovo.gooru.client.uc.tooltip.GlobalToolTip;
import org.ednovo.gooru.client.uc.tooltip.LibraryTopicCollectionToolTip;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.shared.model.folder.FolderDo;
import org.ednovo.gooru.shared.util.MessageProperties;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Search Team
 * 
 */
public class FolderItemTabView extends BaseViewWithHandlers<FolderItemTabUiHandlers> implements IsFolderItemTabView, MessageProperties {

	@UiField(provided = true)
	EditableLabelUc organizeTitleLbl;
	
	@UiField HTMLPanel mainSection, panelTitleSection;
	@UiField VerticalPanel folderContentBlock;
	@UiField Label editFolderLbl, deleteFolderLbl, folderTitleErrorLbl, editMetaLbl;
	@UiField Button newCollectionBtn, newFolderBtn;
	@UiField HTMLEventPanel editButtonEventPanel;
	@UiField FlowPanel folderContentPanel;
	@UiField Button editFolderSaveBtn,editFolderCancelBtn;
	@UiField FolderStyleBundle folderStyle;
	@UiField HTMLPanel loadingImage;
	
	@UiField
	FolderItemMetaDataUc folderItemMetaDataUc;
	
	private  List<FolderDo> folderList = null;
	
	private PopupPanel toolTipPopupPanel=new PopupPanel();
	
	private String parentId, presentFolderId;
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private boolean isRootFolder = true;
	
	private boolean isFolderType = false;
	
	private boolean isBadWord = false;
	
	private boolean isFolderPanelEmpty = false;
	
	private boolean isPaginated = false;
	
	private Integer pageNumber = 0;
	
	private String parentName = null;
	
	private String O1_LEVEL_VALUE = null, O2_LEVEL_VALUE = null, O3_LEVEL_VALUE = null;
	
	private static FolderItemTabViewUiBinder uiBinder = GWT.create(FolderItemTabViewUiBinder.class);
	
	interface FolderItemTabViewUiBinder extends UiBinder<Widget, FolderItemTabView> {
	}

	/**
	 * Class constructor
	 */
	public FolderItemTabView() {
     	setFolderTitleValidations();
		setWidget(uiBinder.createAndBindUi(this));
		setStaticMsgs();
		newFolderBtn.addClickHandler(new AddNewFolderClick());
		newFolderBtn.addMouseOverHandler(new NewFolderMouseOver());
		newFolderBtn.addMouseOutHandler(new NewFolderMouseOut());
		editFolderSaveBtn.setVisible(false);
		editFolderCancelBtn.setVisible(false);
		folderTitleErrorLbl.setVisible(false);	
		editFolderSaveBtn.setText(GL0141);
		editFolderCancelBtn.setText(GL0142);
		loadingImage.setVisible(true);
		folderContentBlock.setVisible(false);
		if (AppClientFactory.getLoggedInUser().getConfirmStatus() == 1){
			panelTitleSection.getElement().getStyle().clearPaddingTop();
		}else{
			panelTitleSection.getElement().getStyle().setPaddingTop(45, Unit.PX);
		}
		
		
	}
	
	private void setFolderTitleValidations() {
		organizeTitleLbl = new EditableLabelUc(){

			@Override
			public void checkCharacterLimit(String text) {
				if (text.length() >= 50) {
					folderTitleErrorLbl.setText(GL0143);
					folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
					folderTitleErrorLbl.setVisible(true);
				}else{
					folderTitleErrorLbl.setVisible(false);
				}
			}

			@Override
			public void showProfanityError(boolean value) {
				if(value){
					if(editFolderSaveBtn.isVisible()){
						folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
						folderTitleErrorLbl.setVisible(true);
						folderTitleErrorLbl.setText(GL0554);
					}
				}else if(!folderTitleValidations()){
					folderTitleErrorLbl.setVisible(true);	
				}
				else{
					folderTitleErrorLbl.setVisible(false);	
				}
			}

			@Override
			public void onEditDisabled(String text) {
				editButtonEventPanel.setVisible(true);
				editFolderSaveBtn.setVisible(false);
				editFolderCancelBtn.setVisible(false);
			}

		};
		organizeTitleLbl.getElement().getStyle().setCursor(Cursor.DEFAULT);
	}

	/**
	 * @function setStaticMsgs 
	 * @created_date : 04-Feb-2014
	 * @description
	 * @parm(s) : 
	 * @return : void
	 * @throws : <Mentioned if any exceptions>
	*/
	private void setStaticMsgs() {
		organizeTitleLbl.setText(GL0180);
		editFolderLbl.setText(GL1147);
		deleteFolderLbl.setText(GL1148);
		editMetaLbl.setText(GL1654);
		newCollectionBtn.setText(GL1451);
		newFolderBtn.setText(GL1450);
		folderItemMetaDataUc.setVisible(false);
	}
	
	public class AddNewFolderClick implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if(!newFolderBtn.getStyleName().contains("disabled")){
				FolderPopupUc folderPopupUc = new FolderPopupUc("", isFolderType) {
					@Override
					public void onClickPositiveButton(ClickEvent event, String folderName, String parentId, HashMap<String,String> params) {
						if(!folderName.isEmpty()) {
							getUiHandlers().createFolderInParent(folderName, parentId);
							Window.enableScrolling(true);
							this.hide();
						}
					}
				};
				folderPopupUc.setGlassEnabled(true);
				folderPopupUc.removeStyleName("gwt-PopupPanelGlass");
				if(isFolderType){
					folderPopupUc.getElement().setAttribute("style", "top:50px !important;");	
				}else{
					folderPopupUc.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - (464), Window.getScrollTop() + 233);
				}
				
				Window.enableScrolling(false);
				folderPopupUc.show();
			}
			
		}
	}
	
	
	@Override
	public void reset() {
		super.reset();
	}

	/**
	 * @function setFolderData 
	 * 
	 * @created_date : 04-Feb-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	*/
	@Override
	public void setFolderData(List<FolderDo> folderList, String folderParentName, String presentFolderId) { 
		setFolderUrlParams();
		if(O1_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&&O3_LEVEL_VALUE==null) {
			isRootFolder = true;
			editButtonEventPanel.setVisible(false);
			editFolderLbl.addStyleName("disabled");
			deleteFolderLbl.addStyleName("disabled");
		} else {
			isRootFolder = false;
			editButtonEventPanel.setVisible(true);
			editFolderLbl.removeStyleName("disabled");
			deleteFolderLbl.removeStyleName("disabled");
		}
		
		this.folderList= folderList;
		this.presentFolderId= presentFolderId;
		if(isRootFolder) {
			organizeTitleLbl.removeStyleName(folderStyle.folder());
		} else {
			organizeTitleLbl.addStyleName(folderStyle.folder());
		}
		if(folderParentName!=null) {
			organizeTitleLbl.setText(folderParentName);
		}
		mainSection.getElement().setAttribute("style", "min-height:"+(Window.getClientHeight()-100)+"px");
		
		if(folderList != null)
		{
		if(folderList.size()==0&&!isPaginated){
			if(AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL)==null){
				isFolderPanelEmpty = true;
				folderContentBlock.clear();
				isFolderType = false;
				
				mainSection.addStyleName(folderStyle.mainSection());
				mainSection.removeStyleName(folderStyle.emptyFolder());
				loadingImage.setVisible(false);
				
				Element ele = Document.get().getElementById("loadingImageLabel");
				if (ele!=null){
					ele.removeFromParent();
				}
				folderContentBlock.add(new FoldersWelcomePageToolTip());
				folderContentBlock.add(new FoldersWelcomePage());
				
			}else{
				folderContentBlock.clear();
				isFolderType = false;
				mainSection.addStyleName(folderStyle.mainSection()); 
				mainSection.addStyleName(folderStyle.emptyFolder());
			}
			
		}
		else{
			mainSection.removeStyleName(folderStyle.emptyFolder());
			if(!isPaginated) {
				folderContentBlock.clear();
			}
			for(int i = 0; i<folderList.size(); i++) {
				if(folderList.get(i).getType().equalsIgnoreCase("folder")){
					isFolderType = false;
				}
				folderContentBlock.add(new ShelfFolderItemChildView(folderList.get(i)));
			}
		}
		}
		else{
			mainSection.removeStyleName(folderStyle.emptyFolder());
			if(!isPaginated) {
				folderContentBlock.clear();
			}
			for(int i = 0; i<folderList.size(); i++) {
				if(folderList.get(i).getType().equalsIgnoreCase("folder")){
					isFolderType = false;
				}
				folderContentBlock.add(new ShelfFolderItemChildView(folderList.get(i)));
			}
		}
		loadingImage.setVisible(false);
		folderContentBlock.setVisible(true);
		if(O3_LEVEL_VALUE!=null){
			newFolderBtn.addStyleName("disabled");
		}else{
			newFolderBtn.removeStyleName("disabled");
		}
		if(folderList.size()==20) {
			isPaginated = true;
		} else {
			isPaginated = false;
		}
		setPaginatedResults();
	}
	
	public class NewFolderMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			if(newFolderBtn.getStyleName().contains("disabled")){
				toolTipPopupPanel.clear();
				toolTipPopupPanel.setWidget(new LibraryTopicCollectionToolTip(GL1178));
				toolTipPopupPanel.setStyleName("");
				toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 27);
				toolTipPopupPanel.show();
			}
		}
		
	}
	
	public class NewFolderMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}
		
	}
	
	public class OnEmptyOrganizeNewCollMouseOver implements MouseOverHandler{

		@Override
		public void onMouseOver(MouseOverEvent event) {
			toolTipPopupPanel.clear();
			toolTipPopupPanel.setWidget(new GlobalToolTip(GL1308));
			toolTipPopupPanel.setStyleName("");
			toolTipPopupPanel.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - 2, event.getRelativeElement().getAbsoluteTop() + 27);
			toolTipPopupPanel.show();
		}
		
	}
	
	public class OnEmptyOrganizeNewCollMouseOut implements MouseOutHandler{

		@Override
		public void onMouseOut(MouseOutEvent event) {
			toolTipPopupPanel.hide();
		}

		
		
	}
	
	/**
	 * @function deleteFolder
	 * 
	 * @created_date : 06-Feb-2014
	 * 
	 * @description
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	*/
	@UiHandler("deleteFolderLbl")
	public void onclickOnDeleteFolder(ClickEvent event){
		if(!isRootFolder) {
			FolderDeleteView folderDeleteView=new FolderDeleteView();
			folderDeleteView.setGlassEnabled(true);
			folderDeleteView.setStyleName("folderDelete");
			folderDeleteView.setPopupPosition(event.getRelativeElement().getAbsoluteLeft() - (630), Window.getScrollTop() + 182);
			Window.enableScrolling(false);
			folderDeleteView.show();
		}
	}
	
	@UiHandler("editFolderLbl")
	public void editFolderName(ClickEvent clickEvent){
		if(!isRootFolder) {
			editButtonEventPanel.setVisible(false);
			editFolderSaveBtn.setVisible(true);
			editFolderCancelBtn.setVisible(true);
			organizeTitleLbl.switchToEdit();
		}
	}
	
	@UiHandler("editFolderCancelBtn")
	public void clickCancelBtn(ClickEvent clickEvent){
		editButtonEventPanel.setVisible(true);
		folderTitleErrorLbl.setVisible(false);
		editFolderSaveBtn.setVisible(false);
		editFolderCancelBtn.setVisible(false);
		organizeTitleLbl.switchToCancelLabel();
		organizeTitleLbl.setText(organizeTitleLbl.getText());
		organizeTitleLbl.getElement().getStyle().clearBackgroundColor();
		organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
	}
	
	@UiHandler("editFolderSaveBtn")
	public void clickSaveBtn(ClickEvent clickEvent){
		if(folderTitleValidations()){
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", organizeTitleLbl.getTextBoxSource().getText());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result){
						if(editFolderSaveBtn.isVisible()){
							organizeTitleLbl.getElement().getStyle().setBorderColor("orange");
							folderTitleErrorLbl.setText(GL0554);
							folderTitleErrorLbl.setVisible(true);
							folderTitleErrorLbl.getElement().getStyle().setFloat(Float.RIGHT);
						}
					}else{
						editButtonEventPanel.setVisible(true);
						folderTitleErrorLbl.setVisible(false);
						editFolderSaveBtn.setVisible(false);
						editFolderCancelBtn.setVisible(false);
						presentFolderId = presentFolderId!=null ? presentFolderId : " " ;
						getUiHandlers().updateCollectionInfo(presentFolderId, organizeTitleLbl.getTextBoxSource().getText(), null);
						organizeTitleLbl.getElement().getStyle().clearBackgroundColor();
						organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
						organizeTitleLbl.switchToLabel();
					}
				}
				
			});
		}
		
	}
	
	private boolean folderTitleValidations() {
		String title=organizeTitleLbl.getTextBoxSource().getText().trim();
		if(title==null || title.equals("")){
			folderTitleErrorLbl.setText(GL0173);
			folderTitleErrorLbl.setVisible(true);
			organizeTitleLbl.getElement().setAttribute("style", "border-color:#fab03a");
			return false;
		}else{
			organizeTitleLbl.getElement().getStyle().setBorderColor("#ccc");
			return true;
		}
		
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId=parentId;
	}
	
	@Override
	public void addFolderItem(FolderDo folderDo, String parentId, HashMap<String,String> urlParams) {
		if(isFolderPanelEmpty==true) {
			folderContentBlock.clear();
			isFolderPanelEmpty=false;
		}
		folderDo.setType("folder");
		setFolderUrlParams();
		HashMap<String,String> params = new HashMap<String,String>();
		if(O3_LEVEL_VALUE!=null) {
			params.put(O3_LEVEL, O3_LEVEL_VALUE);
		}
		if(O2_LEVEL_VALUE!=null) {
			params.put(O2_LEVEL, O2_LEVEL_VALUE);
		}
		if(O1_LEVEL_VALUE!=null) {
			params.put(O1_LEVEL, O1_LEVEL_VALUE);
		}
		
		AppClientFactory.fireEvent(new RefreshFolderItemEvent(folderDo, RefreshFolderType.INSERT, urlParams));
		AppClientFactory.fireEvent(new OpenParentFolderEvent());
		if(urlParams!=null) { 	
			if((O3_LEVEL_VALUE!=null&&O3_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O3_LEVEL))&&urlParams.size()==3) || (O2_LEVEL_VALUE!=null&&O2_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O2_LEVEL))&&urlParams.size()==2) || (O1_LEVEL_VALUE!=null&&O1_LEVEL_VALUE.equalsIgnoreCase(urlParams.get(O1_LEVEL)))&&urlParams.size()==1) {
				addFolder(folderDo);
			} else if (urlParams.get(O3_LEVEL)==null&&urlParams.get(O1_LEVEL)==null&&urlParams.get(O2_LEVEL)==null&&O3_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&&O1_LEVEL_VALUE==null) {
				addFolder(folderDo);
			}
			mainSection.getElement().setAttribute("style", "min-height:"+(Window.getClientHeight()-100)+"px");
		} else {
			if((O3_LEVEL_VALUE!=null&&O3_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				addFolder(folderDo);
			} else if((O2_LEVEL_VALUE!=null&&O2_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				MixpanelUtil.mixpanelEvent("Organize_create_subsubfolder");
				addFolder(folderDo);
			} else if ((O1_LEVEL_VALUE!=null&&O1_LEVEL_VALUE.equalsIgnoreCase(parentId))) {
				MixpanelUtil.mixpanelEvent("Organize_create_subfolder");
				addFolder(folderDo);
			} else if (O1_LEVEL_VALUE==null&&O2_LEVEL_VALUE==null&O3_LEVEL_VALUE==null) {
				MixpanelUtil.mixpanelEvent("Organize_create_folder");
				addFolder(folderDo);
			}
		}
	}
	
	private void addFolder(FolderDo folderDo) {
		folderContentBlock.insert(new ShelfFolderItemChildView(folderDo), 0);
		mainSection.removeStyleName(folderStyle.emptyFolder());
	}
	
	@UiHandler("newCollectionBtn")
	public void onClickNewCollectionBtn(ClickEvent clickEvent){
		if (AppClientFactory.getLoggedInUser().getUserUid().equals(AppClientFactory.GOORU_ANONYMOUS)) {
			AppClientFactory.fireEvent(new InvokeLoginEvent());
		} else {
			setFolderUrlParams();
			Map<String, String> params = new HashMap<String, String>();
			if(O3_LEVEL_VALUE!=null) {
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
				params.put(O2_LEVEL, O2_LEVEL_VALUE);
				params.put(O3_LEVEL, O3_LEVEL_VALUE);
			} else if(O2_LEVEL_VALUE!=null) {
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
				params.put(O2_LEVEL, O2_LEVEL_VALUE);
			} else if(O1_LEVEL_VALUE!=null){
				params.put(O1_LEVEL, O1_LEVEL_VALUE);
			}
				params.put("folderId", presentFolderId);
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
				AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.COLLECTION,params);
				
			}
		}

	@Override
	public void setFolderTitle(String title) {
		organizeTitleLbl.setText(title);
	}
	
	public void setPaginatedResults() {
		Timer timer = new Timer(){
            @Override
            public void run()
            {
            	if(isPaginated) {
            		getUiHandlers().setFolderData(parentId, parentName, getPageNumber());
            	}
            }
        };
        timer.schedule(3000);
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	
	@Override
	public void setPageDetails(Integer pageNumber, String parentId, String parentName) {
		this.pageNumber = pageNumber;
		this.parentId = parentId;
		this.parentName = parentName;
	}
	
	private void setFolderUrlParams() {
		O1_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		O2_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		O3_LEVEL_VALUE = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		displayMetaDataContainer();
	}
	
	private void displayMetaDataContainer() {
		Map<String, String> map = StringUtil.splitQuery(Window.Location.getHref());
		if(map.size()>0) {
			folderItemMetaDataUc.setVisible(true);
		} else {
			folderItemMetaDataUc.setVisible(false);
		}
	}
	
	@UiHandler("editMetaLbl")
	public void editMetaData(ClickEvent event) {
		folderItemMetaDataUc.updateFolderData(presentFolderId, organizeTitleLbl.getText());
		folderItemMetaDataUc.showEditableMetaData(false);
	}

	@Override
	public void setFolderMetaData(Map<String, String> folderMetaData) {
		String ideas = folderMetaData.get("ideas")!=null?folderMetaData.get("ideas"):"";
		String questions = folderMetaData.get("questions")!=null?folderMetaData.get("questions"):"";
		String performanceTasks = folderMetaData.get("performanceTasks")!=null?folderMetaData.get("performanceTasks"):"";
		folderItemMetaDataUc.setMetaData(ideas, questions, performanceTasks);
	}
}