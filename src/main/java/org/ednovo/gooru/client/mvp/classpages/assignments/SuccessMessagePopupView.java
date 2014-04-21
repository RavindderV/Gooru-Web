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
package org.ednovo.gooru.client.mvp.classpages.assignments;

/*
 * 
 * @fileName : AssignmentPopupView.java
 *
 * @description : This class is responsible to show and add assignments 
 *
 *
 * @version : 5.3
 *
 * @date: May 4, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */

import org.ednovo.gooru.shared.util.MessageProperties;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SuccessMessagePopupView extends Composite implements MessageProperties{
	private PopupPanel appPopUp;
	@UiField Button okayButton;
	@UiField Label successPopupHeader;
	
	@UiField HTML successPopupBodyText;
	 
	public interface SuccessMessagePopupViewUiBinder extends UiBinder<Widget, SuccessMessagePopupView> {

	}
	public static SuccessMessagePopupViewUiBinder uiBinder = GWT.create(SuccessMessagePopupViewUiBinder.class);

	public SuccessMessagePopupView(String collectonTitle) {
		appPopUp=new PopupPanel();
		appPopUp.setWidget(uiBinder.createAndBindUi(this));
		AddAssignmentContainerCBundle.INSTANCE.css().ensureInjected();
		setStaticTexts(collectonTitle);
		Window.enableScrolling(false);
		appPopUp.setStyleName(AddAssignmentContainerCBundle.INSTANCE.css().successPopupContainer());
		appPopUp.setGlassEnabled(true);
		appPopUp.show();
		appPopUp.center();
	}
	public void setStaticTexts(String collectonTitle){
		successPopupHeader.setText(GL1384);
		successPopupBodyText.setHTML(StringUtil.generateMessage(GL1385, collectonTitle));
		okayButton.setText(GL1386);
	}
	@UiHandler("okayButton")
	public void closePopupEvent(ClickEvent event){
		hide();
	}
	public void hide(){
		Window.enableScrolling(true);
		appPopUp.hide();
	}


}
