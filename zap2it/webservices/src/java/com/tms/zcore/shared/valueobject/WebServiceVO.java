/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tms.zcore.shared.valueobject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
/**
 *
 * @author pmarshall
 */
public class WebServiceVO {

    String webServiceName;
    Map<String,String> paramValueMap;
    HttpServletRequest request;
    HttpServletResponse response;

    public Map<String, String> getParamValueMap() {
        return paramValueMap;
    }

    public void setParamValueMap(Map<String, String> paramValueMap) {
        this.paramValueMap = paramValueMap;
    }

    public String getWebServiceName() {
        return webServiceName;
    }

    public void setWebServiceName(String webServiceName) {
        this.webServiceName = webServiceName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    
}
