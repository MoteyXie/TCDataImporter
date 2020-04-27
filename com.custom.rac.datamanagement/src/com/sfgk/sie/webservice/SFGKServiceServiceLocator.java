/**
 * SFGKServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sfgk.sie.webservice;

public class SFGKServiceServiceLocator extends org.apache.axis.client.Service implements com.sfgk.sie.webservice.SFGKServiceService {

    public SFGKServiceServiceLocator() {
    }


    public SFGKServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SFGKServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SFGKServicePort
    private java.lang.String SFGKServicePort_address = "http://192.168.27.93:9393/SFGKService";

    public java.lang.String getSFGKServicePortAddress() {
        return SFGKServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SFGKServicePortWSDDServiceName = "SFGKServicePort";

    public java.lang.String getSFGKServicePortWSDDServiceName() {
        return SFGKServicePortWSDDServiceName;
    }

    public void setSFGKServicePortWSDDServiceName(java.lang.String name) {
        SFGKServicePortWSDDServiceName = name;
    }

    public com.sfgk.sie.webservice.SFGKService getSFGKServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SFGKServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSFGKServicePort(endpoint);
    }

    public com.sfgk.sie.webservice.SFGKService getSFGKServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sfgk.sie.webservice.SFGKServicePortBindingStub _stub = new com.sfgk.sie.webservice.SFGKServicePortBindingStub(portAddress, this);
            _stub.setPortName(getSFGKServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSFGKServicePortEndpointAddress(java.lang.String address) {
        SFGKServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sfgk.sie.webservice.SFGKService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sfgk.sie.webservice.SFGKServicePortBindingStub _stub = new com.sfgk.sie.webservice.SFGKServicePortBindingStub(new java.net.URL(SFGKServicePort_address), this);
                _stub.setPortName(getSFGKServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SFGKServicePort".equals(inputPortName)) {
            return getSFGKServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.sie.sfgk.com/", "SFGKServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.sie.sfgk.com/", "SFGKServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SFGKServicePort".equals(portName)) {
            setSFGKServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
