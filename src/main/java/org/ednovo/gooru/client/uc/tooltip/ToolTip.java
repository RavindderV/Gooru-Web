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
package org.ednovo.gooru.client.uc.tooltip;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * 
 * @fileName : ToolTip.java
 *
 * @description : This class is used to display the tooltips.
 *
 * @version : 1.0
 *
 * @date: 31-Dec-2013
 *
 * @Author Gooru Team
 *
 * @Reviewer: Gooru Team
 */
public class ToolTip extends PopupPanel implements MessageProperties, HasMouseOutHandlers{
	
	@UiField
	HTMLPanel panelCode;
	
	@UiField
	Label lblTitle;
	
	@UiField Anchor lblLink;
	
	
	public interface ToolTipUiBinder extends UiBinder<Widget, ToolTip>{
	}
	
	public static ToolTipUiBinder toolTipUiBinder=GWT.create(ToolTipUiBinder.class);{
	}
	/**
	 * Class constructor.
	 */
	public ToolTip(){
		setWidget(toolTipUiBinder.createAndBindUi(this));
		lblTitle.setText(GL0450);
		lblLink.setText(GL0451);
		lblLink.setTarget("_blank");
		
		this.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hide();
			}
		});
		AppClientFactory.getInjector().getHomeService().mosLink(new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				lblLink.setHref(result);
			}
		});
	}
	/**
	 * Class constructor.
	 * @param description
	 */
	public ToolTip(String description){
		
		setWidget(toolTipUiBinder.createAndBindUi(this));
		lblTitle.setText(description);
		lblLink.setText(GL0451);
		lblLink.setTarget("_blank");
		
		this.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hide();
			}
		});
		AppClientFactory.getInjector().getHomeService().mosLink(new SimpleAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				lblLink.setHref(result);
			}
		});
	}
	/**
	 * This will handle the mouse out event.
	 */
	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}
	
	
	
}
