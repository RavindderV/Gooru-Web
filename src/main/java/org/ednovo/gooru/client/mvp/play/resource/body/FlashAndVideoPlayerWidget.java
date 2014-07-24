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
package org.ednovo.gooru.client.mvp.play.resource.body;

import org.ednovo.gooru.client.gin.AppClientFactory;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class FlashAndVideoPlayerWidget extends Composite {



	public FlashAndVideoPlayerWidget(String resourceUrl, String videoStartTime, String videoEndTime) {
		int startTimeInSeconds = 0;
		int endTimeInSeconds = 0;
		String startTimeEndTime="";
		if(videoStartTime==null && videoEndTime==null)
		{
			startTimeEndTime="start=0&end=0;";
		}
		if(videoStartTime!=null){
			startTimeInSeconds=getStartOrEndTime(videoStartTime);
			startTimeEndTime="start=" + startTimeInSeconds;
		}
		if(videoEndTime!=null){
			endTimeInSeconds = getStartOrEndTime(videoEndTime);
			startTimeEndTime=startTimeEndTime+"&end="+endTimeInSeconds+";";
		}
		String tabView=AppClientFactory.getPlaceManager().getRequestParameter("tab", null);
		int autoPlay=tabView!=null&&tabView.equalsIgnoreCase("narration")?0:1;
		String embeddableHtmlString = "<embed id=\"playerid\" type=\"application/x-shockwave-flash\" src=\""+getProtocal()+"//www.youtube.com/v/"
				+ resourceUrl+"?" +startTimeEndTime +"rel=0&amp;enablejsapi=1&amp;version=3&amp;autoplay=0&amp;start=1\""
				+ " width=\"100%\" height=\"100%\" quality=\"high\" allowfullscreen=\"true\" allowscriptaccess=\"always\" autoplay=\"0\" wmode=\"transparent\">";

		HTMLPanel resourcePreviewPanel = new HTMLPanel(embeddableHtmlString);
		resourcePreviewPanel.setStyleName("resourcePreviewWebResourceContainer");
		resourcePreviewPanel.setSize("100%", "100%");

		initWidget(resourcePreviewPanel);

	}

	private int getStartOrEndTime(String resStart) {
		String startParam = resStart;
		int start=0;
		String[] starTimeSplit = startParam.split(":");
		if (starTimeSplit.length == 2) {
			int minute = Integer.parseInt(starTimeSplit[0]);
			int second = Integer.parseInt(starTimeSplit[1]);
			start = second + (60 * minute);

		} else if (starTimeSplit.length == 3) {

			int hour = Integer.parseInt(starTimeSplit[0]);
			int minute = Integer.parseInt(starTimeSplit[1]);
			int second = Integer.parseInt(starTimeSplit[2]);

			start = second + (60 * minute) + (3600 * hour);

		}

		return start;
	}
	public static String getProtocal(){
		return Window.Location.getProtocol().equalsIgnoreCase("http:")?"http:":"https:";
	}
}
