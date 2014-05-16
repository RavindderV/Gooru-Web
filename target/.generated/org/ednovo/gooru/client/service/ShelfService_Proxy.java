package org.ednovo.gooru.client.service;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class ShelfService_Proxy extends RemoteServiceProxy implements org.ednovo.gooru.client.service.ShelfServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "org.ednovo.gooru.client.service.ShelfService";
  private static final String SERIALIZATION_POLICY ="C1CE1230EAE5B7058D70C75695D9802A";
  private static final org.ednovo.gooru.client.service.ShelfService_TypeSerializer SERIALIZER = new org.ednovo.gooru.client.service.ShelfService_TypeSerializer();
  
  public ShelfService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "gwt-service/shelfService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  @Override
  public SerializationStreamWriter createStreamWriter() {
    ClientSerializationStreamWriter toReturn =
      (ClientSerializationStreamWriter) super.createStreamWriter();
    if (getRpcToken() != null) {
      toReturn.addFlags(ClientSerializationStreamWriter.FLAG_RPC_TOKEN_INCLUDED);
    }
    return toReturn;
  }
  @Override
  protected void checkRpcTokenType(RpcToken token) {
    if (!(token instanceof com.google.gwt.user.client.rpc.XsrfToken)) {
      throw new RpcTokenException("Invalid RpcToken type: expected 'com.google.gwt.user.client.rpc.XsrfToken' but got '" + token.getClass() + "'");
    }
  }
}
