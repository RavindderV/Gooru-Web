package org.ednovo.gooru.client.mvp.play.collection.flag;

import java.util.ArrayList;

import org.ednovo.gooru.client.gin.BaseUiHandlers;


public interface CollectionFlagUiHandler extends BaseUiHandlers{
	public void createCollectionContentReport(String associatedGooruOid,String freeText,ArrayList<String> contentReportList,String deleteContentReportGooruOids);
	public void getContentReport(String associatedGooruOid);
	public String getResourceContentReport(String associatedGooruOid);
	
	
	
}
