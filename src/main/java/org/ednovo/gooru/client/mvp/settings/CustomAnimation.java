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
package org.ednovo.gooru.client.mvp.settings;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

public class CustomAnimation extends Animation{
	
	private int desiredHeight=0;
	private int startHeight=0;
	private String showWidget="";
	private Widget resizeWidget;
	
	public CustomAnimation(){
		
	}
	
	public CustomAnimation(Widget widget){
		this.resizeWidget=widget;
		
		if(resizeWidget.getElement().getStyle().getProperty("display").equals("none")){
			resizeWidget.getElement().getStyle().setProperty("visibility", "hidden");
			resizeWidget.getElement().getStyle().clearDisplay();
			desiredHeight=resizeWidget.getOffsetHeight();
			startHeight=0;
			resizeWidget.getElement().getStyle().setProperty("display", "none");
			resizeWidget.getElement().getStyle().clearVisibility();
		}
		else{
			startHeight=resizeWidget.getOffsetHeight();
			
		}
		 
	}
	protected void onStart() {
		super.onStart();
		if(resizeWidget.getElement().getStyle().getProperty("display").equals("none")){
			resizeWidget.getElement().getStyle().setProperty("display", "block");
			resizeWidget.getElement().getStyle().setProperty("height", "0px");
		}
		else{
			showWidget="block";
		}
	}
	
	
	@Override
	protected void onUpdate(double progress) {
		double width = extractProportionalLength(progress) ;
		resizeWidget.setHeight( width + Unit.PX.getType());
		
	}
	
	 private double extractProportionalLength(double progress) {
            double outHeight = startHeight - (startHeight - desiredHeight) * progress; // this is for hidding ,eg: startHeight=200,desiredHeight=
            return outHeight;
      }
	 protected double interpolate(double progress) {
		    return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
	 }
	
	protected void onComplete() {
		resizeWidget.getElement().getStyle().clearHeight();
		
		if(showWidget.equals("block")){
			resizeWidget.getElement().getStyle().setProperty("display", "none");
		}
	}
}
