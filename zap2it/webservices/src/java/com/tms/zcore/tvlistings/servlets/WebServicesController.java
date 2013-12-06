package com.tms.zcore.tvlistings.servlets;

import com.tms.TMSException;
import com.tms.zcore.shared.service.CelebrityScoreServiceImpl;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tms.zcore.shared.service.NewsServiceImpl;
import com.tms.zcore.shared.service.PeopleFinderServiceImpl;
import com.tms.zcore.shared.service.WebService;
import com.tms.zcore.shared.valueobject.ZCBaseVO;
import com.tms.zcore.shared.valueobject.WebServiceVO;
import com.tms.zcore.tvlistings.businessutil.InventoryThread;
import com.tms.zcore.tvlistings.service.*;
import com.tms.zcore.shared.util.ConnectorParameters;
import com.tms.zcore.shared.util.ResponseHelper;
import com.tms.zcore.shared.util.ZCActionUtil;

import com.tms.zcore.shared.util.AffiliateUtil;
import com.tms.zcore.shared.util.ZCAffiliatePropertyNames;
import com.tms.zcore.shared.util.ZCSharedAttributeNames;
import com.tms.zcore.tvlistings.businessutil.RequestSerializer;
import com.tms.zcore.tvlistings.valueobject.UserVO;
import com.tms.zcore.tvlistings.valueobject.ZCSessionVO;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;

public class WebServicesController extends HttpServlet {

    private static final Logger ZC_LOG = Logger.getLogger("com.tms.zcore.tvlistings.servlets.WebServicesController");
    private static HashMap<String, Class> services;
    private static final String AMPERSAND = "&#38;";
    private static final String LESS_THAN = "&#60;";
    private static final String GREATER_THAN = "&#62;";
    private static final String QUOTE = "&#34;";
    private static final String APOSTROPHE = "&#39;";
    private static final long THREE_HOUR_PERIOD = 1000 * 60 * 60 * 3;
    private String lszSysConfig = null;

    public void init() {
        setSystemProperties();
        lszSysConfig = System.getProperty("sysConfig");

        // connect path permutations to class name
        services = new HashMap<String, Class>(17);
        services.put("lineup", LocalizeServiceImpl.class);
        services.put("channels", LocalizeServiceImpl.class);
        services.put("channelFinder", StationChannelFinderServiceImpl.class);
        services.put("newShows", FilterShowsServiceImpl.class);
        services.put("filterShows", FilterShowsServiceImpl.class);
        services.put("celebsontv", CelebrityScoreServiceImpl.class);
        services.put("packageFinder", PackageSelectorServiceImpl.class);
        services.put("peopleFinder", PeopleFinderServiceImpl.class);
        services.put("whatson", FilterShowsServiceImpl.class);
        services.put("primetimeGrid", FilterShowsServiceImpl.class);
        services.put("upcomingAirings", UpcomingAiringsServiceImpl.class);
        services.put("showFinder", UpcomingAiringsServiceImpl.class);
        services.put("checkin", CheckinServiceImpl.class);
        services.put("popularShows", FilterShowsServiceImpl.class);
        services.put("searchPrograms", SearchTVProgramServiceImpl.class);
        services.put("user", UserServiceImpl.class);
        services.put("footer", AttributionServiceImpl.class);
        services.put("olympics", UpcomingAiringsServiceImpl.class);
        services.put("ovd", FilterShowsServiceImpl.class);
        services.put("news", NewsServiceImpl.class);
        services.put("category", CategoryServiceImpl.class);

        
    }

    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        processRequest(request, response);
    }

    public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String webServiceName = null;
        WebService service = null;
        WebServiceVO webServiceVO = null;
        ZCBaseVO baseVO = null;
        String data = null;
        String returnType = null;
        ZCSessionVO sessionVO = null;

        try {
            webServiceName = getWebServiceName(request);
            service = getService(webServiceName);

            if (service != null) {
                String aid = request.getParameter("aid");
                aid = (aid == null || aid.isEmpty()) ? "zap2it" : aid;
                String language = AffiliateUtil.getAffiliateProperty(aid, ZCAffiliatePropertyNames.ZC_AFF_DEFAULT_LANGUAGE).getPropertyValueString();
                language = (language == null || language.isEmpty()) ? "en" : language;
                webServiceVO = service.getWebServiceVO(webServiceName, request.getParameterMap());
                webServiceVO.setRequest(request);
                webServiceVO.setResponse(response);
                baseVO = service.createVO(webServiceVO);
                baseVO.setLocale(new Locale(language));
                returnType = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_RETURN_TYPE);
                data = service.getResponseString(baseVO, returnType, webServiceVO);
                writeResponse(response, data, returnType);


                //If User API, update session info
                if ("user".equalsIgnoreCase(webServiceName)) {
                    sessionVO = (ZCSessionVO) request.getSession().getAttribute(ZCSharedAttributeNames.ATTRIBUTE_SESSION_VO);

                    if (sessionVO != null && sessionVO.getZcIUser() != null) {
                        sessionVO.setZcIUser(((UserVO) baseVO).getUser());
                        request.getSession().setAttribute(ZCSharedAttributeNames.ATTRIBUTE_SESSION_VO, sessionVO);
                    }
                }
            } else {
                writeResponse(response, WebService.INVALID_WEB_SERVICE_NAME, WebService.JSON);
            }
        } catch (final TMSException ex) {
            ZC_LOG.log(Level.WARNING, "Error in Web Services Controller:", ex);
        }
    }

    private WebService getService(String type) throws TMSException {
        Class serviceClass = null;
        WebService service = null;

        if (type != null) {
            serviceClass = services.get(type);
            if (serviceClass == null) {
                throw new TMSException("Web Services type does not exist: " + type);
            } else {
                try {
                    service = (WebService) serviceClass.newInstance();
                } catch (InstantiationException ie) {
                    ZC_LOG.log(Level.WARNING, "Could not instantiate " + serviceClass.getName(), ie);
                } catch (IllegalAccessException ie) {
                    ZC_LOG.log(Level.WARNING, "Could not instantiate " + serviceClass.getName(), ie);
                }
            }
        }
        return service;
    }

    private String getWebServiceName(HttpServletRequest request) {
        String name = null;
        String[] path = request.getRequestURI().toString().split("/");

        if (path != null && path.length > 3) {
            name = path[path.length - 1];
        }
        return name;
    }

    private void writeResponse(HttpServletResponse response, String data, String returnType) {
        //Set headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

        if (returnType.equals(WebService.XML)) {
            data = replaceXMLEntities(data);
            ResponseHelper.writeResponseData(response, ResponseHelper.XML_RESPONSE_HEADER + data, ResponseHelper.XML_CONTENT_TYPE);
        } else if (returnType.equals(WebService.JSON)) {
            ResponseHelper.writeResponseData(response, data, ResponseHelper.JS_JSON_CONTENT_TYPE);
        } else if (returnType.equals(WebService.RSS)) {
            data = replaceXMLEntities(data);
            ResponseHelper.writeResponseData(response, ResponseHelper.RSS_RESPONSE_HEADER + data, ResponseHelper.RSS_CONTENT_TYPE);
        } else {
            ResponseHelper.writeResponseData(response, data, ResponseHelper.HTML_CONTENT_TYPE);
        }
    }

    private String replaceXMLEntities(String string) {
        string = string.replaceAll("&amp;", AMPERSAND);
        string = string.replaceAll("&apos;", APOSTROPHE);
        string = string.replaceAll("&quot;", QUOTE);
        string = string.replaceAll("&lt;", LESS_THAN);
        string = string.replaceAll("&gt;", GREATER_THAN);

        return string;
    }

    private void setSystemProperties() {
        String repLineupId = null;
        String repZipcode = null;
        String fastProdUrl = null;
        String fastDevUrl = null;
        String newsBlogs = null;
        Collection<String> newsBlogList = null;

        String repLineupPath = "/properties/peoplefinder/representativeLineup";
        String repZipcodePath = "/properties/peoplefinder/representativeZip";
        String fastProdExpression = "/properties/search/fastProdUrl";
        String fastDevExpression = "/properties/search/fastDevUrl";
        String newsBlogExpression = "/properties/search/blogs/news";

        //PeopleFinder Properties
        if (System.getProperty("representativeLineup") == null || System.getProperty("representativeZip") == null) {
            repLineupId = ZCActionUtil.getAppConfigProperty(repLineupPath);
            repZipcode = ZCActionUtil.getAppConfigProperty(repZipcodePath);

            if (repLineupId != null) {
                System.setProperty("representativeLineup", repLineupId);
            }

            if (repZipcode != null) {
                System.setProperty("representativeZip", repZipcode);
            }
        }


        //Fast Properties
        if (System.getProperty("fastProdUrl") == null || System.getProperty("fastDevUrl") == null
                || System.getProperty("newsBlogs") == null) {
            fastProdUrl = ZCActionUtil.getAppConfigProperty(fastProdExpression);
            fastDevUrl = ZCActionUtil.getAppConfigProperty(fastDevExpression);
            newsBlogList = ZCActionUtil.getAppConfigPropertyList(newsBlogExpression);

            if (fastProdUrl != null) {
                System.setProperty("fastProdUrl", fastProdUrl);
            }

            if (fastDevUrl != null) {
                System.setProperty("fastDevUrl", fastDevUrl);
            }

            if (newsBlogList != null) {
                newsBlogs = StringUtils.join(newsBlogList.toArray(), ",");
                System.setProperty("newsBlogs", newsBlogs);
            }
        }
    }

    public void destroy() {
        
    }
}