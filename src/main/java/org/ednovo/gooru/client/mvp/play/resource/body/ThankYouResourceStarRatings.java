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

import java.util.HashMap;
import java.util.Map;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.rating.RatingWidgetView;
import org.ednovo.gooru.client.mvp.rating.events.OpenReviewPopUpEvent;
import org.ednovo.gooru.client.mvp.rating.events.PostUserReviewEvent;
import org.ednovo.gooru.client.util.SetStyleForProfanity;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
* @fileName : StarRatingsUc.java
*
* @description : Creates Thank you tool-tip once user rated. 
* 
* @version : 1.0
*
* @date:  April, 2013.
*
* @Author: Gooru Team.
* 
* @Reviewer: Gooru Team.
*/
public class ThankYouResourceStarRatings extends PopupPanel implements MessageProperties {
	
	private static ThankYouResourceStarRatingsUiBinder uiBinder = GWT.create(ThankYouResourceStarRatingsUiBinder.class);

	interface ThankYouResourceStarRatingsUiBinder extends UiBinder<Widget, ThankYouResourceStarRatings> {}
	
	@UiField Button btnSkip,btnPost;
	@UiField TextArea ratingCommentTxtArea;
	@UiField public FlowPanel ratingWidgetPanel;
	@UiField HTMLPanel buttonsContainer;
	@UiField Label saveAndPsotLbl,mandatoryDescLblForSwareWords,reviewTextAreaTitle;
	private RatingWidgetView ratingWidgetView=null;
	
	String assocGooruOId,review,createrName;
	Integer score,count;
	double average;
	final String saving="Saving..";
	final String posting="Posting..";
	/**
	 * Class Constructor
	 * @param assocGooruOId 
	 * @param score 
	 * @param review 
	 * @param average 
	 * @param count 
	 */
	public ThankYouResourceStarRatings(String assocGooruOId, Integer score, String review, double average, Integer count,String createrName){   
		this.assocGooruOId = assocGooruOId;
		this.score = score;
		this.review = review;
		this.average=average;
		this.count=count;
		this.createrName = createrName;
		setWidget(uiBinder.createAndBindUi(this));
		setUserReview(review);
		setAvgRatingWidget();
		setGlassEnabled(true);
		saveAndPsotLbl.setVisible(false);
		buttonsContainer.setVisible(true);
		
	}
	
	/**
	 * Average star ratings widget will get integrated.
	 */
	private void setAvgRatingWidget() {
		ratingWidgetView=new RatingWidgetView();
		ratingWidgetView.getRatingCountLabel().setText(count.toString());
		ratingWidgetView.setAvgStarRating(average);
		ratingWidgetView.getRatingCountLabel().addClickHandler(new ShowRatingPopupEvent());
		ratingWidgetPanel.add(ratingWidgetView);
	}
	
	/**
	 * 
	 * Inner class implementing {@link ClickEvent}
	 *
	 */
	private class ShowRatingPopupEvent implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			/**
			 * OnClick of count label event to invoke Review pop-pup
			 */

			AppClientFactory.fireEvent(new OpenReviewPopUpEvent(assocGooruOId, "",createrName)); 
		}
	}

	/**
	 * On click Post button user entered review will get posted.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("btnPost")
	public void onRatingReviewPostclick(ClickEvent clickEvent){
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("text", ratingCommentTxtArea.getText());
		AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean value) {
					if(!value){
						saveAndPsotLbl.setText("");
						saveAndPsotLbl.setVisible(true);
						buttonsContainer.setVisible(false);
						if(btnPost.getText().equalsIgnoreCase("Save")){
							saveAndPsotLbl.setText(saving);
							AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,true));  
						}else if(btnPost.getText().equalsIgnoreCase("Post")){
							saveAndPsotLbl.setText(posting);
							AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,false));  
						}
					}
					SetStyleForProfanity.SetStyleForProfanityForTextArea(ratingCommentTxtArea, mandatoryDescLblForSwareWords, value);
			}
		});
	}
	
	/**
	 * On click Skip button user user can skip by giving review and thank you tool tip will close.
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("btnSkip")
	public void onRatingReviewSkipclicked(ClickEvent clickEvent){
		hide();
	}
	
	/**
	 * Sets the user review on text area if available.
	 * @param review
	 */
	private void setUserReview(String review) {
		if(!review.equals("")){
			reviewTextAreaTitle.setText(GL1858);
			btnPost.setText("Save");
			ratingCommentTxtArea.setText(review.trim());
		}else{
			reviewTextAreaTitle.setText(GL1855);
			btnPost.setText("Post");
		}
	}
	
	@Override
	public void hide(boolean autoClose) {
		super.hide(true);
		if(autoClose){
			Map<String, String> parms = new HashMap<String, String>();
			parms.put("text", ratingCommentTxtArea.getText());
			AppClientFactory.getInjector().getResourceService().checkProfanity(parms, new SimpleAsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean value) {
						if(!value){
							saveAndPsotLbl.setText("");
							saveAndPsotLbl.setVisible(true);
							buttonsContainer.setVisible(false);
							if(btnPost.getText().equalsIgnoreCase("Save")){
								saveAndPsotLbl.setText(saving);
								AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,true));  
							}else if(btnPost.getText().equalsIgnoreCase("Post")){
								saveAndPsotLbl.setText(posting);
								AppClientFactory.fireEvent(new PostUserReviewEvent(assocGooruOId,ratingCommentTxtArea.getText().trim(),score,false));  
							}
						}
						SetStyleForProfanity.SetStyleForProfanityForTextArea(ratingCommentTxtArea, mandatoryDescLblForSwareWords, value);
				}
			});
		}
	}
}
