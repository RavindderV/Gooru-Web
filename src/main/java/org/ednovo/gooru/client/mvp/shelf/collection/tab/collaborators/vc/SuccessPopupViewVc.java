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
package org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.vc;

import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.CollectionCollaboratorsCBundle;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author BLR Team
 * 
 */
public abstract class SuccessPopupViewVc extends PopupPanel implements MessageProperties {
 
	
	@UiField(provided = true)
	CollectionCollaboratorsCBundle collaborators;
	
	@UiField
	Button btnPositive;
	
	@UiField Label lblTitle, lblDescription,tagImageForTagging;
	
	@UiTemplate("SuccessPopupViewVc.ui.xml")
	interface Binder extends UiBinder<Widget, SuccessPopupViewVc> {

	}

	private static final Binder binder = GWT.create(Binder.class);
	
	/**
	 * 
	 */
	public SuccessPopupViewVc() {
		super(false);
		this.collaborators = CollectionCollaboratorsCBundle.INSTANCE;
		collaborators.css().ensureInjected();
		add(binder.createAndBindUi(this));
		this.setGlassEnabled(true);
		tagImageForTagging.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		Window.enableScrolling(false);
        AppClientFactory.fireEvent(new SetHeaderZIndexEvent(99, false));
                
        setElementId();
        
		this.center();

	}
	/**
	 * @function setElementId 
	 * 
	 * @created_date : Jan 31, 2014
	 * 
	 * @description
	 * 
	 * 
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 * 
	*/
	
	private void setElementId() {
		btnPositive.getElement().setId("btnPositive");
	}

	/* Setters */
	/**
	 * 
	 * @function setPositiveButtonText 
	 * 
	 * @created_date : Jan 31, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param text
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setPositiveButtonText(String text) {
		btnPositive.setText(text);
	}
	
	/**
	 * 
	 * @function setPopupTitle 
	 * 
	 * @created_date : Jan 31, 2014
	 * 
	 * @description
	 * 
	 * 
	 * @param title
	 * 
	 * @return : void
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 * 
	 *
	 *
	 */
	public void setPopupTitle(String title) {
		lblTitle.setText(title);
	}
	public void enableTaggingImage() {
		tagImageForTagging.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}
	public void setDescText(String desc){
		lblDescription.getElement().setInnerHTML(desc);
	}
	
	/* Click handler...*/
	
	@UiHandler("btnPositive")
	public void onPostitiveClickEvent(ClickEvent event){
		onClickPositiveButton(event);
	}
	
	/* Abstract methods to handle button events*/
	public abstract void onClickPositiveButton(ClickEvent event);
}
