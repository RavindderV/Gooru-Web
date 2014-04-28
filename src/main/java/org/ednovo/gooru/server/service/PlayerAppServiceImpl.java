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
package org.ednovo.gooru.server.service;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.service.PlayerAppService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.deserializer.ResourceCollectionDeSerializer;
import org.ednovo.gooru.server.deserializer.ShareDeSerializer;
import org.ednovo.gooru.server.request.JsonResponseRepresentation;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.CollectionItemsList;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.model.content.ReactionDo;
import org.ednovo.gooru.shared.model.content.ResoruceCollectionDo;
import org.ednovo.gooru.shared.model.player.CommentsDo;
import org.ednovo.gooru.shared.model.player.CommentsListDo;
import org.ednovo.gooru.shared.model.player.FeaturedContentDo;
import org.ednovo.gooru.shared.model.search.ResourceSearchResultDo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.StringRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("playerService")
@ServiceURL("/playerService")
public class PlayerAppServiceImpl extends BaseServiceImpl implements PlayerAppService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ShareDeSerializer shareDeSerializer;
	
	private static final String REAL_URL = "realUrl";
	private static final String RESOURCE="resource";
	private static final String NARRATION="narration";
	private static final String TITLE="title";
	private static final String FIRST_NAME="firstName";
	private static final String PROFILE_IMAGE_URL="profileImageUrl";


	@Override
	public CollectionDo getSimpleCollectionDetils(String simpleCollectionId,String resourceId,String tabview, String rootNodeId) {
		return null;
	}
	
	public CollectionDo deserializeCollection(JsonRepresentation jsonRep) {
		CollectionDo collectionDo=new CollectionDo();
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				collectionDo=JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CollectionDo.class);
				if(collectionDo!=null&&collectionDo.getCollectionItems()!=null&&collectionDo.getCollectionItems().size()>0){
					for(int i=0;i<collectionDo.getCollectionItems().size();i++){
						CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(i);
						String decodeUrl=collectionItemDo.getResource().getUrl();
						if(decodeUrl!=null&&!decodeUrl.equals("")&&!decodeUrl.equals("null")){
							if(decodeUrl.substring(0, 4).equalsIgnoreCase("http")){
								
							}else{
								String encodeUrl;
								try {
									encodeUrl = URLEncoder.encode(collectionItemDo.getResource().getUrl(),"UTF-8").replaceAll("\\+", "%20");
									collectionDo.getCollectionItems().get(i).getResource().setUrl(encodeUrl);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return collectionDo;
	}
	
	public ResoruceCollectionDo deserializeResourceCollection(JsonRepresentation jsonRep) {
		ResoruceCollectionDo resourceCollectionDo=new ResoruceCollectionDo();
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				JSONObject resourceCollectionObject=jsonRep.getJsonObject();
				JSONArray collectionList=resourceCollectionObject.getJSONArray("searchResults");
				resourceCollectionDo.setTotalHitCount(resourceCollectionObject.getInt("totalHitCount"));
				List<ResourceSearchResultDo> resourceSearchResultList=new ArrayList<ResourceSearchResultDo>();
				for(int i=0;i<collectionList.length();i++){
					resourceSearchResultList.add(ResourceCollectionDeSerializer.deserializeRecord(collectionList.getJSONObject(i)));
				}
				resourceCollectionDo.setSearchResults(resourceSearchResultList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return resourceCollectionDo;
	}


	@Override
	public CollectionDo getSimpleCollectionDetils(String apiKey,String simpleCollectionId, String resourceId, String tabView, String rootNodeId) {
		CollectionDo collectionDo = new CollectionDo();
		JsonRepresentation jsonRepresentation = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.V2_GET_COLLECTION,simpleCollectionId,getLoggedInSessionToken());
		url+=getStandardId(rootNodeId);
		JsonResponseRepresentation jsonResponseRep=ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRepresentation=jsonResponseRep.getJsonRepresentation();
		if(jsonResponseRep.getStatusCode()==200){
			collectionDo = deserializeCollection(jsonRepresentation);
			collectionDo.setStatusCode(jsonResponseRep.getStatusCode());
		}else{
			collectionDo=new CollectionDo();
			collectionDo.setStatusCode(jsonResponseRep.getStatusCode());
		}
		return collectionDo;
	}
	

	public ResoruceCollectionDo getResourceCollectionsList(String gooruOid,String pageNum,String pageSize) {
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.RESOURCE_COLLECTION_LIST, getLoggedInSessionToken(),pageNum, pageSize, gooruOid);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		return deserializeResourceCollection(jsonRep);
	}

	@Override
	public CollectionItemDo getResourceCollectionItem(String apiKey,String resourceId, String tabView) {
		JsonRepresentation jsonRepresentation = null;
		CollectionItemDo collectionItemDo=null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_RESOURCE_DETAILS,resourceId, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep=ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRepresentation=jsonResponseRep.getJsonRepresentation();
		try {
			if(jsonResponseRep.getStatusCode()==200){
				collectionItemDo=ResourceCollectionDeSerializer.deserializeCollectionItemDo(jsonRepresentation.getJsonObject());
				collectionItemDo.setStatusCode(jsonResponseRep.getStatusCode());
				String decodeUrl=collectionItemDo.getResource().getUrl();
				if(decodeUrl!=null&&!decodeUrl.equals("")&&!decodeUrl.equals("null")){
					if(decodeUrl.substring(0, 4).equalsIgnoreCase("http")){
					}else{
						String encodeUrl;
						try {
							encodeUrl = URLEncoder.encode(collectionItemDo.getResource().getUrl(),"UTF-8").replaceAll("\\+", "%20");
							collectionItemDo.getResource().setUrl(encodeUrl);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				collectionItemDo=new CollectionItemDo();
				collectionItemDo.setStatusCode(jsonResponseRep.getStatusCode());
			}
			//Added this line because of URL encoding is not supported in Shared and View packages.
			//collectionItemDo.getResource().setEncodedUrl(URLEncoder.encode(collectionItemDo.getResource().getUrl()));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return collectionItemDo;
	}
	
	@Override
	public Map<String, String> getShortenShareUrl(String contentGooruOid) {
		JsonRepresentation jsonRep = null;
		Map<String, String> shareUrls=new HashMap<String, String>();
		String embededShortenUrl= UrlGenerator.generateUrl(getHomeEndPoint()+"/" + UrlToken.COLLECTION_PLAY_EMBEDED_URL.getUrl(), contentGooruOid);
		String collectionShareUrl= UrlGenerator.generateUrl(getHomeEndPoint() +"/" + UrlToken.COLLECTION_PLAY_URL.getUrl()+"%26share=true", contentGooruOid);
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SHARE_SHORTEN_URL_PLAY, contentGooruOid, getLoggedInSessionToken(),embededShortenUrl);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		String shortenUrl=shareDeSerializer.deserializeShortenUrlFromJson(jsonRep);
		shareUrls.put("embedbitlyurl", shortenUrl);
		url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.SHARE_SHORTEN_URL_PLAY, contentGooruOid, getLoggedInSessionToken(),collectionShareUrl);
	    jsonResponseRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
	    jsonRep = jsonResponseRep.getJsonRepresentation();
		shortenUrl=shareDeSerializer.deserializeShortenUrlFromJson(jsonRep);
		try {
			collectionShareUrl=URLDecoder.decode(collectionShareUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		shareUrls.put("sharebitlyurl", shortenUrl);
		shareUrls.put("shareurl", collectionShareUrl);
		return shareUrls;
	}

	@Override
	public String updateViewCount(String gooruid, String viewCount,String resourceType) {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.UPDATE_VIEW_COUNT,gooruid, getLoggedInSessionToken());
		Form form=new Form();
		form.add("resourceViews", viewCount);
		form.add("sessionToken", getLoggedInSessionToken());
		ServiceProcessor.post(url, getRestUsername(), getRestPassword(),form);
		return resourceType;
	}

	@Override
	public String startActivityPlayerLog(String activityEventId,String activityParentEventId, String eventName, String gooruOid,
			String resourceGooruOid, String context, String userAgent) {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.START_ACTIVITY_LOG,activityEventId, getLoggedInSessionToken());
		
		Form form=new Form();
		form.add("sessionToken", getLoggedInSessionToken());
		form.add("contentGooruOid", resourceGooruOid);
		form.add("parentGooruOid", gooruOid);
		form.add("context", getLoggedInSessionToken());
		form.add("parentEventId", activityParentEventId);
		form.add("userAgent", userAgent);
		form.add("eventName", eventName);
		
		ServiceProcessor.post(url, getRestUsername(), getRestPassword(),form);
		return activityEventId;
	}

	@Override
	public String stopActivityPlayerLog(String activityEventId,String activityParentEventId, String eventName, String gooruOid,
			String resourceGooruOid, String context, String userAgent) {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.STOP_ACTIVITY_LOG,activityEventId, getLoggedInSessionToken());
		
		Form form=new Form();
		form.add("sessionToken", getLoggedInSessionToken());
		form.add("contentGooruOid", resourceGooruOid);
		form.add("parentGooruOid", gooruOid);
		form.add("context", getLoggedInSessionToken());
		form.add("parentEventId", activityParentEventId);
		form.add("userAgent", userAgent);
		form.add("eventName", eventName);
		
		ServiceProcessor.post(url, getRestUsername(), getRestPassword(),form);
		return activityEventId;
	}
	
	public String createSessionTracker(String collectionGooruOid){
		String seesionId="";
		JSONObject createSessionObject=new JSONObject();
		JSONObject sessionObject=new JSONObject();
		JSONObject collectionObject=new JSONObject();
		JsonRepresentation jsonRepresentation = null;
		try {
			collectionObject.put("gooruOid", collectionGooruOid);
			sessionObject.put("resource", collectionObject);
			sessionObject.put("mode", "test");
			createSessionObject.put("session", sessionObject);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_SESSION, getLoggedInSessionToken());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(),createSessionObject.toString());
			jsonRepresentation=jsonResponseRep.getJsonRepresentation();
			JSONObject createSessionResponse=jsonRepresentation.getJsonObject();
			seesionId=createSessionResponse.getString("sessionId");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return seesionId;
	}
	

	public String updateSessionInCollection(String sessionTrackerId) {
		String sessionItemId="";
		JSONObject updateSessionObject=new JSONObject();
		JSONObject sessionStatus=new JSONObject();
		JsonRepresentation jsonRepresentation = null;
		try {
			sessionStatus.put("status", "archive");
			updateSessionObject.put("session",sessionStatus);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.UPDATE_SESSION, sessionTrackerId,getLoggedInSessionToken());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.put(url, getRestUsername(), getRestPassword(),updateSessionObject.toString());
			jsonRepresentation=jsonResponseRep.getJsonRepresentation();
			if(jsonRepresentation !=null){
				JSONObject createSessionResponse=jsonRepresentation.getJsonObject();
				sessionItemId=createSessionResponse.getString("sessionId");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sessionItemId;
	}

	public void getSessionInCollection(String sessionTrackerId) {
		
	}

	public String createSessionItemInCollection(String sessionTrackerId,String collectionItemId, String resourceGooruOid) {
		String sessionItemId="";
		JSONObject resource=new JSONObject();
		JSONObject collectionItem=new JSONObject();
		JSONObject sessionItem=new JSONObject();
		JSONObject sessionItemObj=new JSONObject();
		JsonRepresentation jsonRepresentation = null;
		try {
			resource.put("gooruOid",resourceGooruOid);
			collectionItem.put("collectionItemId",collectionItemId);
			sessionItem.put("resource",resource);
			sessionItem.put("collectionItem",collectionItem);
			sessionItemObj.put("sessionItem",sessionItem);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_SESSION_ITEM, sessionTrackerId,getLoggedInSessionToken());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(),sessionItemObj.toString());
			jsonRepresentation=jsonResponseRep.getJsonRepresentation();
			JSONObject createSessionResponse=jsonRepresentation.getJsonObject();
			if(createSessionResponse.has("sessionItemId")){
				sessionItemId=createSessionResponse.getString("sessionItemId");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sessionItemId;
	}

	public String createSessionItemAttemptTry(String sessionTrackerId,String sessionItemTrackerId, Integer answerId, String attemptResult) {
		JSONObject sessionItemAttemptTry=new JSONObject();
		JSONObject assessmentAnswer=new JSONObject();
		JSONObject jsonanswerId=new JSONObject();
		try {
			jsonanswerId.put("answerId",answerId);
			assessmentAnswer.put("assessmentAnswer",jsonanswerId);
			assessmentAnswer.put("attemptItemTryStatus",attemptResult);
			sessionItemAttemptTry.put("sessionItemAttemptTry",assessmentAnswer);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_SESSION_ITEM_ATTEMPT, sessionTrackerId,sessionItemTrackerId,getLoggedInSessionToken());
			ServiceProcessor.post(url, getRestUsername(), getRestPassword(),sessionItemAttemptTry.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public String createSessionItemAttemptTryForOe(String sessionTrackerId,String sessionItemTrackerId,String answerId,String attemptStatus,String attemptAnswerResult) {
		JSONObject sessionItemAttemptTry=new JSONObject();
		JSONObject assessmentAnswer=new JSONObject();
		//JSONObject jsonanswerId=new JSONObject();
		try {
			//jsonanswerId.put("answerId",answerId);
			//assessmentAnswer.put("assessmentAnswer",jsonanswerId);
			assessmentAnswer.put("attemptItemTryStatus",attemptStatus);
			assessmentAnswer.put("answerText",attemptAnswerResult);
			sessionItemAttemptTry.put("sessionItemAttemptTry",assessmentAnswer);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_SESSION_ITEM_ATTEMPT, sessionTrackerId,sessionItemTrackerId,getLoggedInSessionToken());
			ServiceProcessor.post(url, getRestUsername(), getRestPassword(),sessionItemAttemptTry.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String sendEmail(String fromEmail, String toEmail, String copyEmail,String subject, String message) {
		JSONObject mailJsonObject=new JSONObject();
		try {
			
			String completeLogoDetails = "<!DOCTYPE html> <html> <head> <meta http-equiv=" +
					"Content-Type" +
					" content=" +
					"text/html; charset=UTF-8" +
					" /> </head> <body style='font-family: arial, sans-serif;color: #515151;height:375px;font-size: 12px; background-color: #f0f0f0;'> <img id='logo-header' src='http://sfs.goorulearning.org/media/mail/v1/images/gooru-logo-small.png' style='width:100px;height:30px;margin: 30px auto 10px auto; margin-left: 385px;'" +
					"></img> <div class='content-block img-desc' style='width: 500px;padding: 35px;margin: 0px auto 30px auto;background-color: white;border: 1px solid #DDD;-moz-box-shadow: 0 0 10px rgba(0,0,0,.1); -webkit-box-shadow: 0 0 10px rgba(0,0,0,.1);box-shadow: 0 0 10px rgba(0,0,0,.1);'> <div style='font-family: arial;width: 520px;color:#666;height:150px'>"+ message + 
					"</body> </html>";
			
			mailJsonObject.put("fromDisplayName", fromEmail);
			mailJsonObject.put("to", toEmail);
			mailJsonObject.put("cfm", copyEmail);
			mailJsonObject.put("subject", subject);
			mailJsonObject.put("message", completeLogoDetails);
			
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SOCIAL_EMAIL,getLoggedInSessionToken());
			ServiceProcessor.post(url, getRestUsername(), getRestPassword(),mailJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean getUserProfileVisibility(String gooruUid){
		boolean userProfileVisibility=true;
		JsonRepresentation jsonRep =null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_USER_PROFILE,gooruUid,getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		return userProfileVisibility;
	}
	@Override
	public String copyCollection(String collectionId, String collectionTitle) {
		String copiedCollectionId="";
		JsonRepresentation jsonRep =null;
//		JSONObject copyCollectionObj=new JSONObject();
//		JSONObject collection=new JSONObject();
		try {
//			collection.put("collectionType", "collection");
//			collection.put("title", collectionTitle);
//			copyCollectionObj.put("collection", collection);
//			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_COPY_COLLECTION, collectionId,getLoggedInSessionToken());
//			JsonRepresentation jsonRep=ServiceProcessor.put(url, getRestUsername(), getRestPassword(),copyCollectionObj.toString());
//			if(jsonRep!=null){
//				JSONObject copiedCollectionObj=new JSONObject(jsonRep);
//				copiedCollectionId=copiedCollectionObj.getString("gooruOid");
//			}
			collectionTitle = collectionTitle.trim();
			collectionTitle=URLEncoder.encode(collectionTitle);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.COPY_RENAME_COLLECTION, collectionId,getLoggedInSessionToken(),"true",collectionTitle);
			JsonResponseRepresentation jsonResponseRep=ServiceProcessor.put(url, getRestUsername(), getRestPassword(),new Form());
			jsonRep=jsonResponseRep.getJsonRepresentation();
			if(jsonRep!=null){
				JSONObject copiedCollectionObj=jsonRep.getJsonObject();
				copiedCollectionId=copiedCollectionObj.getString("gooruOid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return copiedCollectionId;
	}

	@Override
	public String copyCollectionItem(String collectionItemId, String collectionId) {
		String copiedCollectionItemId="";
		try {
			JsonRepresentation jsonRep =null;
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.COPY_RESOURCCE,getLoggedInSessionToken());
			Form form=new Form();
			form.add("sessionToken", getLoggedInSessionToken());
			form.add("resourceId",collectionItemId);
			form.add("collectionId",collectionId);
			form.add("data","{\"collectionItem\":{\"itemType\":\"subscribed\"}}");
			JsonResponseRepresentation jsonResponseRep=ServiceProcessor.post(url, getRestUsername(), getRestPassword(),form);
			jsonRep = jsonResponseRep.getJsonRepresentation();
			if(jsonRep!=null){
				JSONObject copiedCollectionObj=jsonRep.getJsonObject();
				//copiedCollectionItemId=copiedCollectionObj.getString("collectionItemId");
			}
			
//			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_COPY_COLLECTION_ITEM,collectionItemId, collectionId,getLoggedInSessionToken());
//			JsonRepresentation jsonRep=ServiceProcessor.put(url, getRestUsername(), getRestPassword());
//			if(jsonRep!=null){
//				JSONObject copiedCollectionObj=jsonRep.get3();
//				copiedCollectionItemId=copiedCollectionObj.getString("collectionItemId");
//			}
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return copiedCollectionItemId;
	}
	
	@Override
	public ArrayList<CollectionItemsList> getWorkspaceCollections(String userId,String offset,String limit) {
		    JsonRepresentation jsonRep =null;
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_USER_WORKSPACE,getLoggedInSessionToken(),offset,limit);
			JsonResponseRepresentation jsonResponseRep=ServiceProcessor.get(url, getRestUsername(), getRestPassword());
			jsonRep= jsonResponseRep.getJsonRepresentation();
			return deserializeUserCollections(jsonRep);
	}
	
	
	public ArrayList<CollectionItemsList> deserializeUserCollections(JsonRepresentation jsonRep){
		ArrayList<CollectionItemsList> resourceModelList=new ArrayList<CollectionItemsList>();
		try {
			if(jsonRep!=null){
				JSONArray myCollectionArryObj=jsonRep.getJsonArray();
				for(int i=0;i<myCollectionArryObj.length();i++){
					JSONObject myCollectionObj=myCollectionArryObj.getJSONObject(i);
					String myCollectionTitle=myCollectionObj.isNull("title")?"":myCollectionObj.getString("title").toString();
					String mycollectionGid=myCollectionObj.isNull("gooruOid")?"":myCollectionObj.getString("gooruOid").toString();
					String myCollectionType=myCollectionObj.isNull("collectionType")?"":myCollectionObj.getString("collectionType").toString();
					int collectionItemsListSize = 0;
					if(!myCollectionObj.isNull("collectionItems")){
						JSONArray collectionInfo=myCollectionObj.getJSONArray("collectionItems");
						collectionItemsListSize = collectionInfo.length();
					}
					CollectionItemsList collectionDetails=new CollectionItemsList();
					collectionDetails.setCollectionTitle(myCollectionTitle);
					collectionDetails.setCollectionId(mycollectionGid);
					collectionDetails.setCollectionType(myCollectionType);
					collectionDetails.setCollectionItemsListSize(collectionItemsListSize);
					resourceModelList.add(collectionDetails);
				}//main for loop
			}			
		}//try end
		catch (JSONException e) {
			e.printStackTrace();
		}
		return resourceModelList;
	}
	@Override
	public String updateContentThumbsRating(String resourceGooruOid,int userThumbsRataing) {
		JSONObject scoreObject=new JSONObject();
		try {
			scoreObject.put("score",userThumbsRataing);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CONTENT_THUMBS_RATING,resourceGooruOid,getLoggedInSessionToken());
			Form form=new Form();
			form.add("score",""+userThumbsRataing);
			form.add("sessionToken",getLoggedInSessionToken());
			//ServiceProcessor.put(url, getRestUsername(), getRestPassword(),scoreObject.toString());
			ServiceProcessor.post(url, getRestUsername(), getRestPassword(),form);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<ContentReportDo> getContentReport(String associatedGooruOid,String gooruUid){
		JsonRepresentation jsonRep = null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_CONTENT_REPORT,associatedGooruOid,getLoggedInSessionToken(),gooruUid);
		JsonResponseRepresentation jsonResponseRep=ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep = jsonResponseRep.getJsonRepresentation();
		
		return deserializeGetContentReport(jsonRep);
	}
	
	public ArrayList<ContentReportDo> deserializeGetContentReport(JsonRepresentation jsonRep){
		ArrayList<ContentReportDo> reactionsList=new ArrayList<ContentReportDo>();
		try {
			JSONArray jsonArray=jsonRep.getJsonArray();
			/*JSONObject getContentReportObj = new JSONObject();
			getContentReportObj=jsonRep.getJsonObject();
	
			JSONArray jsonArray=getContentReportObj.getJSONArray("searchResults");	*/	
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				ContentReportDo contentReportDo =deserializContentReport(jsonObject);
				reactionsList.add(contentReportDo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reactionsList;
	}
	
	public ContentReportDo createContentReport(String associatedGooruOid,String freeText,ArrayList<String> contentReportList,String deleteContentReportGooruOids){
		JsonRepresentation jsonRep=null;
		JSONObject jsonObject=null;
		try{
			if(deleteContentReportGooruOids!=null&&!deleteContentReportGooruOids.isEmpty()){
				deleteContentReport(deleteContentReportGooruOids);
			}
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_CONTENT_REPORT,getLoggedInSessionToken());
			JSONObject contentReportObject=new JSONObject();
			JSONArray contentReportJsonArray=new JSONArray(); 
			contentReportObject.put("assocGooruOid", associatedGooruOid);
			contentReportObject.put("freeText", freeText);
			contentReportObject.put("target", new JSONObject().put("value", "content"));
			if(contentReportList!=null){
				for(int i=0;i<contentReportList.size();i++){
					contentReportJsonArray.put(new JSONObject().put("value", contentReportList.get(i)));
				}
			}
			contentReportObject.put("types", contentReportJsonArray);
			JsonResponseRepresentation jsonResponseRep=ServiceProcessor.post(url, getRestUsername(), getRestPassword(),contentReportObject.toString());
			jsonRep = jsonResponseRep.getJsonRepresentation();
			//jsonObject=jsonRep.getJsonObject();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		//return deserializContentReport(jsonRep);
		return null;
	}
	
	@Override
	public ReactionDo createReaction(String resourceId, String reactionText,String gooruReactionId,String collectionId, String createStudyPlayerReaction) {
		JsonRepresentation jsonRep=null;
		JSONObject jsonObject=null;
		try {
			if(gooruReactionId!=null&&!gooruReactionId.isEmpty()){
				deleteContentReaction(gooruReactionId);
			}
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.CREATE_RECTION, getLoggedInSessionToken());
			JSONObject createReactionJsonObj = new JSONObject();
			JSONObject createContextJsonObj = new JSONObject();
			createReactionJsonObj.put("assocGooruOid", resourceId);
			createReactionJsonObj.put("target", new JSONObject().put("value","content")); 
			if(reactionText!=null){
				createReactionJsonObj.put("type", new JSONObject().put("value",reactionText)); 
			}
			createContextJsonObj.put("collectionGooruId", collectionId);
			createContextJsonObj.put("resourceGooruId", resourceId);
			createContextJsonObj.put("eventName", createStudyPlayerReaction);
			createReactionJsonObj.put("context",createContextJsonObj.toString());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.post(url, getRestUsername(), getRestPassword(),createReactionJsonObj.toString());
			jsonRep = jsonResponseRep.getJsonRepresentation();
			jsonObject=jsonRep.getJsonObject();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deserializeResourceReaction(jsonObject); 
	}
	
	public void deleteContentReaction(String associatedGooruOid){
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.DELETE_CONTENT_REACTION,associatedGooruOid,getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}
	
	@Override
	public ArrayList<ReactionDo> getResourceReaction(String associatedGooruOid, String gooruUid) {
		JsonRepresentation jsonRep=null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_CONTENT_REACTION,associatedGooruOid,getLoggedInSessionToken(),gooruUid);
		JsonResponseRepresentation jsonResponseRep=ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRep=jsonResponseRep.getJsonRepresentation();
		return deserializeGetReaction(jsonRep); 
	}
	
	
	
	public ArrayList<ReactionDo> deserializeGetReaction(JsonRepresentation jsonRep){
		ArrayList<ReactionDo> reactionsList=new ArrayList<ReactionDo>();
		try {
			JSONArray jsonArray=jsonRep.getJsonArray();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				ReactionDo reactionDo =deserializeResourceReaction(jsonObject);
				reactionsList.add(reactionDo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reactionsList;
	}
	private ReactionDo deserializeResourceReaction(JSONObject jsonObject) { 
		ReactionDo reactionDo = new  ReactionDo(); 
		String gooruReactionId="";
		try {
			reactionDo.setAssocGooruOid(jsonObject.isNull("assocGooruOid")?"":jsonObject.getString("assocGooruOid"));
			reactionDo.setReactionText(jsonObject.getJSONObject("type").getString("value")); 
			reactionDo.setDeleteReactionGooruOid(jsonObject.isNull("gooruOid")?"":jsonObject.getString("gooruOid"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reactionDo;
	}

	public void deleteContentReport(String associatedGooruOid){
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.DELETE_CONTENT_REPORT,associatedGooruOid,getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}
	
	protected ContentReportDo deserializContentReport(JSONObject jsonObject){
		ContentReportDo contentReportDo=new ContentReportDo();
		ArrayList<String> contentReportList=new ArrayList<String>();
		String gooruContentId="";
		
		try { 
			String contentReportText=jsonObject.getJSONObject("type").getString("value");
			contentReportList.add(contentReportText);
			contentReportDo.setFreeText(jsonObject.isNull("freeText")?"":jsonObject.getString("freeText"));
			contentReportDo.setAssocGooruOid(jsonObject.isNull("assocGooruOid")?"":jsonObject.getString("assocGooruOid"));
			contentReportDo.setDeleteContentGooruOid(jsonObject.isNull("gooruOid")?"":jsonObject.getString("gooruOid"));
			
			//gooruContentId=gooruContentId+(jsonObject.isNull("gooruOid")?"":jsonObject.getString("gooruOid"));

			//if(jsonRep!=null){
				//JSONArray contnetJsonArray=jsonRep.getJsonArray(); 
				/*JSONObject contnetJsonArray=jsonRep.getJsonObject();
				for(int i=0;i<contnetJsonArray.length();i++){ 
					JSONObject jsonObject=contnetJsonArray.getJSONObject(i);
					String contentReportText=jsonObject.getJSONObject("type").getString("value");
					contentReportList.add(contentReportText);
					contentReportDo.setFreeText(jsonObject.isNull("freeText")?"":jsonObject.getString("freeText"));
					contentReportDo.setAssocGooruOid(jsonObject.isNull("assocGooruOid")?"":jsonObject.getString("assocGooruOid"));
					gooruContentId=gooruContentId+(jsonObject.isNull("gooruOid")?"":jsonObject.getString("gooruOid"));
					if(contnetJsonArray.length()!=(i+1)){
						gooruContentId=gooruContentId+",";
					}
				}*/
				//contentReportDo.setContentReportList(contentReportList);
				//contentReportDo.setDeleteContentGooruOid(gooruContentId);
			//}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return contentReportDo;
	}
	
	public CommentsDo createCommentForCollection(String gooruCollectionId, String userCommentsEntered){
		JsonRepresentation jsonRep=null;
		try{

			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_CREATE_COLLECTION_COMMENT,getLoggedInSessionToken());
			JSONObject commentObject=new JSONObject();
	
			commentObject.put("comment", userCommentsEntered);
			commentObject.put("gooruOid", gooruCollectionId);

			JsonResponseRepresentation jsonResponseRep=ServiceProcessor.post(url, getRestUsername(), getRestPassword(),commentObject.toString());
			jsonRep =jsonResponseRep.getJsonRepresentation();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return deserializCommentsObject(jsonRep);
	}
	
	protected CommentsDo deserializCommentsObject(JsonRepresentation jsonRep){
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				CommentsDo commentsDoObj = JsonDeserializer.deserialize(
						jsonRep.getJsonObject().toString(), CommentsDo.class);
				return commentsDoObj;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CommentsDo();
	}
	
	public CommentsListDo getCollectionCommentsList(String gooruOid,String offset,String pageLimit) {
		JsonRepresentation jsonRep= null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GET_COLLECTION_COMMENTS, getLoggedInSessionToken(),gooruOid,offset,pageLimit);
		JsonResponseRepresentation jsonResponseRep = ServiceProcessor.get(url, getSearchUsername(), getSearchPassword());
		jsonRep =jsonResponseRep.getJsonRepresentation();
		return deserializeCollectionComments(jsonRep);
	}
	
	public CommentsListDo deserializeCollectionComments(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				return JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CommentsListDo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CommentsListDo();
	}
	
	public void deleteCollectionCommentbyCommentUid(String commentUid){
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_DELETE_COLLECTION_COMMENT,commentUid,getLoggedInSessionToken());
		ServiceProcessor.delete(url, getRestUsername(), getRestPassword());
	}
	
	public CommentsDo updateCollectionCommentbyCommentUid(String commentUid, String commentsUpdatedByUser) {
		CommentsDo commentsDo= new CommentsDo();
		JSONObject updateSessionObject=new JSONObject();
		JSONObject statusActive = new JSONObject();
		JsonRepresentation jsonRepresentation = null;
		try {
			statusActive.put("value", "active");
			updateSessionObject.put("comment",commentsUpdatedByUser);
			updateSessionObject.put("status", statusActive);
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_UPDATE_COLLECTION_COMMENT, commentUid,getLoggedInSessionToken());
			JsonResponseRepresentation jsonResponseRep = ServiceProcessor.put(url, getRestUsername(), getRestPassword(),updateSessionObject.toString());
			jsonRepresentation =jsonResponseRep.getJsonRepresentation();
			if(jsonResponseRep.getStatusCode()==200){
				commentsDo = deserializCommentsObject(jsonRepresentation);
				commentsDo.setStatusCode(jsonResponseRep.getStatusCode());
			}else{
				commentsDo=new CommentsDo();
				commentsDo.setStatusCode(jsonResponseRep.getStatusCode());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return commentsDo;
	}
	
	@Override

	public String generatePdf(String innerHtml, String completedDateTime) {
		String pdfUrl=null;
		JSONObject generatePdfJsonObj=new JSONObject();
		StringRepresentation stringRepresentation = null;
		try {
			generatePdfJsonObj.put("html", innerHtml);
			generatePdfJsonObj.put("fileName", completedDateTime);
			String url =UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_GENERATE_PDF,getLoggedInSessionToken());
			stringRepresentation=ServiceProcessor.postString(url, getRestUsername(), getRestPassword(),generatePdfJsonObj.toString());
			pdfUrl=stringRepresentation.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfUrl;

	}
	
	@Override

	public String sendEmailWithPdf(String toAddress, String fromAddress,String cfm, String subject, String message, String url,String FileName) {

		JSONObject infoJsonObj=new JSONObject();

		JSONObject attachment=new JSONObject();

		StringRepresentation stringRepresentation = null;

		String response=null;

		try {

			infoJsonObj.put("to", toAddress);

			infoJsonObj.put("fromDisplayName", fromAddress);

			infoJsonObj.put("cfm", cfm);

			infoJsonObj.put("subject", subject);

			infoJsonObj.put("message", message);

			url= url.replaceAll("\n", "");
			
			url= url.replaceAll("\\n", "");
			
			FileName= FileName.replaceAll("\n", "");
			
			FileName= FileName.replaceAll("\\n", "");
			
			attachment.put("url",url);

			attachment.put("fileName",FileName);

			infoJsonObj.put("attachment", new JSONArray("["+attachment.toString()+"]"));

			String apiUrl =UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_SEND_EMAIL_WITH_PDF,getLoggedInSessionToken());

			stringRepresentation=ServiceProcessor.postString(apiUrl, getRestUsername(), getRestPassword(),infoJsonObj.toString()); 

			response = stringRepresentation.getText();

			if(response==null) {

				response = "success";

			} else {

				response = "failure";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see org.ednovo.gooru.client.service.PlayerAppService#getFeaturedContent()
	 */
	/**
	 * This method will send email with pdf attachment by making API call.
	 * 
	 * @param toAddress it specifies the to mail.
	 * @param fromAddress it specifies the from email.
	 * @param cfm it specifies "yes" or "no",if yes copy of mail send to from email also,if not will not send copy of message.
	 * @param subject  it specifies the subject of the email.
	 * @param message  it specifies the text message of the email.
	 * @param url it specifies the pdf url value.
	 * @param fileName it specfies the pdf file name value
	 */
	@Override
	public ArrayList<FeaturedContentDo> getFeaturedContent() {
		ArrayList<FeaturedContentDo> featuredContentDos=new ArrayList<FeaturedContentDo>();
		JsonRepresentation jsonRepresentation =null;
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_FEATURED_COLLECTIONS, getLoggedInSessionToken());
		JsonResponseRepresentation jsonResponseRep =ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		jsonRepresentation =jsonResponseRep.getJsonRepresentation();
		try{		
			JSONArray featuredContentArray=jsonRepresentation.getJsonArray();
			if(featuredContentArray.length()>0){
			 for(int i=0;i<featuredContentArray.length();i++){
				 FeaturedContentDo featuredContentDo=new FeaturedContentDo();
				 JSONObject featureJsonObject=featuredContentArray.getJSONObject(i);
				 JSONArray featureArray=featureJsonObject.getJSONArray("scollections");
				 if(featureArray.length()>0){
					 JSONObject featureContent=featureArray.getJSONObject(0);
					 featuredContentDo.setCollectionTitle(featureContent.isNull("title")?"":featureContent.getString("title"));
					 featuredContentDo.setCollectionGooruOid(featureContent.isNull("gooruOid")?"":featureContent.getString("gooruOid"));
					 JSONObject thubnailObject=featureContent.getJSONObject("thumbnails");
					 featuredContentDo.setCollectionThumbnailUrl(thubnailObject.isNull("url")?"":thubnailObject.getString("url"));
					 featuredContentDos.add(featuredContentDo);
				 }
			 }
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		return featuredContentDos;
	}

	@Override
	public void deleteReaction(String gooruReactionId) {
		deleteContentReaction(gooruReactionId);
	}
	
	private String getStandardId(String standardId) {
		String standardIdVal = "";
		if(standardId != null) {
			standardIdVal = "&rootNodeId="+standardId;
		}
		return standardIdVal;
	}

}
