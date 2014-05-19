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
package org.ednovo.gooru.client.mvp.profilepage;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.home.event.HeaderTabType;
import org.ednovo.gooru.client.mvp.home.event.HomeEvent;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.mvp.profilepage.ProfilePagePresenter.IsProfilePageProxy;
import org.ednovo.gooru.client.mvp.profilepage.event.RequestCollectionOpenEvent;
import org.ednovo.gooru.client.mvp.profilepage.event.RequestFolderOpenEvent;
import org.ednovo.gooru.client.mvp.profilepage.event.SetUserPublicProfileImageEvent;
import org.ednovo.gooru.client.mvp.search.event.ConfirmStatusPopupEvent;
import org.ednovo.gooru.client.mvp.search.event.SetFooterEvent;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.shelf.ShelfView;
import org.ednovo.gooru.client.service.ProfilePageServiceAsync;
import org.ednovo.gooru.shared.model.code.CodeDo;
import org.ednovo.gooru.shared.model.code.LibraryCodeDo;
import org.ednovo.gooru.shared.model.code.ProfileCodeDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.user.BiographyDo;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.model.user.ProfilePageDo;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

/**
 * @fileName : ProfilePagePresenter.java
 * 
 * @description :
 * 
 * 
 * @version : 1.0
 * 
 * @date: July 12, 2013
 * 
 * @Author Gooru Team
 * 
 * @Reviewer:
 */
public class ProfilePagePresenter extends BasePlacePresenter<IsProfilePageView, IsProfilePageProxy> implements ProfilePageUiHandlers {
	
//	private SimpleAsyncCallback<List<CollectionDo>> userCollectionAsyncCallback;

	private ImageUploadPresenter imageUploadPresenter;

	private ProfileDo profileDo;

	private Date dob;
	
	private boolean isUserUnder13=false;

	@Inject
	private ProfilePageServiceAsync profilePageService;

	private SimpleAsyncCallback<List<CollectionItemDo>> getWorkSpaceAsyncCallback;
	
	private SimpleAsyncCallback<List<CollectionItemDo>> getFolderItemsAsyncCallback;

	private SimpleAsyncCallback<BiographyDo> userProfileBiographyAsyncCallback;

	private int PARENT_FOLDER_ID = 0;
	
	private static String USER_META_ACTIVE_FLAG = "1";
	
	private String profileUrl=null;
	
	private String userId = "";

	private String callBack = "";
	
	SignUpPresenter signUpViewPresenter = null;
	
	private boolean isRefresh = false;
	
	@Inject
	public ProfilePagePresenter(IsProfilePageView view, IsProfilePageProxy proxy,ImageUploadPresenter imageUploadPresenter, SignUpPresenter signUpViewPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.signUpViewPresenter = signUpViewPresenter;
		this.imageUploadPresenter=imageUploadPresenter;
		addRegisteredHandler(RequestFolderOpenEvent.TYPE, this);
		addRegisteredHandler(RequestCollectionOpenEvent.TYPE, this);
		addRegisteredHandler(SetUserPublicProfileImageEvent.TYPE, this);
	}

	@ProxyCodeSplit
	@NameToken(PlaceTokens.PROFILE_PAGE)
	@UseGatekeeper(AppPlaceKeeper.class)
	public interface IsProfilePageProxy extends ProxyPlace<ProfilePagePresenter> {
	}

	@Override
	public String getViewToken() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	protected void onReveal() {
		AppClientFactory.setBrowserWindowTitle(SeoTokens.PROFILE_PAGE_TITLE);
		AppClientFactory.setMetaDataDescription(SeoTokens.HOME_META_DESCRIPTION);
		userId = AppClientFactory.getPlaceManager().getRequestParameter("id");
		AppClientFactory.fireEvent(new ConfirmStatusPopupEvent(false));
		callBack = "reveal";
		createProfileUserData();
	}

	@Override
	protected void onReset() {
		if(AppClientFactory.getPlaceManager().refreshPlace()) {
			String userResetId = AppClientFactory.getPlaceManager().getRequestParameter("id");
			String folderId = AppClientFactory.getPlaceManager().getRequestParameter("folderid");
			if(folderId!=null) {
				callBack = "reset";
			}
			if(userResetId != userId) {
				callBack = "reveal";
				userId = userResetId;
				createProfileUserData();
			}
			getUserWorkSpace();
			if (getPlaceManager().getRequestParameter("callback") != null && getPlaceManager().getRequestParameter("callback").equalsIgnoreCase("signup")) {
				//To show SignUp (Registration popup)
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = getPlaceManager().getRequestParameter("type") ;
				int displayScreen =getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}
	}
	
	protected void getUserWorkSpace() {
		String folderId = AppClientFactory.getPlaceManager().getRequestParameter("folderid");
		getView().setContentTabVisibility(true);
		if(folderId!=null&&!isRefresh) {
			isRefresh = true;
			getProfilePageService().getUserWorkSpace(userId, getFolderItemsAsyncCallback());
		}
		if(folderId!=null) {
			getProfilePageService().getFolders(folderId, getGetWorkSpaceAsyncCallback());
		} else {
			getProfilePageService().getUserWorkSpace(userId, getGetWorkSpaceAsyncCallback());
		}
		
		
	}
	
	@Override
	protected void onUnbind() {
		Window.enableScrolling(true);
		AppClientFactory.fireEvent(new SetHeaderZIndexEvent(0, true));
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().closeAllOpenedPopUp();
		imageUploadPresenter.getView().closeImageUploadWidget();
	}
	private void createProfileUserData() {
		getView().showProfileView(true);
		if(userId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId())) {
			AppClientFactory.fireEvent(new HomeEvent(HeaderTabType.USERNAME));
		}
		AppClientFactory.getInjector().getUserService().getUserPublicProfilePage(userId,new SimpleAsyncCallback<ProfilePageDo>(){
			@Override
			public void onSuccess(ProfilePageDo result) {
				final String profileOptionvalue=result.getOptionalValue();
				AppClientFactory.getInjector().getUserService().getUserProfileV2Details(userId, USER_META_ACTIVE_FLAG, new SimpleAsyncCallback<ProfileDo>(){
					@Override
					public void onSuccess(ProfileDo result) {
						if(result!=null){
							profileDo = result;
							AppClientFactory.setBrowserWindowTitle(SeoTokens.PROFILE_PAGE_TITLE + profileDo.getUser().getUsernameDisplay());
							getView().setProfileData(profileDo);
							
							/** New Library Layout Data **/
							getView().getContentView().setData("");
							
							if(userId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId())&&!isChildUser(result)) {
								getView().enableEditableData(profileOptionvalue);
								getView().getChilNoShareOption().setVisible(false);
							} else if(userId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId())&&isChildUser(result)){
								getView().disableChildData();
								getView().getChilNoShareOption().setVisible(true);
							}
						}
					}
				});
				
				//User Editing their own profile page.
				if (userId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId())){
					getView().getOffProfileContainer().setVisible(false);
					getView().getOnProfileContainer().setVisible(true);
					getView().editOptions(true);
				}else{ //User trying to access somebody PPP.
					if (profileOptionvalue.equalsIgnoreCase("true")){	// and PPP is enabled.
						getView().getOffProfileContainer().setVisible(false);
						getView().getOnProfileContainer().setVisible(true);
						getView().editOptions(false);
						getView().getChilNoShareOption().setVisible(false);
					}else{	// and PPP is not enabled.
						getView().getOffProfileContainer().setVisible(true);
						getView().getOnProfileContainer().setVisible(false);
						getView().editOptions(false);
						getView().getChilNoShareOption().setVisible(false);
					}
				}
			}
		});
		AppClientFactory.fireEvent(new SetFooterEvent(AppClientFactory.getPlaceManager().getCurrentPlaceRequest().getNameToken()));
		getProfilePageService().profileVisitEvent(userId, new SimpleAsyncCallback<Void>(){
			@Override
			public void onSuccess(Void result) {
			}
		});
	}
	
	private boolean isChildUser(ProfileDo profileDo) {
		dob = profileDo.getDateOfBirth();
		if (profileDo.getUser().getLoginType().equalsIgnoreCase("credential")) {
			if (dob != null) {
				int age = getAge(dob);
				if (age < 13) {
					setUserUnder13(true);
				} else {
					setUserUnder13(false);
				}
			} else if (profileDo.getUser().getAccountTypeId()!=null){
				//All child accounts have account type id is set to 2.
				//Based on this will not show PPP for child account., but we need to look when child trun >13.
				if (profileDo.getUser().getAccountTypeId()==2){
					setUserUnder13(true);
				}else{
					setUserUnder13(false);
				}
			} else {
				setUserUnder13(false);
			}
		} else {
			setUserUnder13(false);
		}
		return isUserUnder13();
	}
	
	private int getAge(Date birthDate) {
		long ageInMillis = new Date().getTime() - birthDate.getTime();
		Date age = new Date(ageInMillis);
		return age.getYear() - 70;
	}

	@Override
	public void setShareView() {
		String id=AppClientFactory.getPlaceManager().getRequestParameter("id");
		String user=AppClientFactory.getPlaceManager().getRequestParameter("user");
		profileUrl = PlaceTokens.PROFILE_PAGE+"&id="+id+"&user="+user;
		AppClientFactory.getInjector().getClasspageService().ShotenUrl(profileUrl, new SimpleAsyncCallback<List<String>>() {
			/*@Override
			public void onSuccess(Sting shortenUrl) {
				getVie)w().setShareData(profileDo, shortenUrl, profileUrl);
			}*/

			@Override
			public void onSuccess(List<String> shortenUrl) {
				getView().setShareData(profileDo, shortenUrl, profileUrl);
			}
		});
	}

	@Override
	public void onBind() {
		super.onBind();
		
		setUserProfileBiographyAsyncCallback(new SimpleAsyncCallback<BiographyDo>() {

			@Override
			public void onSuccess(BiographyDo result) {
				if(result.getAboutMe()!=null){
					//getView().getProfileBiographyEditUC().setText(
						//result.getAboutMe());
					getView().getProfileDo().setAboutMe(result.getAboutMe());
				}
			}
		});

		setGetWorkSpaceAsyncCallback(new SimpleAsyncCallback<List<CollectionItemDo>>() {
			@Override
			public void onSuccess(List<CollectionItemDo> collectionItemDo) {
				getView().clearContentItemData();
				if(collectionItemDo.size()>0) {
					String folderId = AppClientFactory.getPlaceManager().getRequestParameter("folderid");
					if(folderId!=null) {
						getView().setMetaData(collectionItemDo.get(PARENT_FOLDER_ID));
					}
				}
				getView().setContentItemData(collectionItemDo);
				if(callBack.equalsIgnoreCase("reveal")) {
					/*new Library Layout data */
				}
			}
		});

		setFolderItemsAsyncCallback(new SimpleAsyncCallback<List<CollectionItemDo>>() {
			@Override
			public void onSuccess(List<CollectionItemDo> collectionItemDo) {
				/*new Library Layout data */
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	}

	@Override
	public void requestFolderView(String collectionId, Map<String, String> params) {
		AppClientFactory.getPlaceManager().revealPlace(PlaceTokens.PROFILE_PAGE, params);
	}

	@Override
	public void updateProfileVisibilitySetting(String optionalValue) {
		AppClientFactory.getInjector().getUserService().updateUserProfileVisibility(
			AppClientFactory.getLoggedInUser().getGooruUId(), optionalValue, new SimpleAsyncCallback<ProfilePageDo>() {
				@Override
				public void onSuccess(ProfilePageDo result) {
					
				}
		});
	}

	/** 
	 * This method is to get the profilePageService
	 */
	public ProfilePageServiceAsync getProfilePageService() {
		return profilePageService;
	}

	/** 
	 * This method is to set the profilePageService
	 */
	public void setProfilePageService(ProfilePageServiceAsync profilePageService) {
		this.profilePageService = profilePageService;
	}

	/**
	 * @return the getGetWorkSpaceAsyncCallback
	 */
	public SimpleAsyncCallback<List<CollectionItemDo>> getGetWorkSpaceAsyncCallback() {
		return getWorkSpaceAsyncCallback;
	}

	/**
	 * @param getWorkSpaceAsyncCallback
	 *            the getWorkSpaceAsyncCallback to set
	 */
	public void setGetWorkSpaceAsyncCallback(
			SimpleAsyncCallback<List<CollectionItemDo>> getWorkSpaceAsyncCallback) {
		this.getWorkSpaceAsyncCallback = getWorkSpaceAsyncCallback;
	}

	/**
	 * @return the getGetWorkSpaceAsyncCallback
	 */
	public SimpleAsyncCallback<List<CollectionItemDo>> getFolderItemsAsyncCallback() {
		return getFolderItemsAsyncCallback;
	}

	/**
	 * @param getWorkSpaceAsyncCallback
	 *            the getWorkSpaceAsyncCallback to set
	 */
	public void setFolderItemsAsyncCallback(
			SimpleAsyncCallback<List<CollectionItemDo>> getFolderItemsAsyncCallback) {
		this.getFolderItemsAsyncCallback = getFolderItemsAsyncCallback;
	}
	
	public SimpleAsyncCallback<BiographyDo> getUserProfileBiographyAsyncCallback() {
		return userProfileBiographyAsyncCallback;
	}

	public void setUserProfileBiographyAsyncCallback(
			SimpleAsyncCallback<BiographyDo> userProfileBiographyAsyncCallback) {
		this.userProfileBiographyAsyncCallback = userProfileBiographyAsyncCallback;
	}

	@Override
	public void requestCollectionView(CollectionItemDo collectionItemDo,
			Map<String, String> params) {
		getView().clearContentItemData();
		getView().setCollectionData(collectionItemDo);
	}

	@Override
	public void showUploadImageWidget() {
		//FIXME under age 13 
		imageUploadPresenter.showUploadTypeWidgets(false);
		addToPopupSlot(imageUploadPresenter);
		imageUploadPresenter.setPublicProfileImage(true);
		imageUploadPresenter.setCollectionImage(false);
		imageUploadPresenter.setEditResourceImage(false);
	}

	@Override
	public void setUserProfileImage(String imageUrl) {
		getView().setUserProfileImage(imageUrl);
	}

	@Override
	public void updateUserBiography(String gooruUid, String bioText, String userType,
			String firstName, String lastName, String gender) {
		AppClientFactory.getInjector().getUserService().updateProfileBiography(
				AppClientFactory.getPlaceManager().getRequestParameter(
						gooruUid), bioText, userType, firstName, lastName,
						gender, getUserProfileBiographyAsyncCallback());
	}

	@Override
	public void addCourse(Set<ProfileCodeDo> profileCodeDo) {
		AppClientFactory.getInjector().getProfilePageService().addCourseUserProfile(profileCodeDo, "profilePage", new SimpleAsyncCallback<Void>(){
			@Override
			public void onSuccess(Void result) {
				
			}
		});
	}

	@Override
	public void deleteCourse(CodeDo codeDo) {
		AppClientFactory.getInjector().getProfilePageService().deleteCourseUserProfile(codeDo, "profilePage", new SimpleAsyncCallback<Void>(){
			@Override
			public void onSuccess(Void result) {
				
			}
		});
	}

	public boolean isUserUnder13() {
		return isUserUnder13;
	}

	public void setUserUnder13(boolean isUserUnder13) {
		this.isUserUnder13 = isUserUnder13;
	}

	@Override
	public void getTaxonomyData() {
		final String userId = AppClientFactory.getPlaceManager().getRequestParameter("id");
		if(userId.equalsIgnoreCase(AppClientFactory.getLoggedInUser().getGooruUId())) {
			AppClientFactory.getInjector().getTaxonomyService().getCourse(new SimpleAsyncCallback<List<LibraryCodeDo>>() {
				@Override
				public void onSuccess(List<LibraryCodeDo> result) {
					getView().setCourseList(result);
				}
			});
		}
	}
}
