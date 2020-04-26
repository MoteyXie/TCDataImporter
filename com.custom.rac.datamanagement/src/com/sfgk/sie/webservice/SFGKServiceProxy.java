package com.sfgk.sie.webservice;

public class SFGKServiceProxy implements com.sfgk.sie.webservice.SFGKService {
  private String _endpoint = null;
  private com.sfgk.sie.webservice.SFGKService sFGKService = null;
  
  public SFGKServiceProxy() {
    _initSFGKServiceProxy();
  }
  
  public SFGKServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initSFGKServiceProxy();
  }
  
  private void _initSFGKServiceProxy() {
    try {
      sFGKService = (new com.sfgk.sie.webservice.SFGKServiceServiceLocator()).getSFGKServicePort();
      if (sFGKService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sFGKService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sFGKService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sFGKService != null)
      ((javax.xml.rpc.Stub)sFGKService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sfgk.sie.webservice.SFGKService getSFGKService() {
    if (sFGKService == null)
      _initSFGKServiceProxy();
    return sFGKService;
  }
  
  public java.lang.String getID(java.lang.String arg0, int arg1) throws java.rmi.RemoteException{
    if (sFGKService == null)
      _initSFGKServiceProxy();
    return sFGKService.getID(arg0, arg1);
  }
  
  public java.lang.String classify(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (sFGKService == null)
      _initSFGKServiceProxy();
    return sFGKService.classify(arg0, arg1);
  }
  
  public java.lang.String getDesignIDRuleXML() throws java.rmi.RemoteException{
    if (sFGKService == null)
      _initSFGKServiceProxy();
    return sFGKService.getDesignIDRuleXML();
  }
  
  
}