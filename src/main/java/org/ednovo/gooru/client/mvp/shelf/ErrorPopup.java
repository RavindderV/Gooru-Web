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
package org.ednovo.gooru.client.mvp.shelf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.ui.HTMLEventPanel;
import org.ednovo.gooru.shared.model.content.ClasspageListDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * 
 * @fileName : ErrorPopup.java
 *
 * @description : Thi sfile is related to ERROR POPUP.
 *
 *
 * @version : 1.0
 *
 * @date: 02-Jan-2014
 *
 * @Author : Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class ErrorPopup extends PopupPanel {
      
	@UiField HTMLEventPanel cancelButton;
	
	@UiField(provided = true)
	ErrorPopUpCBundle res;
		
	@UiField Label errorMessage;
	
	@UiTemplate("ErrorPopup.ui.xml")
	interface Binder extends UiBinder<Widget, ErrorPopup> {

	}	
	private static final Binder binder = GWT.create(Binder.class);
	/**
	 * Class Constructor.
	 * @param message
	 */
	public ErrorPopup(String message){
		this.res = ErrorPopUpCBundle.INSTANCE;
		res.css().ensureInjected();
		add(binder.createAndBindUi(this));
		this.setGlassEnabled(true);
		this.setSize("475px", "200px");
		this.center();
		errorMessage.setText(message);
	}
	/**
	 * 
	 * @function onCancelClicked 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description :This UIHandler is used to navigate to either class page or shelf based on place token.
	 * 
	 * 
	 * @parm(s) : @param clickEvent
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	@UiHandler("cancelButton")
//	public abstract void onClcickClicked(ClickEvent event);
	
	public void onCancelClicked(ClickEvent clickEvent) {
		if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.SHELF)){
			navigateShelf();
		}else if (AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken().equalsIgnoreCase(PlaceTokens.EDIT_CLASSPAGE)){
			navigateClasspage();
		}
		hide();
	}
	/**
	 * 
	 * @function navigateShelf 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to navigate to shelf.
	 * 
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void navigateShelf(){
		AppClientFactory.getInjector().getResourceService().getUserCollection(new SimpleAsyncCallback<List<CollectionDo>>() {
            @Override
            public void onSuccess(List<CollectionDo> result) {
                if(result.size()>0) {
                    AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF, new String[] { "id", result.get(0).getGooruOid() });
                } else {
                    AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.SHELF);
                }
            }
        });
	}
	/**
	 * 
	 * @function navigateClasspage 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to navigate to class page.
	 * 
	 * 
	 * @parm(s) : 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	private void navigateClasspage(){
		AppClientFactory.getInjector().getClasspageService().v2GetAllClasspages("1", "0", new SimpleAsyncCallback<ClasspageListDo>() {

			@Override
			public void onSuccess(ClasspageListDo result) {
				if (result.getSearchResults().size()>0){
					String gooruOId = result.getSearchResults().get(0).getGooruOid();
					Map<String, String> params = new HashMap<String, String>();
					params.put("classpageid", gooruOId);
					params.put("pageNum", "0");
					params.put("pageSize", "10");
					params.put("pos", "1");
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.EDIT_CLASSPAGE, params);
				}else{
					AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.HOME);
				}
			}
		});
	}
}
