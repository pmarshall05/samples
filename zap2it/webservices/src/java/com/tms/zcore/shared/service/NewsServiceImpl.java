/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.zcore.shared.service;

import com.thoughtworks.xstream.XStream;
import com.tms.TMSException;
import com.tms.TMSUtils;
import com.tms.common.factory.CommonObjectFactory;
import com.tms.datastore.common.Person;
import com.tms.datastore.tv.Show;
import com.tms.datastore.tv.ShowCard;
import com.tms.tvlistings.factory.TVObjectFactory;
import com.tms.tvlistings.factory.TVSearchFactory;
import com.tms.zcore.shared.util.ConnectorParameters;
import com.tms.zcore.shared.util.HostBuilder;
import com.tms.zcore.shared.util.RSSFeedUtility;
import com.tms.zcore.shared.util.RSSFeedUtility.FeedType;
import com.tms.zcore.shared.util.SEOHelper;
import com.tms.zcore.shared.util.WebServicesUtil;
import com.tms.zcore.shared.util.ZCActionUtil;
import com.tms.zcore.shared.valueobject.NewsVO;
import com.tms.zcore.shared.valueobject.WebServiceVO;
import com.tms.zcore.shared.valueobject.ZCBaseVO;
import com.tms.zcore.shared.valueobject.ZCRSSThumbnailVO;
import com.tms.zcore.shared.valueobject.ZCRSSVO;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author pjmarshall
 */
public class NewsServiceImpl implements ZCBaseService, WebService {

    private static final Logger ZC_LOG = Logger.getLogger("com.tms.zcore.shared.service.NewsServiceImpl");
    private final String DEFAULT_PERSON_ID = "0";
    private final int MAX_DAYS = 7;
    private final String POP2IT = "pop2it";
    private final String FROM_INSIDE_THE_BOX = "frominsidethebox";
    private static XStream xstreamJSON = null;
    private static XStream xstreamXML = null;

    static {
        xstreamJSON = WebServicesUtil.getXStream(JSON, com.tms.zcore.shared.valueobject.NewsVO.class);
        xstreamXML = WebServicesUtil.getXStream(XML, com.tms.zcore.shared.valueobject.NewsVO.class);
    }

    public String toXML() {
        return null;
    }

    public String toHTML() {
        return null;
    }

    public String toHTML(NewsVO newsVO) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<div class=\"ZapContent\">");

        if (newsVO.getStories() != null && !newsVO.getStories().isEmpty()) {
            for (ZCRSSVO story : newsVO.getStories()) {
                htmlBuilder.append("<div class=\"story\">");
                if (story.getThumbnail() != null) {
                    htmlBuilder.append("<div class=\"image\">");
                    htmlBuilder.append("<a href=\"").append(story.getLink()).append("\">");
                    htmlBuilder.append("<img class=\"thumbnail\" src=\"").append(story.getThumbnail().getUrl()).append("\">");
                    htmlBuilder.append("</a>");
                    htmlBuilder.append("</div>");
                }
                htmlBuilder.append("<div class=\"text\">");
                htmlBuilder.append("<div class=\"title\">");
                htmlBuilder.append("<a href=\"").append(story.getLink()).append("\">");
                htmlBuilder.append(story.getTitle());
                htmlBuilder.append("</a>");
                htmlBuilder.append("</div>");
                htmlBuilder.append("<div class=\"description\">").append(story.getDescription()).append("</div>");
                htmlBuilder.append("</div>");
                htmlBuilder.append("</div>");
            }
        }
        htmlBuilder.append("</div>");
        String html = htmlBuilder.toString().replace("'", "\\\'");
        return "document.write('" + html + "');";
    }

    public String toJSON() {
        return null;
    }

    public String getErrorCode(String zcTMSErrorCode) {
        return "0000";
    }

    public String toXHTML(ZCBaseVO baseVO) {
        return null;
    }

    public ZCBaseVO doService(ZCBaseVO baseVO) throws TMSException {
        NewsVO newsVO = (NewsVO) baseVO;
        Person person = null;
        ShowCard showcard = null;
        Show show = null;
        String[] stories = null;
        String[] files = null;
        Collection<String> blogStories = null;
        String category = null;
        String blog = null;
        String mainImage = null;
        Collection<ZCRSSVO> newsitems = null;
        ZCRSSThumbnailVO thumbnail = null;
        FeedType type = null;
        String id = null;
        String feedDir = null;
        String env = null;
        String imageHost = null;
        Calendar start = null;
        Calendar now = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");

        //Get image host
        imageHost = ZCActionUtil.getImageUrlPrefix();


        //Feed for Series
        if (newsVO.getSeriesId() != null) {
            type = FeedType.SHOW;
            feedDir = newsVO.getSeriesId();
            stories = RSSFeedUtility.getSortedFiles(newsVO.getSeriesId(), type);

            if (TVSearchFactory.getInstance().doesShowcardExist(newsVO.getSeriesId())) {
                showcard = TVObjectFactory.getInstance().getShowCard(newsVO.getSeriesId());
                if (showcard != null) {
                    category = showcard.getTitle();
                }
            } else {
                show = TVObjectFactory.getInstance().getShow(newsVO.getSeriesId());
                if (show != null) {
                    category = show.getTitle();
                }
            }

            newsVO.setLink(HostBuilder.getTvHostByConfig(ZCActionUtil.getEnvironment(), DEFAULT_AID)
                    + SEOHelper.getSeoLink(newsVO.getSeriesId(), TMSUtils.getInstance().simplifyStringForURL(category.toLowerCase(), "-"), false));
            newsVO.setDescription("TV news and recaps for " + category);
        } else if (newsVO.getPersonId() > 0) {
            //Feed for Person given valid Id
            type = FeedType.CELEBRITY;
            feedDir = String.valueOf(newsVO.getPersonId());
            person = CommonObjectFactory.getInstance().getPerson(newsVO.getPersonId());
            category = person.getMainPersonName().getFullName();
            stories = RSSFeedUtility.getSortedFiles(String.valueOf(newsVO.getPersonId()), type);

            newsVO.setLink(HostBuilder.getPeopleHostByConfig(ZCActionUtil.getEnvironment(), DEFAULT_AID)
                    + "/p/" + person.getMainPersonName().getUrlFriendlyFullName() + "/" + newsVO.getPersonId());
            newsVO.setDescription("Celebrity news and gossip for " + category);
        } else if (newsVO.getBlog() != null && !newsVO.getBlog().isEmpty()) {
            //Feed for Blogs only
            now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);
            start = (Calendar) now.clone();
            start.add(Calendar.DATE, -newsVO.getDays());

            blog = newsVO.getBlog().toLowerCase().replaceAll(" ", "");
            if (blog.equals(POP2IT)) {
                type = FeedType.POP2IT;
                category = "Pop2it";
                newsVO.setLink("http://blog.zap2it.com/pop2it/");
                newsVO.setDescription("Celebrity gossip, celeb pics and Hollywood, TV talk show, movie, music and pop culture news - Pop2it");
            } else if (blog.equals(FROM_INSIDE_THE_BOX)) {
                type = FeedType.FROM_INSIDE_THE_BOX;
                category = "From Inside the Box";
                newsVO.setLink("http://blog.zap2it.com/frominsidethebox/");
                newsVO.setDescription("TV news, TV recaps, TV reviews, daily Nielsen TV ratings, TV star interviews and analysis - From Inside the Box");
            }
            feedDir = null;
            blogStories = new LinkedList<String>();

            while (now.after(start) || now.equals(start)) {
                files = RSSFeedUtility.getSortedFiles(sdf.format(now.getTime()), type, start);

                if (files != null) {
                    blogStories.addAll(Arrays.asList(files));
                }
                now.add(Calendar.DATE, -1);
            }
            if (blogStories != null && !blogStories.isEmpty()) {
                stories = new String[blogStories.size()];
                stories = blogStories.toArray(stories);
            }
        }

        if (stories != null) {
            newsitems = RSSFeedUtility.readFiles(stories, feedDir, type, newsVO.getMax());
            newsVO.setStories(newsitems);
        }

        newsVO.setTitle(category);
        newsVO.setCategory(category);

        return newsVO;
    }
    /* Web Services Methods */

    public WebServiceVO getWebServiceVO(String webServiceName, Map<String, String[]> parameterMap) {
        WebServiceVO webServiceVO = new WebServiceVO();
        Map<String, String> paramMap = new HashMap<String, String>();

        String seriesId = (parameterMap.get(ConnectorParameters.CP_SERIES_ID) == null || parameterMap.get(ConnectorParameters.CP_SERIES_ID).length == 0) ? null : parameterMap.get(ConnectorParameters.CP_SERIES_ID)[0];
        String name = (parameterMap.get(ConnectorParameters.CP_CELEBRITY_NAME) == null || parameterMap.get(ConnectorParameters.CP_CELEBRITY_NAME).length == 0) ? null : parameterMap.get(ConnectorParameters.CP_CELEBRITY_NAME)[0];
        String personId = (parameterMap.get(ConnectorParameters.CP_PERSON_ID) == null || parameterMap.get(ConnectorParameters.CP_PERSON_ID).length == 0) ? DEFAULT_PERSON_ID : parameterMap.get(ConnectorParameters.CP_PERSON_ID)[0];
        String days = (parameterMap.get(ConnectorParameters.CP_DAYS) == null || parameterMap.get(ConnectorParameters.CP_DAYS).length == 0) ? "1" : parameterMap.get(ConnectorParameters.CP_DAYS)[0];
        String rssSource = (parameterMap.get(ConnectorParameters.CP_BLOG) == null || parameterMap.get(ConnectorParameters.CP_BLOG).length == 0) ? null : parameterMap.get(ConnectorParameters.CP_BLOG)[0];
        String maxItems = (parameterMap.get(ConnectorParameters.CP_MAX_ITEMS) == null || parameterMap.get(ConnectorParameters.CP_MAX_ITEMS).length == 0) ? "0" : parameterMap.get(ConnectorParameters.CP_MAX_ITEMS)[0];
        String callback = (parameterMap.get(ConnectorParameters.CP_CALLBACK) == null || parameterMap.get(ConnectorParameters.CP_CALLBACK).length == 0) ? null : parameterMap.get(ConnectorParameters.CP_CALLBACK)[0];
        String returnType = (parameterMap.get(ConnectorParameters.CP_RETURN_TYPE) == null || parameterMap.get(ConnectorParameters.CP_RETURN_TYPE).length == 0) ? RSS : parameterMap.get(ConnectorParameters.CP_RETURN_TYPE)[0];
        String returnVariable = (parameterMap.get(ConnectorParameters.CP_RETURN_VARIABLE) == null || parameterMap.get(ConnectorParameters.CP_RETURN_VARIABLE).length == 0) ? null : parameterMap.get(ConnectorParameters.CP_RETURN_VARIABLE)[0];

        if (Integer.parseInt(days) > MAX_DAYS) {
            days = String.valueOf(MAX_DAYS);
        }

        paramMap.put(ConnectorParameters.CP_SERIES_ID, seriesId);
        paramMap.put(ConnectorParameters.CP_CELEBRITY_NAME, name);
        paramMap.put(ConnectorParameters.CP_PERSON_ID, personId);
        paramMap.put(ConnectorParameters.CP_DAYS, days);
        paramMap.put(ConnectorParameters.CP_BLOG, rssSource);
        paramMap.put(ConnectorParameters.CP_MAX_ITEMS, maxItems);
        paramMap.put(ConnectorParameters.CP_CALLBACK, callback);
        paramMap.put(ConnectorParameters.CP_RETURN_TYPE, returnType);
        paramMap.put(ConnectorParameters.CP_RETURN_VARIABLE, returnVariable);

        webServiceVO.setWebServiceName(webServiceName);
        webServiceVO.setParamValueMap(paramMap);
        return webServiceVO;
    }

    public ZCBaseVO createVO(WebServiceVO webServiceVO) {
        NewsVO newsVO = new NewsVO();
        String name = null;
        String seriesId = null;
        String blog = null;
        long personId = 0;
        int max = 0;
        int days = 0;

        if (webServiceVO != null) {
            name = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_CELEBRITY_NAME);
            seriesId = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_SERIES_ID);
            blog = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_BLOG);
            personId = Long.parseLong(webServiceVO.getParamValueMap().get(ConnectorParameters.CP_PERSON_ID));
            max = Integer.parseInt(webServiceVO.getParamValueMap().get(ConnectorParameters.CP_MAX_ITEMS));
            days = Integer.parseInt(webServiceVO.getParamValueMap().get(ConnectorParameters.CP_DAYS));

            newsVO.setPersonId(personId);
            newsVO.setName(name);
            newsVO.setSeriesId(seriesId);
            newsVO.setMax(max);
            newsVO.setDays(days);
            newsVO.setBlog(blog);
        }

        try {
            doService(newsVO);
        } catch (TMSException tmse) {
        }
        return newsVO;
    }

    public String getResponseString(ZCBaseVO baseVO, String returnType, WebServiceVO webServiceVO) {
        NewsVO newsVO = (NewsVO) baseVO;
        String response = null;
        String callback = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_CALLBACK);
        String aid = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_AFFILIATE_ID);
        String returnVariable = webServiceVO.getParamValueMap().get(ConnectorParameters.CP_RETURN_VARIABLE);
        XStream xstream = JSON.equalsIgnoreCase(returnType) ? xstreamJSON : xstreamXML;

        xstream.aliasField("language", NewsVO.class, "locale");

        if (HTML.equalsIgnoreCase(returnType)) {
            response = toHTML(newsVO);
        } else if (RSS.equals(returnType)) {
            xstream.addImplicitCollection(NewsVO.class, "stories");
            xstream.aliasField("dc:creator", ZCRSSVO.class, "author");
            xstream.aliasField("media:thumbnail", ZCRSSVO.class, "thumbnail");
            xstream.aliasField("media:thumbnail", NewsVO.class, "thumbnail");
            response = WebServicesUtil.getResponse(xstream, newsVO);
            response += "\n</rss>";
        } else {
            xstream.aliasField("author", ZCRSSVO.class, "author");
            xstream.aliasField("thumbnail", ZCRSSVO.class, "thumbnail");
            response = WebServicesUtil.getResponse(xstream, newsVO);
        }

        return response;
    }
}
