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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.item;

import java.util.List;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.child.ChildPresenter;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.service.ResourceServiceAsync;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;

import com.google.inject.Inject;
/**
 * @fileName : ShelfCollectionResourceChildPresenter.java
 *
 * @description : This class is the presenter for the shelf collection resource child presenter.
 *
 *
 * @version : 1.0
 *
 * @date: 02-Jan-2014
 *
 * @Author Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class ShelfCollectionResourceChildPresenter extends ChildPresenter<ShelfCollectionResourceChildPresenter, IsShelfCollectionResourceView> {

	private SimpleAsyncCallback<CollectionItemDo> updateCollectionItemAsyncCallback;

	private SimpleAsyncCallback<CollectionItemDo> copyCollectionItemAsyncCallback;
	
	private SimpleAsyncCallback<Void> updateQuestionItemResourceAsyncCallback;

	private SimpleAsyncCallback<CollectionItemDo> createCollectionItemAsyncCallback;

	private SimpleAsyncCallback<CollectionItemDo> reorderCollectionItemAsyncCallback;

	private SimpleAsyncCallback<Void> deleteCollectionItemAsyncCallback;
	
	private SimpleAsyncCallback<List<CollectionDo>> getMyUserCollectionsAsyncCallback;
	
	@Inject private ImageUploadPresenter imageUploadPresenter;

	/**
	 * Class constructor
	 * 
	 * @param childView 
	 */
	public ShelfCollectionResourceChildPresenter(IsShelfCollectionResourceView childView) {
		super(childView);
	}

	/**
	 * Update collection item by collection item id which is mandatory   
	 * 
	 * @param collectionItemId gooruOid of collection item
	 * @param narration about collection item resource
	 * @param start  page or time of collection item
	 * @param stop  page or time of collection item
	 */
	public void updateCollectionItem(String collectionItemId, String narration, String start, String stop) {
		getResourceService().updateCollectionItemMetadata(collectionItemId, narration, null, start, stop, getUpdateCollectionItemAsyncCallback());
	}
	/**
	 * @function updateNarrationItem 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to update the narration.
	 * 
	 * 
	 * @parm(s) : @param collectionItemId
	 * @parm(s) : @param narration
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public void updateNarrationItem(String collectionItemId, String narration) {
		getResourceService().updateNarrationMetadata(collectionItemId, narration, null, getUpdateCollectionItemAsyncCallback());
	}

	
	/*public void getUserCollections(){
		getResourceService().getUserCollection(getUserCollectionsAsyncCallback());
	}*/
	/**
	 * @function getUserColletionsList 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to get the user collection list.
	 * 
	 * 
	 * @parm(s) : @param pageSize
	 * @parm(s) : @param pageNum
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public void getUserColletionsList(Integer pageSize,Integer pageNum)
	{
		getResourceService().getUserCollectionList(pageSize,pageNum,false,getUserCollectionsAsyncCallback());
	}

	/**
	 *  Delete collection item by collection item id which is mandatory   
	 * 
	 * @param collectionItemId  gooruOid of collection item
	 */
	public void deleteCollectionItem(String collectionItemId) {
		this.getResourceService().deleteCollectionItem(collectionItemId, getDeleteCollectionItemAsyncCallback());
	}

	/**
	 * Reorder collection item with new sequence
	 * 
	 * @param collectionItem instance of {@link CollectionItemDo}
	 */
	public void reorderCollectionItem(CollectionItemDo collectionItem) {
		getResourceService().reorderCollectionItem(collectionItem, getReorderCollectionItemAsyncCallback());
	}

	/**
	 * Copy collection item by collection item id which is mandatory  
	 * 
	 * @param collectionItemId gooruOid of collection item
	 */
	/*public void copyCollectionItem(String collectionItemId) {
		getResourceService().copyCollectionItem(collectionItemId, getCopyCollectionItemAsyncCallback());
	}*/
	
	/**
	 * create collection item by collection item id which is mandatory  
	 * 
	 * @param collectionItemId gooruOid of collection item
	 */
	public void createCollectionItem(String collectionId, String collectionItemId) {
		getResourceService().copyCollectionItem(collectionId, collectionItemId, getCopyCollectionItemAsyncCallback());
	}
    /*public void updateResourceQuestion(String collectionItemId,CollectionQuestionItemDo collectionQuestionItemDo){
    //	getResourceService().updateQuestionResource(collectionItemId, collectionQuestionItemDo, getUpdateQuestionItemResourceAsyncCallback());
    	
    }*/
	/**
	 * @return instance of {@link CollectionDo} after update the collections
	 */
	public SimpleAsyncCallback<List<CollectionDo>> getUserCollectionsAsyncCallback() {
		if (getMyUserCollectionsAsyncCallback == null) {
			getMyUserCollectionsAsyncCallback = new SimpleAsyncCallback<List<CollectionDo>>() {

				@Override
				public void onSuccess(List<CollectionDo> result) {
					getView().onPostUserCollections(result);
				}
			};
		}
		return getMyUserCollectionsAsyncCallback;
	}
	/**
	 * @return instance of {@link CollectionItemDo} after update the collection item
	 */
	public SimpleAsyncCallback<CollectionItemDo> getUpdateCollectionItemAsyncCallback() {
		if (updateCollectionItemAsyncCallback == null) {
			updateCollectionItemAsyncCallback = new SimpleAsyncCallback<CollectionItemDo>() {

				@Override
				public void onSuccess(CollectionItemDo result) {
					getView().getVisible().setVisible(false);
					getView().onPostUpdate(result);
				
					
				}
				@Override
				public void onFailure(Throwable arg0) {
					getView().getVisible().setVisible(false);
				}
			};
		}
		return updateCollectionItemAsyncCallback;
	}

	
	/**
	 * @return instance of {@link CollectionItemDo} after reorder the collection item
	 */
	public SimpleAsyncCallback<CollectionItemDo> getReorderCollectionItemAsyncCallback() {
		if (reorderCollectionItemAsyncCallback == null) {
			reorderCollectionItemAsyncCallback = new SimpleAsyncCallback<CollectionItemDo>() {

				@Override
				public void onSuccess(CollectionItemDo result) {
					getView().onPostReorder(result);
				}
			};
		}
		return reorderCollectionItemAsyncCallback;
	}

	/**
	 * @return instance of {@link CollectionItemDo}
	 */
	public SimpleAsyncCallback<CollectionItemDo> getCopyCollectionItemAsyncCallback() {
		if (copyCollectionItemAsyncCallback == null) {
			copyCollectionItemAsyncCallback = new SimpleAsyncCallback<CollectionItemDo>() {

				@Override
				public void onSuccess(CollectionItemDo result) {
					
					getView().onPostCopy(result);
				}
			};
		}
		return copyCollectionItemAsyncCallback;
	}

	/**
	 * @return instance of {@link CollectionItemDo}
	 */
	public SimpleAsyncCallback<CollectionItemDo> getCreateCollectionItemAsyncCallback() {
		if (createCollectionItemAsyncCallback == null) {
			createCollectionItemAsyncCallback = new SimpleAsyncCallback<CollectionItemDo>() {

				@Override
				public void onSuccess(CollectionItemDo result) {
					getView().onPostCopy(result);
				}
			};
		}
		return copyCollectionItemAsyncCallback;
	}
	/**
	 * @function getDeleteCollectionItemAsyncCallback 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This is simple async call back for  delete collection item.
	 * 
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : SimpleAsyncCallback<Void>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public SimpleAsyncCallback<Void> getDeleteCollectionItemAsyncCallback() {
		if (deleteCollectionItemAsyncCallback == null) {
			deleteCollectionItemAsyncCallback = new SimpleAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					getView().onPostDelete();
				}
			};
		}
		return deleteCollectionItemAsyncCallback;
	}
	/**
	 * @function getUpdateQuestionItemResourceAsyncCallback 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description :This is simple async call back for update question item.
	 * 
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : SimpleAsyncCallback<Void>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public SimpleAsyncCallback<Void> getUpdateQuestionItemResourceAsyncCallback() {
		if (updateQuestionItemResourceAsyncCallback == null) {
			updateQuestionItemResourceAsyncCallback = new SimpleAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					getView().onPutUpdate();
					
				}
			};
		}
		return updateQuestionItemResourceAsyncCallback;
	}
	/**
	 * 
	 * @function setUpdateQuestionItemResourceAsyncCallback 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to update question item resource async call back.
	 * 
	 * 
	 * @parm(s) : @param updateQuestionItemResourceAsyncCallback
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public void setUpdateQuestionItemResourceAsyncCallback(
			SimpleAsyncCallback<Void> updateQuestionItemResourceAsyncCallback) {
		this.updateQuestionItemResourceAsyncCallback = updateQuestionItemResourceAsyncCallback;
		
	}
	
	
	
	/**
	 * @function getResourceService 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to get the resource async service.
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : ResourceServiceAsync
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public ResourceServiceAsync getResourceService() {
		
		return AppClientFactory.getInjector().getResourceService();
	}

}
