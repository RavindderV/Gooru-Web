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

import java.util.List;

import org.ednovo.gooru.client.service.HomeService;
import org.ednovo.gooru.server.annotation.ServiceURL;
import org.ednovo.gooru.server.deserializer.FeaturedContentDeSerializer;
import org.ednovo.gooru.server.request.ServiceProcessor;
import org.ednovo.gooru.server.request.UrlToken;
import org.ednovo.gooru.server.serializer.JsonDeserializer;
import org.ednovo.gooru.shared.exception.GwtException;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.featured.FeaturedCollectionContentDo;
import org.json.JSONException;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.core.shared.GWT;
/**
 * @fileName : HomeServiceImpl.java
 *
 * @description : This is the implementation of the home serivice.
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
@Service("homeService")
@ServiceURL("/homeService")
public class HomeServiceImpl extends BaseServiceImpl implements HomeService {

	@Autowired
	FeaturedContentDeSerializer featuredContentDeSerializer;

	private static final long serialVersionUID = -8562571170804242471L;

//	private static final String FEATURED_COLLECTION_LIMIT = "5";
	
	/*@Override
	public List<FeaturedCollectionContentDo> getFeaturedCollections() {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.FEATURED_COLLECTION, getLoggedInSessionToken(), FEATURED_COLLECTION_LIMIT);
		JsonRepresentation jsonRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		return featuredContentDeSerializer.deSerializer(jsonRep);
	}*/
	/**
	 * This method is used to get the featured theme collections.
	 */
	@Override
	public List<FeaturedCollectionContentDo> getFeaturedThemeCollection(String themeType) {
		String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.V2_FEATURED_THEME_COLLECTIONS, themeType, getLoggedInSessionToken());
		JsonRepresentation jsonRep = ServiceProcessor.get(url, getRestUsername(), getRestPassword());
		return featuredContentDeSerializer.deSerializer(jsonRep);
	}
	/**
	 * This method is used to update the user details.
	 */
	@Override
	public void updateUserDetails(String userNameValue, String userRoleValue)throws GwtException {
		
		String url = UrlGenerator.generateUrl(getRestEndPoint(),UrlToken.UPDATE_USER,getLoggedInUserUid(),getLoggedInSessionToken());
		Form form = new Form();
		form.add("username", userNameValue);
		form.add("userrole", userRoleValue);
		JsonRepresentation jsonRep = ServiceProcessor.put(url, getRestUsername(), getRestPassword(),form);
//		try {
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
/*
	@Override
	public List<CollectionDo> getCollectionList(List<String> collectionIds) throws GwtException {
		List<CollectionDo> featuredList = new ArrayList<CollectionDo>();
		Iterator<String> iterator = collectionIds.iterator();
		while (iterator.hasNext()) {
			String url = UrlGenerator.generateUrl(getRestEndPoint(), UrlToken.GET_COLLECTION, iterator.next(), getLoggedInSessionToken(), true + "");
			featuredList.add(deserializeCollection(ServiceProcessor.get(url, getRestUsername(), getRestPassword())));
		}
		return featuredList;
	}*/
	/**
	 * @function deserializeCollection 
	 * 
	 * @created_date : 02-Jan-2014
	 * 
	 * @description : This method is used to deserialize the collection.
	 * 
	 * 
	 * @parm(s) : @param jsonRep
	 * @parm(s) : @return
	 * 
	 * @return : CollectionDo
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public CollectionDo deserializeCollection(JsonRepresentation jsonRep) {
		if (jsonRep != null && jsonRep.getSize() != -1) {
			try {
				GWT.log("jsonResp:>>"+jsonRep.toString());
				return JsonDeserializer.deserialize(jsonRep.getJsonObject().toString(), CollectionDo.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new CollectionDo();
	}
	
	public String whatsNewMosLink(){
		return getWhatsNewMosLink();
	}
	
	public String whatsNewFibLink(){
		return getWhatsNewFibLink();
	}
	
	public String mosLink(){
		return getMosLink();
	}
}
