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
package org.ednovo.gooru.client.mvp.home.library.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @fileName : SetLoadingIconEvent.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Dec 4, 2013
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class SetLoadingIconEvent extends GwtEvent<SetLoadingIconHandler> {

	private boolean isVisible;
	private Integer topicId;
	
	public static final Type<SetLoadingIconHandler> TYPE = new Type<SetLoadingIconHandler>();
	
	/**
	 * @param topicId 
	 * 
	 */
	public SetLoadingIconEvent(boolean isVisible, Integer topicId) {
		this.isVisible = isVisible;
		this.topicId = topicId;
	}

	@Override
	public Type<SetLoadingIconHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SetLoadingIconHandler handler) {
		handler.setLoadingIcon(isVisible,topicId);
	}

}
