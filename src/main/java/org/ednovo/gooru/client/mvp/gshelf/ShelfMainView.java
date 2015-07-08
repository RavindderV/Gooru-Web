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
package org.ednovo.gooru.client.mvp.gshelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.application.client.PlaceTokens;
import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.client.gin.BaseViewWithHandlers;
import org.ednovo.gooru.application.shared.i18n.MessageProperties;
import org.ednovo.gooru.application.shared.model.content.ClassPageCollectionDo;
import org.ednovo.gooru.application.shared.model.folder.FolderDo;
import org.ednovo.gooru.client.mvp.folders.FoldersWelcomePage;
import org.ednovo.gooru.client.mvp.shelf.list.TreeMenuImages;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @fileName : ShelfView.java
 * 
 * @description :
 * 
 * 
 * @version : 5.5
 * 
 * @date: June 17, 2013
 * 
 * @Author Gooru Team
 * 
 * @Reviewer:
 */
public class ShelfMainView extends BaseViewWithHandlers<ShelfMainUiHandlers> implements IsShelfMainView {
	
	private static MessageProperties i18n = GWT.create(MessageProperties.class);
	
	@UiField HTMLPanel floderTreeContainer,gShelfMainContainer,pnlSlot,pnlNoDataContainer,pnlMainContainer;
	
	@UiField HTMLEventPanel organizeRootPnl,createNewCourse;
	
	@UiField Button btnSelectedText;
	
	@UiField Anchor lnkMyCourses,lnkMyFoldersAndCollecctions;
	
	@UiField Label organizelbl,lblCollectionTitle;
	
	@UiField static ScrollPanel collectionListScrollpanel;
	
	private static final String O1_LEVEL = "o1";
	
	private static final String O2_LEVEL = "o2";
	
	private static final String O3_LEVEL = "o3";
	
	private static final String ID = "id";

	private static final String FOLDER = "Folder";
	
	private static final String COURSE = "Course";
	private static final String UNIT = "Unit";
	private static final String LESSON = "Lesson";
	private static final String COLLECTION = "collection";
	
	private static final String UNTITLEDCOURSE = i18n.GL3347();
	private static final String UNTITLEDUNIT = i18n.GL3364();
	private static final String UNTITLEDLESSON = i18n.GL3365();
	
	private boolean isCreateCourse;
	
	private String VIEW ="view";
	
	static Integer pageNumber = 1;

	private TreeItem treeChildSelectedItem = new TreeItem();

	private TreeItem previousTreeChildSelectedItem = new TreeItem();
	
	private Integer childPageNumber = 1;
	
	int collectionItemDoSize, count;

	boolean collectionItemsNotFriendly = false;

	String selectedFolderId = "";

	List<ClassPageCollectionDo> classpageTitles = null;

	private static final List<FolderDo> SHELF_COLLECTIONS = new ArrayList<FolderDo>();
	
	List<FolderDo> folderListDoChild=new ArrayList<FolderDo>();

	private static ShelfMainViewUiBinder uiBinder = GWT
			.create(ShelfMainViewUiBinder.class);

	interface ShelfMainViewUiBinder extends UiBinder<Widget, ShelfMainView> {
	}

	private Tree shelfFolderTree = new Tree(new TreeMenuImages()) {
		@Override
		public void onBrowserEvent(Event event) {
			int eventType = DOM.eventGetType(event);
			if (!(eventType == Event.ONKEYUP || eventType == Event.ONKEYPRESS || eventType == Event.ONKEYDOWN)) {
				super.onBrowserEvent(event);
			}
		}
		public void onLoad(){
			 super.onLoad();
			 adjustTreeItemElementsStyle(shelfFolderTree);
		}
	};

	/**
	 * class constructor
	 */
	public ShelfMainView() {
		setWidget(uiBinder.createAndBindUi(this));
		setIdForFields();
		setTreeStucture();
		setCreateCourse(true);
		//setDefaultOrganizePanel();
		//organizelbl.setText(i18n.GL3285());
		lnkMyCourses.addClickHandler(new DropDownClickEvent(0));
		lnkMyFoldersAndCollecctions.addClickHandler(new DropDownClickEvent(1));
	}

	/**
	 * This inner class will handle the click event on the drop down box
	 */
	class DropDownClickEvent implements ClickHandler{
		int selectedIndex;
		DropDownClickEvent(int selectedIndex){
			this.selectedIndex=selectedIndex;
		}
		@Override
		public void onClick(ClickEvent event) {
			Anchor selected=(Anchor) event.getSource();
			btnSelectedText.setText(selected.getText());
			if(selectedIndex==0){
				enableDisableCourseButton(true);
				organizelbl.setText(i18n.GL3335());
				getUiHandlers().setListPresenterBasedOnType(COURSE);
			}else if(selectedIndex==1){
				enableDisableCourseButton(false);
			    organizelbl.setText(i18n.GL0180());
				getUiHandlers().setListPresenterBasedOnType(FOLDER);
			}
		}
	}
	
	/**
	 * To set the default organize list.
	 */
	@Override
	public void setDefaultOrganizePanel(String tabView) {
		if(treeChildSelectedItem!=null){
			if(getFolderLevel()==0) {
				organizeRootPnl.addStyleName("active");
			} else {
				organizeRootPnl.removeStyleName("active");
			}
			ShelfTreeWidget treeItemShelfTree = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
			if(treeItemShelfTree!=null){
				if(organizeRootPnl.getStyleName().contains("active")) {
					treeItemShelfTree.setActiveStyle(false);
				} else {
					treeItemShelfTree.setActiveStyle(true);
				}
			}
		}
		if(tabView==null || tabView.equals(COURSE)){
			enableDisableCourseButton(true);
			organizelbl.setText(i18n.GL3335());
			btnSelectedText.setText(i18n.GL3335());
		}else if(tabView.equals(FOLDER)){
			enableDisableCourseButton(false);
			organizelbl.setText(i18n.GL0180());
			btnSelectedText.setText(i18n.GL0180());
		}
		collectionListScrollpanel.getElement().getStyle().setMarginRight(0, Unit.PX);
		collectionListScrollpanel.getElement().getStyle().setWidth(235, Unit.PX);
		collectionListScrollpanel.getElement().getStyle().setHeight(Window.getClientHeight()-181, Unit.PX);
	}
	
	/**
	 * To set id's for Ui fields
	 */
	private void setIdForFields() {
		gShelfMainContainer.getElement().setId("gShelfMainContainer");
		btnSelectedText.getElement().setId("btnSelectedText");
		lnkMyCourses.getElement().setId("lnkMyCourses");
		lnkMyFoldersAndCollecctions.getElement().setId("lnkMyFoldersAndCollecctions");
		StringUtil.setAttributes(createNewCourse.getElement(), "createNew", "createNew", "createNew");
		StringUtil.setAttributes(organizeRootPnl.getElement(), "organizeRootPnl", "", "");
		StringUtil.setAttributes(organizelbl.getElement(), "organizelbl", "", "");
		StringUtil.setAttributes(collectionListScrollpanel.getElement(), "FoldersListScrollpanel", "", "");
	}
	private void setTreeStucture() {
		floderTreeContainer.clear();
		shelfFolderTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				organizeRootPnl.removeStyleName("active");
				ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) event.getSelectedItem().getWidget();
					
				treeChildSelectedItem = event.getSelectedItem();
				((ShelfTreeWidget) treeChildSelectedItem.getWidget()).openFolderItem();
				getUiHandlers().setBreadCrumbs(shelfTreeWidget.getUrlParams());
				setFolderActiveStatus();
				
				if(shelfTreeWidget.getCollectionDo()==null){
					String widgetType = shelfTreeWidget.getTreeWidgetType();
					getUiHandlers().setBreadCrumbs(null);
					getUiHandlers().setRightPanelData(getFolderDo(), widgetType, null);
				}
			}
		});
		floderTreeContainer.add(shelfFolderTree);
	}
	
	/**
	 * To get Level of Folder
	 * @return folderLevel 
	 */
	public int getFolderLevel() {
		int folderLevel = 0;
		String o1=AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		String id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
		if(o3!=null) {folderLevel = 3;} else if (o2!=null) {folderLevel = 2;} else if(o1!=null) {folderLevel = 1;} else if(id!=null) {folderLevel = 4;}
		return folderLevel;
	}

	/**
	 * To set the active status current selected tree item.
	 */
	public void setFolderActiveStatus() { 
		ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		if(shelfTreeWidget!=null&&shelfTreeWidget.getCollectionDo()!=null){
				if("folder".equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getType()) || COURSE.equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getType()) || UNIT.equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getType()) || LESSON.equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getType())) {
					TreeItem parent = treeChildSelectedItem.getParentItem();
					treeChildSelectedItem.getTree().setSelectedItem(parent, false);
					if(parent != null)parent.setSelected(false);
					treeChildSelectedItem.setState(treeChildSelectedItem.getState(), false);
					String type=shelfTreeWidget.getCollectionDo().getType();
					if(FOLDER.equalsIgnoreCase(type)){
						getUiHandlers().getChildFolderItems(shelfTreeWidget.getCollectionDo().getGooruOid(),type,shelfTreeWidget.getFolderOpenedStatus());
					}else{
						callChilds(shelfTreeWidget,type);
					}
					shelfTreeWidget.setFolderOpenedStatus(true);
				}else{
					getUiHandlers().setCollectionContent(shelfTreeWidget.getCollectionDo());
					shelfTreeWidget.setCollectionOpenedStatus(true);
				}
				shelfTreeWidget.setActiveStyle(true);
				ShelfTreeWidget previousshelfTreeWidget = (ShelfTreeWidget) previousTreeChildSelectedItem.getWidget();
				if(previousshelfTreeWidget==null) {
					previousTreeChildSelectedItem = treeChildSelectedItem;
				}
				if(previousshelfTreeWidget!=null && previousshelfTreeWidget.getCollectionDo()!=null &&(shelfTreeWidget.getCollectionDo().getGooruOid()!=previousshelfTreeWidget.getCollectionDo().getGooruOid())) {
					previousshelfTreeWidget.setActiveStyle(false);
				}
				if(previousshelfTreeWidget!=null && previousshelfTreeWidget.getCollectionDo()==null){
					previousshelfTreeWidget.setActiveStyle(false);
				}
			//}
		}else{
			TreeItem parent = treeChildSelectedItem.getParentItem();
			treeChildSelectedItem.getTree().setSelectedItem(parent, false);
			if(parent != null)parent.setSelected(false);
			treeChildSelectedItem.setState(treeChildSelectedItem.getState(), false);

			/*if(!"Collection".equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getCollectionType()) && !"Assessment".equalsIgnoreCase(shelfTreeWidget.getCollectionDo().getCollectionType())){
				shelfTreeWidget.setFolderOpenedStatus(true);
			}else{
				shelfTreeWidget.setCollectionOpenedStatus(true);
			}*/

			shelfTreeWidget.setActiveStyle(true);
			ShelfTreeWidget previousshelfTreeWidget = (ShelfTreeWidget) previousTreeChildSelectedItem.getWidget();
			if(previousshelfTreeWidget==null) {
				previousTreeChildSelectedItem = treeChildSelectedItem;
			}
			if(previousshelfTreeWidget!=null) {
				previousshelfTreeWidget.setActiveStyle(false);
			}
		}
		
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}

	public void callChilds(ShelfTreeWidget shelfTreeWidget,String typeVal){
		String courseId=null,unitId=null,lessonId=null;
		if(COURSE.equalsIgnoreCase(typeVal)){
			courseId=AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL,null);
		}else if(UNIT.equalsIgnoreCase(typeVal)){
			courseId=AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL,null);
			unitId=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL,null);
		}else{
		    courseId=AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL,null);
			unitId=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL,null);
			lessonId=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL,null);
		}
		getUiHandlers().getChildFolderItemsForCourse(courseId, unitId, lessonId, typeVal, shelfTreeWidget.getFolderOpenedStatus());
	}
	/* (non-Javadoc)
	 * @see com.gwtplatform.mvp.client.ViewImpl#setInSlot(java.lang.Object, com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void setInSlot(Object slot, Widget content) {
		pnlSlot.clear();
		if (content != null) {
			 if(slot==ShelfMainPresenter.RIGHT_SLOT){
				 pnlSlot.add(content);
			 }else{
				 
			}
			Window.enableScrolling(false);
		}else{

		}
	}
	/**
	 * To get the child items of particular tree widget
	 */
	@Override
	public void getChildFolderItems(List<FolderDo> folderListDo) {
		String o2 = null, o3 = null, selectedFolder = null, selectedFolderName = null, id = null;
		FolderDo folderDo = null;
		TreeItem selectedItem = null;
		ShelfTreeWidget shelfTreeWidget=null;
		ShelfTreeWidget selectedWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		if(folderListDo!=null) {
			int nextLevel = 1;
			if(selectedWidget.getLevel()==1) {
				o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 2;
			} else if (selectedWidget.getLevel()==2) { 
				o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 3;
			}else if (selectedWidget.getLevel()==3) {
				id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
				nextLevel = 4;
			}
			for(int i=0;i<folderListDo.size();i++) {
				shelfTreeWidget = new ShelfTreeWidget(folderListDo.get(i), nextLevel);
				shelfTreeWidget.setWidgetPositions(nextLevel, i, selectedWidget.getUrlParams());
				TreeItem item = new TreeItem(shelfTreeWidget);
				treeChildSelectedItem.addItem(item);
				correctStyle(item);
				if(nextLevel==2&& (o2!=null&&o2.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o2!=null) {
						selectedFolder = o2;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if(nextLevel==3&& (o3!=null&&o3.equalsIgnoreCase(folderListDo.get(i).getGooruOid()) || id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid()))) {
					if(o3!=null) {
						selectedFolder = o3;
					} else {
						selectedFolder = id;
					}
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} else if (nextLevel==4&&id!=null&&id.equalsIgnoreCase(folderListDo.get(i).getGooruOid())) {
					selectedFolder = id;
					selectedItem = item;
					selectedFolderName = folderListDo.get(i).getTitle();
					folderDo = folderListDo.get(i);
				} 
			}
		}
		if(treeChildSelectedItem.getState()) {
			treeChildSelectedItem.setState(false);
			selectedWidget.setOpenStyle(false, treeChildSelectedItem.getChildCount());
		} else {
			treeChildSelectedItem.setState(true);
			selectedWidget.setOpenStyle(true, treeChildSelectedItem.getChildCount());
		}
		//This will set the data in the right panel
				if(selectedWidget!=null){
					folderListDoChild.clear();
					int childWidgetsCount=treeChildSelectedItem.getChildCount();
					for (int i = 0; i < childWidgetsCount; i++) {
						ShelfTreeWidget widget = (ShelfTreeWidget)treeChildSelectedItem.getChild(i).getWidget();
						folderListDoChild.add(widget.getCollectionDo());
					}
					if(COURSE.equalsIgnoreCase(selectedWidget.getCollectionDo().getType()) || UNIT.equalsIgnoreCase(selectedWidget.getCollectionDo().getType())|| LESSON.equalsIgnoreCase(selectedWidget.getCollectionDo().getType())){
						getUiHandlers().setRightPanelData(selectedWidget.getCollectionDo(), selectedWidget.getCollectionDo().getType(),folderListDoChild);
					}else{
						getUiHandlers().setRightListData(folderListDoChild,((ShelfTreeWidget)treeChildSelectedItem.getWidget()).getCollectionDo());
					}
				}
		if(selectedFolder!=null&&selectedItem!=null) { 
			checkShelfRefreshStatus(selectedItem, selectedFolder);
			ShelfTreeWidget selectedWidget1 = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
			selectedWidget1.setFolderOpenedStatus(true);
		}
	}	
	
	private static void correctStyle(final UIObject uiObject) {
	      if (uiObject instanceof TreeItem) {
	         if (uiObject != null && uiObject.getElement() != null) {
	            Element element = uiObject.getElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      } else {
	         if (uiObject != null && uiObject.getElement() != null && uiObject.getElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement() != null
	               && uiObject.getElement().getParentElement().getParentElement().getStyle() != null) {
	            Element element = uiObject.getElement().getParentElement().getParentElement();
	            element.getStyle().setPadding(0, Unit.PX);
	            element.getStyle().setMarginLeft(0, Unit.PX);
	         }
	      }
 }
	/** 
	 * This method is to get the childPageNumber
	 */
	public int getChildPageNumber() {
		return childPageNumber;
	}

	@Override
	public void setChildPageNumber(Integer childPageNumber) {
		this.childPageNumber = childPageNumber;
	}
    /**
     * To set the user meta data
     */
	@Override
	public void setUserMetaData(List<FolderDo> collections, boolean clearShelfPanel) {
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		if (clearShelfPanel) {
			pageNumber = 1;
			shelfFolderTree.clear();
		}
		SHELF_COLLECTIONS.clear();
		if(collections != null) {
			SHELF_COLLECTIONS.addAll(collections);
			collectionItemDoSize = SHELF_COLLECTIONS.size();
		}
		String gooruOid = o1!=null?o1:id;
		int collectionCount=0;
		if(collections!=null){
			collectionItemDoSize = collections.size();
			for(int i=0;i<collections.size();i++){
				FolderDo floderDo=collections.get(i);
				if(!getShelffCollection(floderDo.getGooruOid())){
					ShelfTreeWidget shelfTreeWidget = new ShelfTreeWidget(floderDo, 1);
					shelfTreeWidget.setWidgetPositions(1, collectionCount, null);
					TreeItem folderItem=new TreeItem(shelfTreeWidget);
					shelfFolderTree.addItem(folderItem);
					//When page is refreshed, the folderItem previously selected will be highlighted.
					if(gooruOid!=null&&gooruOid.equalsIgnoreCase(floderDo.getGooruOid())) {
						checkShelfRefreshStatus(folderItem, floderDo.getGooruOid());
						shelfTreeWidget.setFolderOpenedStatus(true);
					}
					collectionCount++;
				}
				floderTreeContainer.clear();
				floderTreeContainer.add(shelfFolderTree);
			}
		}
	}
	
	private boolean getShelffCollection(String collectionId) {
		boolean flag = false;
		Iterator<Widget> widgets = shelfFolderTree.iterator();
		while (widgets.hasNext()) {
			Widget widget = widgets.next();
			if (widget instanceof ShelfTreeWidget && ((ShelfTreeWidget) widget).getCollectionDo().getGooruOid().equals(collectionId)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @function checkShelfRefreshStatus 
	 * @created_date : 11-Jun-2015
	 * @description
	 * @parm(s) : @param treeItem
	 * @return : void
	 * @throws : <Mentioned if any exceptions>
	*/
	
	private void checkShelfRefreshStatus(TreeItem treeItem, String parentId) {
		treeChildSelectedItem = treeItem;
		ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		shelfTreeWidget.setActiveStyle(true);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		id = id!=null?id:"";
		if(!parentId.equalsIgnoreCase(id)) {
			String type=shelfTreeWidget.getCollectionDo().getType();
			if(FOLDER.equalsIgnoreCase(type)){
				getUiHandlers().getChildFolderItems(parentId,type,false);
			}else{
				callChilds(shelfTreeWidget,type);
			}
		}else{
			getUiHandlers().setCollectionContent(shelfTreeWidget.getCollectionDo());
		}
		ShelfTreeWidget previousshelfTreeWidget = (ShelfTreeWidget) previousTreeChildSelectedItem.getWidget();
		if(previousshelfTreeWidget!=null) {
			previousshelfTreeWidget.setActiveStyle(false);
		}
		previousTreeChildSelectedItem = treeChildSelectedItem;
	}
	
	public void adjustTreeItemElementsStyle(Tree shelfTreePanel){
		int treeItemsCount=shelfTreePanel.getItemCount();
		if(shelfTreePanel!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem treeItem=shelfTreePanel.getItem(i);
				Widget shelfWidget= treeItem.getWidget();
				if(shelfWidget instanceof ShelfTreeWidget){
					adjustChildTreeItemsStyle(treeItem);
				}
				correctStyle(treeItem);
			}
		}
	}
	public void adjustChildTreeItemsStyle(TreeItem treeItem){
		int treeItemsCount=treeItem.getChildCount();
		if(treeItem!=null&&treeItemsCount>0){
			for(int i=0;i<treeItemsCount;i++){
				TreeItem childTreeItem=treeItem.getChild(i);
				Widget shelfWidget= childTreeItem.getWidget();
				if(shelfWidget instanceof ShelfTreeWidget){
					adjustChildTreeItemsStyle(childTreeItem);
				}
				correctStyle(childTreeItem);
			}
		}
	}
	
	@UiHandler("organizeRootPnl")
	public void clickOnOrganizeRootPnl(ClickEvent event) {
		organizeRootPnl.addStyleName("active");
		ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		if(shelfTreeWidget!=null&&shelfTreeWidget.getLevel()!=0) {
			shelfTreeWidget.setActiveStyle(false);
		}
		getUiHandlers().setRightListData(SHELF_COLLECTIONS,null);
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.MYCONTENT);
	}
	/**
	 * To create couse template and adding to the root tree
	 * @param event
	 */
	@UiHandler("createNewCourse")
	public void createNewCourseOrCollection(ClickEvent event) {
		if(FOLDER!=getViewType()&&isCreateCourse()){
			setCreateCourse(false);
			createNewCourse.getElement().getFirstChildElement().getStyle().setBackgroundColor("#dddddd");
			createNewCourse.getElement().getFirstChildElement().getStyle().setCursor(Cursor.DEFAULT);
			organizeRootPnl.removeStyleName("active");
			ShelfTreeWidget shelfTreeWidget = new ShelfTreeWidget(null, 1);
			shelfTreeWidget.setTreeWidgetType(COURSE);
			shelfTreeWidget.setLevel(1);
			TreeItem treeItem = new TreeItem(shelfTreeWidget);
			shelfFolderTree.insertItem(0, treeItem);
			shelfTreeWidget.getTitleLbl().setText(UNTITLEDCOURSE);
			shelfTreeWidget.getTitleFocPanel().addStyleName("course");
			getUiHandlers().setRightPanelData(getFolderDo(), COURSE,null);
			treeChildSelectedItem=treeItem;
			correctStyle(treeItem);
			floderTreeContainer.add(shelfFolderTree);
			setFolderActiveStatus();
			shelfTreeWidget.setFolderOpenedStatus(true);
			Map<String, String> params = new HashMap<String, String>();
			params.put(VIEW, getViewType());
			AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.MYCONTENT,params);
		}
	}
	/**
	 * To add new unit/lesson/collection/assessment template to the existing tree
	 */
	@Override
	public void createNewItem(String type) {
		ShelfTreeWidget selectedWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		String o2=null,id=null,o3=null;
		int nextLevel = 1;
		if(selectedWidget.getLevel()==1) {
			o2=AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
			id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
			nextLevel = 2;
		} else if (selectedWidget.getLevel()==2) { 
			o3=AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
			id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
			nextLevel = 3;
		}else if (selectedWidget.getLevel()==3) {
			id=AppClientFactory.getPlaceManager().getRequestParameter(ID);
			nextLevel = 4;
		}
		selectedWidget.setOpen(true);
		ShelfTreeWidget shelfTreeWidget = null;
		if(UNIT.equalsIgnoreCase(type)){
			shelfTreeWidget = new ShelfTreeWidget(null, 2);
			shelfTreeWidget.setTreeWidgetType(UNIT);
			shelfTreeWidget.getTitleLbl().setText(UNTITLEDUNIT);
			shelfTreeWidget.getTitleFocPanel().addStyleName("unit");
			shelfTreeWidget.setLevel(2);
		}else if(LESSON.equalsIgnoreCase(type)){
			shelfTreeWidget = new ShelfTreeWidget(null, 3);
			shelfTreeWidget.setTreeWidgetType(LESSON);
			shelfTreeWidget.getTitleLbl().setText(UNTITLEDLESSON);
			shelfTreeWidget.getTitleFocPanel().addStyleName("lesson");
			shelfTreeWidget.setLevel(3);
		}else if("Collection".equalsIgnoreCase(type)){
			shelfTreeWidget = new ShelfTreeWidget(null, 4);
			shelfTreeWidget.getTitleLbl().setText("UntitledCollection");
			shelfTreeWidget.getTitleFocPanel().addStyleName("collection");
			shelfTreeWidget.setLevel(4);
		}else if("Assessment".equalsIgnoreCase(type) || "ExternalAssessment".equalsIgnoreCase(type)){
			shelfTreeWidget = new ShelfTreeWidget(null, 4);
			shelfTreeWidget.getTitleLbl().setText("UntitledAssessment");
			shelfTreeWidget.getTitleFocPanel().addStyleName("assessment");
			shelfTreeWidget.setLevel(4);
		}
		TreeItem item = new TreeItem(shelfTreeWidget);
		treeChildSelectedItem.insertItem(0, item);
		treeChildSelectedItem.setState(true);
		
		if(!"Collection".equalsIgnoreCase(type) && !"Assessment".equalsIgnoreCase(type)){
			shelfTreeWidget.setFolderOpenedStatus(true);
		}else{
			shelfTreeWidget.setCollectionOpenedStatus(true);
		}
		correctStyle(item);
		treeChildSelectedItem=item;
		setFolderActiveStatus();
	}
	
	/**
	 * To get more collection item after scroll down,if collection is more than 20.
	 * @param event instance of ScrollEvent
	 */
	class ScrollHandlerImpl implements ScrollHandler{
		boolean isLeftScroll;
		ScrollHandlerImpl(boolean isLeftScroll){
			this.isLeftScroll=isLeftScroll;
		}
		@Override
		public void onScroll(ScrollEvent event) {
		}
	}
	
	@UiHandler("collectionListScrollpanel")
	public void onScroll(ScrollEvent event){
		executeScroll(true);
	}

	@Override
	public void executeScroll(boolean isLeftScroll){
		if(isLeftScroll){
			if(collectionListScrollpanel.getVerticalScrollPosition() == collectionListScrollpanel.getMaximumVerticalScrollPosition() && collectionItemDoSize >= 20) {
				pageNumber = pageNumber + 1;
				getUiHandlers().getMoreListItems(20, pageNumber, false);
			}
		}else{
			if(getUiHandlers().getMyCollectionsListPresenter().getScrollPanel().getVerticalScrollPosition() == getUiHandlers().getMyCollectionsListPresenter().getScrollPanel().getMaximumVerticalScrollPosition() && collectionItemDoSize >= 20) {
				pageNumber = pageNumber + 1;
				getUiHandlers().getMoreListItems(20, pageNumber, false);
			}
		}
	}
   	@Override
   	public HTMLPanel getSlot(){
   		return pnlSlot;
   	}
   	
   	@Override
   	public void setNoDataForAnonymousUser(boolean isAnonymous){
   		if(isAnonymous){
   			pnlMainContainer.setVisible(false);
   			pnlNoDataContainer.setVisible(true);
   			pnlNoDataContainer.add(new FoldersWelcomePage());
   			Window.enableScrolling(true);
   		}else{
   			pnlMainContainer.setVisible(true);
   			pnlNoDataContainer.setVisible(false);
   			Window.enableScrolling(false);
   		}
   	}
   	/**
   	 * @return viewType
   	 */
   	public String getViewType(){
		return AppClientFactory.getPlaceManager().getRequestParameter(VIEW);
   	}
    /**
     * Highlight the Tree based on id's when reveal the page.
     */
	@Override
	public void updateLeftShelfPanelActiveStyle() {
		String gooruOid = null;
		String o1 = AppClientFactory.getPlaceManager().getRequestParameter(O1_LEVEL);
		String o2 = AppClientFactory.getPlaceManager().getRequestParameter(O2_LEVEL);
		String o3 = AppClientFactory.getPlaceManager().getRequestParameter(O3_LEVEL);
		String id = AppClientFactory.getPlaceManager().getRequestParameter(ID);
		ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget(); 
		if(shelfTreeWidget==null || organizeRootPnl.getStyleName().contains("active")) {
			if(id!=null) {
				gooruOid = id;
			} else {
				gooruOid = o1;
			}
			for(int i = 0; i < shelfFolderTree.getItemCount(); i++) { 
				TreeItem item = shelfFolderTree.getItem(i);
				checkFolderItemStyle(item, gooruOid);
			}
			organizeRootPnl.addStyleName("active");
		} else {
			/** If the selected folder is closed, and when clicked on right side the following condition executes and make that folder open. **/
			if(treeChildSelectedItem.getState()==false){
				treeChildSelectedItem.setState(true);
			}
			if(organizeRootPnl.getStyleName().contains("active")) {
				gooruOid = o1;
			} else if(shelfTreeWidget.getLevel()==1) {
				if(id==null){
					gooruOid = o2;
				}else{
					gooruOid = id; 
				}
			} else if(shelfTreeWidget.getLevel()==2) {
				if(id==null){
					gooruOid = o3;
				}else{
					gooruOid = id;
				}
			} else if(shelfTreeWidget.getLevel()==3) {
				gooruOid = id;
			}
			for(int i = 0; i < treeChildSelectedItem.getChildCount(); i++) {
				 TreeItem item = treeChildSelectedItem.getChild(i);
				 checkFolderItemStyle(item, gooruOid);
			}
		}
	}
	
	private void checkFolderItemStyle(TreeItem item, String gooruOid) {
		ShelfTreeWidget updatedItem = (ShelfTreeWidget) item.getWidget();
		if(gooruOid!=null){
			if(gooruOid.equalsIgnoreCase(updatedItem.getCollectionDo().getGooruOid())) {
				treeChildSelectedItem = item;
				//updatedItem.setActiveStyle(true);
				setFolderActiveStatus();
				return;
			}
		} 
	}
	/**
	 * set basic data of course and get the folderObj
	 * @return folderObj 
	 */
	public FolderDo getFolderDo(){
		FolderDo folderObj = new FolderDo();
		folderObj.setTitle(UNTITLEDCOURSE);
		folderObj.setType("course");
		return folderObj;
	}
   /**
    * Updates the respective tree widget,
    * as we create/update course/unit/lesson/collection 
    */
	@Override
	public void updateTreeWidget(FolderDo courseDo,boolean flag) {
		ShelfTreeWidget shelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getWidget();
		shelfTreeWidget.updateData(courseDo);
		String type = shelfTreeWidget.getTreeWidgetType();

		if(COURSE.equalsIgnoreCase(type)){
			HashMap<String,String> urlParams = new HashMap<String,String>();
			urlParams.put(COURSE, courseDo.getTitle()); 
			urlParams.put(O1_LEVEL,courseDo.getGooruOid());
			shelfTreeWidget.setUrlParams(urlParams);

		}else if(UNIT.equalsIgnoreCase(type)){

			ShelfTreeWidget parentShelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getWidget();
			HashMap<String,String> urlParams = new HashMap<String,String>();
			urlParams.put(COURSE,parentShelfTreeWidget.getUrlParams().get(COURSE));
			urlParams.put(UNIT, courseDo.getTitle());
			urlParams.put(O1_LEVEL,parentShelfTreeWidget.getUrlParams().get("o1"));
			urlParams.put(O2_LEVEL,courseDo.getGooruOid());
			shelfTreeWidget.setUrlParams(urlParams);

		}else if(LESSON.equalsIgnoreCase(type)){

			ShelfTreeWidget courseShelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getParentItem().getWidget();
			HashMap<String,String> urlParams = new HashMap<String,String>();
			urlParams.put(COURSE,courseShelfTreeWidget.getUrlParams().get(COURSE));
			urlParams.put(O1_LEVEL,courseShelfTreeWidget.getUrlParams().get("o1"));

			ShelfTreeWidget unitShelfTreeWidget = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getWidget();
			urlParams.put(UNIT, unitShelfTreeWidget.getUrlParams().get(UNIT));
			urlParams.put(O2_LEVEL,unitShelfTreeWidget.getUrlParams().get("o2"));

			urlParams.put(LESSON,courseDo.getTitle());
			urlParams.put(O3_LEVEL,courseDo.getGooruOid());

			shelfTreeWidget.setUrlParams(urlParams);
		}
		/*else{
			ShelfTreeWidget shelfTreeWidget1 = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getWidget();
			HashMap<String,String> urlParams = new HashMap<String,String>();
			urlParams.put(COURSE,shelfTreeWidget1.getUpdatedWidgetsTitleType().get(COURSE));
			urlParams.put(COLLECTION, courseDo.getTitle());
			urlParams.put("o1",shelfTreeWidget1.getUrlParams().get("o1"));
			urlParams.put("o2",shelfTreeWidget1.getUrlParams().get("o2"));
			urlParams.put("o3",shelfTreeWidget1.getUrlParams().get("o2"));
			urlParams.put("id",courseDo.getGooruOid());

			shelfTreeWidget.setUrlParams(urlParams);
		}*/
	}

	/**
	 * Sets the boolean value to enable or disable the course create.
	 */
	@Override
	public void enableDisableCourseButton(boolean isEnable) {
		if(isEnable){
			createNewCourse.getElement().getFirstChildElement().getStyle().setBackgroundColor("#4d99cd");
			createNewCourse.getElement().getFirstChildElement().getStyle().setCursor(Cursor.POINTER);
		}else{
			createNewCourse.getElement().getFirstChildElement().getStyle().setBackgroundColor("#dddddd");
			createNewCourse.getElement().getFirstChildElement().getStyle().setCursor(Cursor.DEFAULT);
		}
		setCreateCourse(isEnable);
	}

	/**
	 * @return the isCreateCourse
	 */
	public boolean isCreateCourse() {
		return isCreateCourse;
	}

	/**
	 * @param isCreateCourse the isCreateCourse to set
	 */
	public void setCreateCourse(boolean isCreateCourse) {
		this.isCreateCourse = isCreateCourse;
	}
	@Override
	public Label getCollectionLabel(){
		return lblCollectionTitle;
	}

	@Override
	public void removeDeletedTreeWidget(String deletedTreeWidgetId,String currentTypeView){
		
		if(COURSE.equalsIgnoreCase(currentTypeView)){
			for (FolderDo folderDo : SHELF_COLLECTIONS) {
				if(folderDo.getGooruOid().equalsIgnoreCase(deletedTreeWidgetId)){
					SHELF_COLLECTIONS.remove(folderDo);
					break;
				}
			}
			organizeRootPnl.addStyleName("active");
			getUiHandlers().setRightListData(SHELF_COLLECTIONS, null);
			treeChildSelectedItem.remove();
		}else if(UNIT.equalsIgnoreCase(currentTypeView)){
			ShelfTreeWidget deletedTreeParentWidget = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getWidget();
			TreeItem treeItem = treeChildSelectedItem.getParentItem();
			getUiHandlers().setRightPanelData(deletedTreeParentWidget.getCollectionDo(), deletedTreeParentWidget.getCollectionDo().getType(),folderListDoChild);
			treeChildSelectedItem.remove();
			checkFolderItemStyle(treeItem,deletedTreeParentWidget.getCollectionDo().getGooruOid());
			getUiHandlers().onDeleteSetBreadCrumbs(deletedTreeParentWidget.getCollectionDo().getTitle(),COURSE);
			
		}else if(LESSON.equalsIgnoreCase(currentTypeView)){
			ShelfTreeWidget deletedTreeParentWidget = (ShelfTreeWidget) treeChildSelectedItem.getParentItem().getWidget();
			TreeItem treeItem = treeChildSelectedItem.getParentItem();
			getUiHandlers().setRightPanelData(deletedTreeParentWidget.getCollectionDo(), deletedTreeParentWidget.getCollectionDo().getType(),folderListDoChild);
			treeChildSelectedItem.remove();
			checkFolderItemStyle(treeItem,deletedTreeParentWidget.getCollectionDo().getGooruOid());
			getUiHandlers().onDeleteSetBreadCrumbs(deletedTreeParentWidget.getCollectionDo().getTitle(),UNIT);
		}
	}
}