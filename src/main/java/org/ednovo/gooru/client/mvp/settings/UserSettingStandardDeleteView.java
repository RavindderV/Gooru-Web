package org.ednovo.gooru.client.mvp.settings;

import java.util.List;

import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.mvp.home.library.events.StandardPreferenceSettingEvent;
import org.ednovo.gooru.shared.i18n.CopyOfMessageProperties;
import org.ednovo.gooru.shared.model.user.ProfileDo;
import org.ednovo.gooru.shared.util.MessageProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
public class UserSettingStandardDeleteView extends PopupPanel {

	private static UserSettingStandardDeleteViewUiBinder uiBinder = GWT
			.create(UserSettingStandardDeleteViewUiBinder.class);

	interface UserSettingStandardDeleteViewUiBinder extends
			UiBinder<Widget, UserSettingStandardDeleteView> {
	}
	
	private CopyOfMessageProperties i18n = GWT.create(CopyOfMessageProperties.class);
	
	@UiField Label titleLabel,headerLabel,descriptionLabel;
	@UiField Button cancelButton,removeButton;
	String gooruUid="";
	String USER_TAXONOMY_ROOT_CODE="user_taxonomy_root_code";
	Label standardSavingTextLabel;
	HTMLPanel standardsSaveCancelButtonContainer;
	Button standardsEditButton;
	List<String> getUserCodeId;
	private static final String USER_META_ACTIVE_FLAG = "0";
	
	public UserSettingStandardDeleteView(String gooruUid,Button standardsEditButton,HTMLPanel standardsSaveCancelButtonContainer,Label standardSavingTextLabel) {
		setWidget(uiBinder.createAndBindUi(this));
		this.gooruUid=gooruUid;
		this.standardsEditButton=standardsEditButton;
		this.standardsSaveCancelButtonContainer=standardsSaveCancelButtonContainer;
		this.standardSavingTextLabel=standardSavingTextLabel;
			
		setTextAndUi();
		this.setGlassEnabled(true);
		this.setPixelSize(450, 252);
		Window.enableScrolling(false);
	}
	public void setTextAndUi()
	{
		titleLabel.setText(i18n.GL1162());	
		//headerLabel.setText(MessageProperties.i18n.GL1565);	
		descriptionLabel.setText(i18n.GL1564());	
		cancelButton.setText(i18n.GL0142());	
		removeButton.setText(i18n.GL0237());	
	}
	 @UiHandler("cancelButton")
	 public void onClickCancelButton(ClickEvent event)
	 {
		 this.hide();
		 Window.enableScrolling(true);
	 }
	 @UiHandler("removeButton")
	 public void onClickremoveButton(ClickEvent event)
	 {
		 this.hide();
		 Window.enableScrolling(true);
		 standardsEditButton.setVisible(false);
		 standardsSaveCancelButtonContainer.setVisible(false);
		 standardSavingTextLabel.setText(i18n.GL0808());
		 AppClientFactory.getInjector().getUserService().updatePartyCustomField(gooruUid,USER_TAXONOMY_ROOT_CODE,"",new SimpleAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					standardsEditButton.setVisible(true);
					standardSavingTextLabel.setText("");
					AppClientFactory.getInjector().getUserService().getUserProfileV2Details(gooruUid,USER_META_ACTIVE_FLAG,
							new SimpleAsyncCallback<ProfileDo>() {

								@Override
								public void onSuccess(ProfileDo profileObj) {
									AppClientFactory.fireEvent(new StandardPreferenceSettingEvent(profileObj.getUser().getMeta().getTaxonomyPreference().getCode()));
								}

							});
					
				}
			});
	 }
}
