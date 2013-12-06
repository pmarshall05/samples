/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tms.zcore.shared.service;

import com.tms.zcore.shared.valueobject.ZCBaseVO;
import com.tms.zcore.shared.valueobject.WebServiceVO;
import java.util.Map;

/**
 *
 * @author pmarshall
 */
public interface WebService {

    public static final String JSON = "json";
    public static final String XML = "xml";
    public static final String HTML = "html";
    public static final String XHTML = "xhtml";
    public static final String RSS = "rss";
    public static final String DEFAULT_ZIPCODE = "12345";
    public static final String DEFAULT_LINEUP = "DFLTE:-";
    public static final String DEFAULT_AID = "zap2it";
    public static final String DEFAULT_VERSION = "1";
    public static final String DEFAULT_FORMAT_TYPE = "1";
    public static final String INVALID_RETURN_TYPE = "Invalid return type";
    public static final String INVALID_WEB_SERVICE_NAME = "Invalid web service name";
    public static final String DURATION_IN_HOURS = "3";


    public WebServiceVO getWebServiceVO(String webServiceName, Map<String,String[]> parameterMap);

    public ZCBaseVO createVO(WebServiceVO webServiceVO);

    public String getResponseString(ZCBaseVO baseVO, String returnType, WebServiceVO webServiceVO);
}
