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

package org.ednovo.gooru.client.gin;

import org.ednovo.gooru.client.AppPlaceKeeper;
import org.ednovo.gooru.client.AppRootPresenter;
import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.mvp.authentication.IsSignUpView;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpView;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.IsAfterThirteen;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.IsSignUpCompleteProfile;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpAfterThirteenPresenter;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpCompleteProfilePresenter;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpCompleteProfileView;
import org.ednovo.gooru.client.mvp.authentication.afterthirteen.SignUpTurnsAfterThirteenView;
import org.ednovo.gooru.client.mvp.classpages.ClasspagePresenter;
import org.ednovo.gooru.client.mvp.classpages.ClasspagePresenter.IsClasspageProxy;
import org.ednovo.gooru.client.mvp.classpages.ClasspageView;
import org.ednovo.gooru.client.mvp.classpages.IsClasspageView;
import org.ednovo.gooru.client.mvp.classpages.assignments.AddAssignmentContainerPresenter;
import org.ednovo.gooru.client.mvp.classpages.assignments.AddAssignmentContainerView;
import org.ednovo.gooru.client.mvp.classpages.assignments.IsAddAssignmentContainerView;
import org.ednovo.gooru.client.mvp.classpages.classlist.ClassListPresenter;
import org.ednovo.gooru.client.mvp.classpages.classlist.ClassListView;
import org.ednovo.gooru.client.mvp.classpages.classlist.IsClassListView;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspagePresenter;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspagePresenter.IsEditClasspageProxy;
import org.ednovo.gooru.client.mvp.classpages.edit.EditClasspageView;
import org.ednovo.gooru.client.mvp.classpages.edit.IsEditClasspageView;
import org.ednovo.gooru.client.mvp.classpages.home.ClassHomePresenter;
import org.ednovo.gooru.client.mvp.classpages.home.ClassHomePresenter.IsClassHomeProxy;
import org.ednovo.gooru.client.mvp.classpages.home.ClassHomeView;
import org.ednovo.gooru.client.mvp.classpages.home.IsClassHomeView;
import org.ednovo.gooru.client.mvp.classpages.studentView.IsStudentAssignmentView;
import org.ednovo.gooru.client.mvp.classpages.studentView.StudentAssignmentPresenter;
import org.ednovo.gooru.client.mvp.classpages.studentView.StudentAssignmentPresenter.IsStudentAssignmentProxy;
import org.ednovo.gooru.client.mvp.classpages.studentView.StudentAssignmentView;
import org.ednovo.gooru.client.mvp.classpages.study.ClassCodePresenter;
import org.ednovo.gooru.client.mvp.classpages.study.ClassCodePresenter.IsClassCodeProxy;
import org.ednovo.gooru.client.mvp.classpages.study.ClassCodeView;
import org.ednovo.gooru.client.mvp.classpages.study.IsClassCodeView;
import org.ednovo.gooru.client.mvp.devicesupport.DeviceSupportPresenter;
import org.ednovo.gooru.client.mvp.devicesupport.DeviceSupportPresenter.IsDeviceSupportProxy;
import org.ednovo.gooru.client.mvp.devicesupport.DeviceSupportView;
import org.ednovo.gooru.client.mvp.devicesupport.IsDeviceSupportView;
import org.ednovo.gooru.client.mvp.error.ErrorPresenter;
import org.ednovo.gooru.client.mvp.error.ErrorPresenter.IsErrorProxy;
import org.ednovo.gooru.client.mvp.error.ErrorView;
import org.ednovo.gooru.client.mvp.error.IsErrorView;
import org.ednovo.gooru.client.mvp.folders.FoldersPresenter;
import org.ednovo.gooru.client.mvp.folders.FoldersPresenter.IsFoldersProxy;
import org.ednovo.gooru.client.mvp.folders.FoldersView;
import org.ednovo.gooru.client.mvp.folders.IsFoldersView;
import org.ednovo.gooru.client.mvp.folders.edit.EditFolderPresenter;
import org.ednovo.gooru.client.mvp.folders.edit.EditFolderPresenter.IsEditFolderProxy;
import org.ednovo.gooru.client.mvp.folders.edit.EditFolderView;
import org.ednovo.gooru.client.mvp.folders.edit.IsEditFolderView;
import org.ednovo.gooru.client.mvp.folders.edit.tab.content.FolderContentTabPresenter;
import org.ednovo.gooru.client.mvp.folders.edit.tab.content.FolderContentTabView;
import org.ednovo.gooru.client.mvp.folders.edit.tab.content.IsFolderContentTabView;
import org.ednovo.gooru.client.mvp.folders.edit.tab.info.FolderInfoTabPresenter;
import org.ednovo.gooru.client.mvp.folders.edit.tab.info.FolderInfoTabView;
import org.ednovo.gooru.client.mvp.folders.edit.tab.info.IsFolderInfoTabView;
import org.ednovo.gooru.client.mvp.folders.newfolder.FolderFormViewPresenter;
import org.ednovo.gooru.client.mvp.folders.newfolder.FolderFormViewPresenter.IsFolderPopUpFormProxy;
import org.ednovo.gooru.client.mvp.folders.newfolder.FolderPopUpUiBinder;
import org.ednovo.gooru.client.mvp.folders.newfolder.IsFoldersPopupView;
import org.ednovo.gooru.client.mvp.home.HomePresenter;
import org.ednovo.gooru.client.mvp.home.HomePresenter.IsHomeProxy;
import org.ednovo.gooru.client.mvp.home.HomeView;
import org.ednovo.gooru.client.mvp.home.IsHomeView;
import org.ednovo.gooru.client.mvp.home.register.IsUserRegistrationView;
import org.ednovo.gooru.client.mvp.home.register.UserRegistrationPresenter;
import org.ednovo.gooru.client.mvp.home.register.UserRegistrationView;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadPresenter;
import org.ednovo.gooru.client.mvp.image.upload.ImageUploadView;
import org.ednovo.gooru.client.mvp.image.upload.IsImageUploadView;
import org.ednovo.gooru.client.mvp.library.partner.IsPartnerLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.PartnerLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.PartnerLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.autodesk.AutodeskLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.autodesk.AutodeskLibraryPresenter.IsAutodeskLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.autodesk.AutodeskLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.autodesk.IsAutodeskLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.fincapinc.CfciLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.fincapinc.CfciLibraryPresenter.IsCfciLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.fincapinc.CfciLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.fincapinc.IsCfciView;
import org.ednovo.gooru.client.mvp.library.partner.fte.FteLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.fte.FteLibraryPresenter.IsFteLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.fte.FteLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.fte.IsFteLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.lessonopoly.IsLessonopolyLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.lessonopoly.LessonopolyLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.lessonopoly.LessonopolyLibraryPresenter.IsLessonopolyLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.lessonopoly.LessonopolyLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.ngc.IsNgcLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.ngc.NgcLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.ngc.NgcLibraryPresenter.IsNgcLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.ngc.NgcLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.onr.IsOnrLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.onr.OnrLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.onr.OnrLibraryPresenter.IsOnrLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.onr.OnrLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.psdpal.IsPsdpalView;
import org.ednovo.gooru.client.mvp.library.partner.psdpal.PsdpalLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.psdpal.PsdpalLibraryPresenter.IsPsdpalLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.psdpal.PsdpalLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.wspwh.IsWspwhLibraryView;
import org.ednovo.gooru.client.mvp.library.partner.wspwh.WspwhLibraryPresenter;
import org.ednovo.gooru.client.mvp.library.partner.wspwh.WspwhLibraryPresenter.IsWspwhLibraryProxy;
import org.ednovo.gooru.client.mvp.library.partner.wspwh.WspwhLibraryView;
import org.ednovo.gooru.client.mvp.library.rusd.IsRusdView;
import org.ednovo.gooru.client.mvp.library.rusd.RusdPresenter;
import org.ednovo.gooru.client.mvp.library.rusd.RusdPresenter.IsRusdProxy;
import org.ednovo.gooru.client.mvp.library.rusd.RusdView;
import org.ednovo.gooru.client.mvp.library.sausd.IsSausdView;
import org.ednovo.gooru.client.mvp.library.sausd.SausdPresenter;
import org.ednovo.gooru.client.mvp.library.sausd.SausdPresenter.IsSausdProxy;
import org.ednovo.gooru.client.mvp.library.sausd.SausdView;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerPresenter.IsCollectionPlayerProxy;
import org.ednovo.gooru.client.mvp.play.collection.CollectionPlayerView;
import org.ednovo.gooru.client.mvp.play.collection.IsCollectionPlayerView;
import org.ednovo.gooru.client.mvp.play.collection.add.AddCollectionPresenter;
import org.ednovo.gooru.client.mvp.play.collection.add.AddCollectionView;
import org.ednovo.gooru.client.mvp.play.collection.add.IsAddCollectionView;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.collection.body.IsCollectionPlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.collection.end.CollectionEndPresenter;
import org.ednovo.gooru.client.mvp.play.collection.end.CollectionEndView;
import org.ednovo.gooru.client.mvp.play.collection.end.IsCollectionEndView;
import org.ednovo.gooru.client.mvp.play.collection.flag.CollectionFlagPresenter;
import org.ednovo.gooru.client.mvp.play.collection.flag.CollectionFlagView;
import org.ednovo.gooru.client.mvp.play.collection.flag.IsCollectionFlagView;
import org.ednovo.gooru.client.mvp.play.collection.info.IsResourceInfoView;
import org.ednovo.gooru.client.mvp.play.collection.info.ResourceInfoPresenter;
import org.ednovo.gooru.client.mvp.play.collection.info.ResourceInfoView;
import org.ednovo.gooru.client.mvp.play.collection.preview.IsPreviewPlayerView;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter.IsPreviewPlayerProxy;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerView;
import org.ednovo.gooru.client.mvp.play.collection.preview.end.IsPreviewEndView;
import org.ednovo.gooru.client.mvp.play.collection.preview.end.PreviewEndPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.end.PreviewEndView;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.IsPreviewHomeView;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.PreviewHomePresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.home.PreviewHomeView;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.IsPreviewPlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.PreviewPlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.metadata.PreviewPlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.collection.share.CollectionSharePresenter;
import org.ednovo.gooru.client.mvp.play.collection.share.CollectionShareView;
import org.ednovo.gooru.client.mvp.play.collection.share.IsCollectionShareView;
import org.ednovo.gooru.client.mvp.play.collection.toc.CollectionPlayerTocPresenter;
import org.ednovo.gooru.client.mvp.play.collection.toc.CollectionPlayerTocView;
import org.ednovo.gooru.client.mvp.play.collection.toc.IsCollectionPlayerTocView;
import org.ednovo.gooru.client.mvp.play.resource.IsResourcePlayerView;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerPresenter;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerPresenter.IsResourcePlayerProxy;
import org.ednovo.gooru.client.mvp.play.resource.ResourcePlayerView;
import org.ednovo.gooru.client.mvp.play.resource.add.AddResourceCollectionPresenter;
import org.ednovo.gooru.client.mvp.play.resource.add.AddResourceCollectionView;
import org.ednovo.gooru.client.mvp.play.resource.add.IsAddResourceCollectionView;
import org.ednovo.gooru.client.mvp.play.resource.body.IsResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.resource.flag.IsResourceFlag;
import org.ednovo.gooru.client.mvp.play.resource.flag.ResourceFlagPresenter;
import org.ednovo.gooru.client.mvp.play.resource.flag.ResourceFlagView;
import org.ednovo.gooru.client.mvp.play.resource.narration.IsResourceNarrationView;
import org.ednovo.gooru.client.mvp.play.resource.narration.ResourceNarrationPresenter;
import org.ednovo.gooru.client.mvp.play.resource.narration.ResourceNarrationView;
import org.ednovo.gooru.client.mvp.play.resource.question.IsQuestionResourceView;
import org.ednovo.gooru.client.mvp.play.resource.question.QuestionResourcePresenter;
import org.ednovo.gooru.client.mvp.play.resource.question.QuestionResourceView;
import org.ednovo.gooru.client.mvp.play.resource.share.IsResourceShareView;
import org.ednovo.gooru.client.mvp.play.resource.share.ResourceSharePresenter;
import org.ednovo.gooru.client.mvp.play.resource.share.ResourceShareView;
import org.ednovo.gooru.client.mvp.player.CollectionPlayPresenter;
import org.ednovo.gooru.client.mvp.player.CollectionPlayPresenter.IsCollectionPlayProxy;
import org.ednovo.gooru.client.mvp.player.CollectionPlayView;
import org.ednovo.gooru.client.mvp.player.IsCollectionPlayView;
import org.ednovo.gooru.client.mvp.player.IsResourcePlayView;
import org.ednovo.gooru.client.mvp.player.ResourcePlayPresenter;
import org.ednovo.gooru.client.mvp.player.ResourcePlayPresenter.IsResourcePlayProxy;
import org.ednovo.gooru.client.mvp.player.ResourcePlayView;
import org.ednovo.gooru.client.mvp.prime.IsPrimeView;
import org.ednovo.gooru.client.mvp.prime.PrimePresenter;
import org.ednovo.gooru.client.mvp.prime.PrimePresenter.IsPrimeProxy;
import org.ednovo.gooru.client.mvp.prime.PrimeView;
import org.ednovo.gooru.client.mvp.profilepage.IsProfilePageView;
import org.ednovo.gooru.client.mvp.profilepage.ProfilePagePresenter;
import org.ednovo.gooru.client.mvp.profilepage.ProfilePagePresenter.IsProfilePageProxy;
import org.ednovo.gooru.client.mvp.profilepage.ProfilePageView;
import org.ednovo.gooru.client.mvp.profilepage.list.IsProfilePageListView;
import org.ednovo.gooru.client.mvp.profilepage.list.ProfilePageListPresenter;
import org.ednovo.gooru.client.mvp.profilepage.list.ProfilePageListView;
import org.ednovo.gooru.client.mvp.profilepage.tab.content.IsProfilePageContentTabView;
import org.ednovo.gooru.client.mvp.profilepage.tab.content.ProfilePageContentTabPresenter;
import org.ednovo.gooru.client.mvp.profilepage.tab.content.ProfilePageContentTabView;
import org.ednovo.gooru.client.mvp.rating.IsRatingAndReviewPopupView;
import org.ednovo.gooru.client.mvp.rating.RatingAndReviewPopupPresenter;
import org.ednovo.gooru.client.mvp.rating.RatingAndReviewPopupView;
import org.ednovo.gooru.client.mvp.register.IsRegisterView;
import org.ednovo.gooru.client.mvp.register.RegisterPresenter;
import org.ednovo.gooru.client.mvp.register.RegisterPresenter.IsRegisterProxy;
import org.ednovo.gooru.client.mvp.register.RegisterView;
import org.ednovo.gooru.client.mvp.search.IsSearchRootView;
import org.ednovo.gooru.client.mvp.search.SearchRootPresenter;
import org.ednovo.gooru.client.mvp.search.SearchRootPresenter.IsSearchRootProxy;
import org.ednovo.gooru.client.mvp.search.SearchRootView;
import org.ednovo.gooru.client.mvp.search.collection.CollectionSearchPresenter;
import org.ednovo.gooru.client.mvp.search.collection.CollectionSearchPresenter.IsCollectionSearchProxy;
import org.ednovo.gooru.client.mvp.search.collection.CollectionSearchView;
import org.ednovo.gooru.client.mvp.search.collection.IsCollectionSearchView;
import org.ednovo.gooru.client.mvp.search.resource.IsResourceSearchView;
import org.ednovo.gooru.client.mvp.search.resource.ResourceSearchPresenter;
import org.ednovo.gooru.client.mvp.search.resource.ResourceSearchPresenter.IsResourceSearchProxy;
import org.ednovo.gooru.client.mvp.search.resource.ResourceSearchView;
import org.ednovo.gooru.client.mvp.settings.IsUserSettingsView;
import org.ednovo.gooru.client.mvp.settings.UserSettingsPresenter;
import org.ednovo.gooru.client.mvp.settings.UserSettingsPresenter.IsUserSettingProxy;
import org.ednovo.gooru.client.mvp.settings.UserSettingsView;
import org.ednovo.gooru.client.mvp.shelf.IsShelfView;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter;
import org.ednovo.gooru.client.mvp.shelf.ShelfPresenter.IsShelfProxy;
import org.ednovo.gooru.client.mvp.shelf.ShelfView;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormInPlayPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormInPlayView;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormPresenter.IsCollectionFormProxy;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormView;
import org.ednovo.gooru.client.mvp.shelf.collection.IsCollectionFormInPlayView;
import org.ednovo.gooru.client.mvp.shelf.collection.IsCollectionFormView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.FolderItemTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.FolderItemTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.folders.IsFolderItemTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.CollectionAssignTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.assign.IsCollectionAssignTab;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.CollectionCollaboratorsTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.CollectionCollaboratorsTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.collaborators.IsCollectionCollaboratorsTab;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.info.CollectionInfoTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.info.CollectionInfoTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.info.IsCollectionInfoTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.CollectionResourceTabPresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.CollectionResourceTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.IsCollectionResourceTabView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddResourcePresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.AddResourceView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.IsAddResourceView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.drive.DrivePresenter;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.drive.DriveView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.drive.IsDriveView;
import org.ednovo.gooru.client.mvp.shelf.collection.tab.resource.add.drive.DrivePresenter.IsDriveyProxy;
import org.ednovo.gooru.client.mvp.shelf.list.IsShelfListView;
import org.ednovo.gooru.client.mvp.shelf.list.ShelfListPresenter;
import org.ednovo.gooru.client.mvp.shelf.list.ShelfListView;
import org.ednovo.gooru.client.mvp.wrap.IsWrapView;
import org.ednovo.gooru.client.mvp.wrap.WrapPresenter;
import org.ednovo.gooru.client.mvp.wrap.WrapPresenter.IsWrapProxy;
import org.ednovo.gooru.client.mvp.wrap.WrapView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * @author Search Team
 * 
 */
public class AppModule extends AppPresenterModule {

	public static final String GA_ACCOUNT = "UA-20089789-1";
	public static final String GA_ACCOUNT_HEWLETT = "UA-5033010-1";

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(AppPlaceManager.class).in(Singleton.class);
		bind(AppPlaceKeeper.class).in(Singleton.class);
		bind(AppClientFactory.class).asEagerSingleton();
		bind(IsPlaceManager.class).to(AppPlaceManager.class)
				.in(Singleton.class);
		bind(TokenFormatter.class).to(AppParameterTokenFormatter.class).in(
				Singleton.class);
		bind(RootPresenter.class).to(AppRootPresenter.class).asEagerSingleton();

		bindConstant().annotatedWith(AppDefaultPlace.class)
				.to(PlaceTokens.HOME);
		bindPresenter(PrimePresenter.class, IsPrimeView.class, PrimeView.class,
				IsPrimeProxy.class);
		bindPresenter(HomePresenter.class, IsHomeView.class, HomeView.class,
				IsHomeProxy.class);
		bindPresenter(WrapPresenter.class, IsWrapView.class, WrapView.class,
				IsWrapProxy.class);
		bindPresenter(RegisterPresenter.class, IsRegisterView.class,
				RegisterView.class, IsRegisterProxy.class);
		bindPresenter(SearchRootPresenter.class, IsSearchRootView.class,
				SearchRootView.class, IsSearchRootProxy.class);
		bindPresenter(CollectionSearchPresenter.class,
				IsCollectionSearchView.class, CollectionSearchView.class,
				IsCollectionSearchProxy.class);
		bindPresenter(ResourceSearchPresenter.class,
				IsResourceSearchView.class, ResourceSearchView.class,
				IsResourceSearchProxy.class);
		bindPresenter(ErrorPresenter.class, IsErrorView.class, ErrorView.class,
				IsErrorProxy.class);
		bindPresenter(ShelfPresenter.class, IsShelfView.class, ShelfView.class,
				IsShelfProxy.class);
		bindPresenter(UserSettingsPresenter.class, IsUserSettingsView.class,
				UserSettingsView.class, IsUserSettingProxy.class);
		// 5.2 Changes
		bindPresenter(ClasspagePresenter.class, IsClasspageView.class,
				ClasspageView.class, IsClasspageProxy.class);
		bindPresenter(FoldersPresenter.class, IsFoldersView.class,
				FoldersView.class, IsFoldersProxy.class);
		bindPresenter(EditClasspagePresenter.class, IsEditClasspageView.class,
				EditClasspageView.class, IsEditClasspageProxy.class);

		bindPresenter(FolderFormViewPresenter.class, IsFoldersPopupView.class,
				FolderPopUpUiBinder.class, IsFolderPopUpFormProxy.class);
		bindPresenter(EditFolderPresenter.class, IsEditFolderView.class,
				EditFolderView.class, IsEditFolderProxy.class);

		bindPresenter(ProfilePagePresenter.class, IsProfilePageView.class,
				ProfilePageView.class, IsProfilePageProxy.class);

		bindPresenterWidget(CollectionResourceTabPresenter.class,
				IsCollectionResourceTabView.class,
				CollectionResourceTabView.class);
		bindPresenterWidget(CollectionInfoTabPresenter.class,
				IsCollectionInfoTabView.class, CollectionInfoTabView.class);

		bindPresenterWidget(FolderInfoTabPresenter.class,
				IsFolderInfoTabView.class, FolderInfoTabView.class);

		bindPresenterWidget(FolderContentTabPresenter.class,
				IsFolderContentTabView.class, FolderContentTabView.class);

		bindSingletonPresenterWidget(ShelfListPresenter.class,
				IsShelfListView.class, ShelfListView.class);
		bindPresenter(CollectionFormPresenter.class,
				IsCollectionFormView.class, CollectionFormView.class,
				IsCollectionFormProxy.class);
		bindPresenter(ResourcePlayPresenter.class, IsResourcePlayView.class,
				ResourcePlayView.class, IsResourcePlayProxy.class);
		bindPresenter(CollectionPlayPresenter.class,
				IsCollectionPlayView.class, CollectionPlayView.class,
				IsCollectionPlayProxy.class);
		bindPresenterWidget(ImageUploadPresenter.class,
				IsImageUploadView.class, ImageUploadView.class);
		bindPresenterWidget(AddResourcePresenter.class,
				IsAddResourceView.class, AddResourceView.class);

		bindPresenterWidget(AddAssignmentContainerPresenter.class,
				IsAddAssignmentContainerView.class,
				AddAssignmentContainerView.class);

		bindPresenter(ClassCodePresenter.class, IsClassCodeView.class,
				ClassCodeView.class, IsClassCodeProxy.class);
		
		bindPresenter(ClassHomePresenter.class, IsClassHomeView.class,
				ClassHomeView.class, IsClassHomeProxy.class);

		bindPresenter(StudentAssignmentPresenter.class,
				IsStudentAssignmentView.class, StudentAssignmentView.class,
				IsStudentAssignmentProxy.class);

		bindPresenterWidget(ProfilePageContentTabPresenter.class,
				IsProfilePageContentTabView.class,
				ProfilePageContentTabView.class);

		/*bindPresenterWidget(ProfilePageInfoTabPresenter.class,
				IsProfilePageInfoTabView.class, ProfilePageInfoTabView.class);
*/
		bindPresenter(DeviceSupportPresenter.class, IsDeviceSupportView.class,
				DeviceSupportView.class, IsDeviceSupportProxy.class);

		bindPresenterWidget(ProfilePageListPresenter.class,
				IsProfilePageListView.class, ProfilePageListView.class);

		bindPresenterWidget(UserRegistrationPresenter.class,
				IsUserRegistrationView.class, UserRegistrationView.class);

		bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class);
		
		bindConstant().annotatedWith(GaAccount.class).to(GA_ACCOUNT);
		bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();
		
//		bind(GoogleAnalyticsHpNavigationTracker.class).asEagerSingleton();
		
		bindPresenterWidget(CollectionFormInPlayPresenter.class,
				IsCollectionFormInPlayView.class,
				CollectionFormInPlayView.class);

		bindPresenterWidget(CollectionAssignTabPresenter.class,
				IsCollectionAssignTab.class, CollectionAssignTabView.class);
		bindPresenterWidget(SignUpPresenter.class, IsSignUpView.class,SignUpView.class);
		bindPresenter(CollectionPlayerPresenter.class, IsCollectionPlayerView.class, CollectionPlayerView.class,IsCollectionPlayerProxy.class);
		bindPresenter(ResourcePlayerPresenter.class, IsResourcePlayerView.class, ResourcePlayerView.class,IsResourcePlayerProxy.class);
		bindPresenterWidget(CollectionPlayerMetadataPresenter.class, IsCollectionPlayerMetadataView.class, CollectionPlayerMetadataView.class);
		bindPresenterWidget(CollectionPlayerTocPresenter.class, IsCollectionPlayerTocView.class, CollectionPlayerTocView.class);
		bindPresenterWidget(ResourcePlayerMetadataPresenter.class, IsResourcePlayerMetadataView.class, ResourcePlayerMetadataView.class);
		bindPresenterWidget(ResourceNarrationPresenter.class, IsResourceNarrationView.class, ResourceNarrationView.class);
		bindPresenterWidget(CollectionSharePresenter.class, IsCollectionShareView.class, CollectionShareView.class);
		bindPresenterWidget(ResourceInfoPresenter.class, IsResourceInfoView.class, ResourceInfoView.class);
		bindPresenterWidget(QuestionResourcePresenter.class, IsQuestionResourceView.class, QuestionResourceView.class);
		bindPresenterWidget(CollectionEndPresenter.class, IsCollectionEndView.class, CollectionEndView.class);
		bindPresenterWidget(ResourceSharePresenter.class, IsResourceShareView.class, ResourceShareView.class);
		bindPresenterWidget(AddResourceCollectionPresenter.class, IsAddResourceCollectionView.class, AddResourceCollectionView.class);	
		bindPresenterWidget(AddCollectionPresenter.class, IsAddCollectionView.class, AddCollectionView.class);
		bindPresenterWidget(SignUpCompleteProfilePresenter.class,IsSignUpCompleteProfile.class,SignUpCompleteProfileView.class);
		bindPresenterWidget(SignUpAfterThirteenPresenter.class,IsAfterThirteen.class,SignUpTurnsAfterThirteenView.class);
		
		bindPresenter(RusdPresenter.class, IsRusdView.class, RusdView.class,IsRusdProxy.class);
		bindPresenter(PreviewPlayerPresenter.class,IsPreviewPlayerView.class,PreviewPlayerView.class,IsPreviewPlayerProxy.class);
		bindPresenterWidget(PreviewPlayerMetadataPresenter.class,IsPreviewPlayerMetadataView.class,PreviewPlayerMetadataView.class);
		bindPresenterWidget(PreviewHomePresenter.class,IsPreviewHomeView.class,PreviewHomeView.class);
		bindPresenterWidget(PreviewEndPresenter.class,IsPreviewEndView.class,PreviewEndView.class);
		bindPresenterWidget(CollectionFlagPresenter.class,IsCollectionFlagView.class,CollectionFlagView.class);
		bindPresenterWidget(ResourceFlagPresenter.class,IsResourceFlag.class,ResourceFlagView.class);
		
		bindPresenterWidget(CollectionCollaboratorsTabPresenter.class,
				IsCollectionCollaboratorsTab.class, CollectionCollaboratorsTabView.class);
	
		bindPresenterWidget(FolderItemTabPresenter.class,IsFolderItemTabView.class, FolderItemTabView.class);
		
		bindPresenterWidget(ClassListPresenter.class, IsClassListView.class, ClassListView.class);
		
		bindPresenter(FteLibraryPresenter.class, IsFteLibraryView.class, FteLibraryView.class,IsFteLibraryProxy.class);
		bindPresenter(AutodeskLibraryPresenter.class, IsAutodeskLibraryView.class, AutodeskLibraryView.class,IsAutodeskLibraryProxy.class);
		bindPresenter(OnrLibraryPresenter.class, IsOnrLibraryView.class, OnrLibraryView.class,IsOnrLibraryProxy.class);
		bindPresenter(LessonopolyLibraryPresenter.class, IsLessonopolyLibraryView.class, LessonopolyLibraryView.class,IsLessonopolyLibraryProxy.class);
		bindPresenter(NgcLibraryPresenter.class, IsNgcLibraryView.class, NgcLibraryView.class,IsNgcLibraryProxy.class);
		bindPresenter(WspwhLibraryPresenter.class, IsWspwhLibraryView.class, WspwhLibraryView.class,IsWspwhLibraryProxy.class);
		bindPresenterWidget(PartnerLibraryPresenter.class, IsPartnerLibraryView.class, PartnerLibraryView.class);
		bindPresenterWidget(RatingAndReviewPopupPresenter.class, IsRatingAndReviewPopupView.class, RatingAndReviewPopupView.class);
		bindPresenter(PsdpalLibraryPresenter.class, IsPsdpalView.class, PsdpalLibraryView.class,IsPsdpalLibraryProxy.class);
		bindPresenter(CfciLibraryPresenter.class, IsCfciView.class, CfciLibraryView.class,IsCfciLibraryProxy.class);
		
		

		bindPresenter(SausdPresenter.class, IsSausdView.class, SausdView.class,IsSausdProxy.class);
		bindPresenter(DrivePresenter.class, IsDriveView.class, DriveView.class,IsDriveyProxy.class);

	}
}