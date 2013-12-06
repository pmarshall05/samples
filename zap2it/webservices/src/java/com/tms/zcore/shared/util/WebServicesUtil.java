/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.zcore.shared.util;

import com.tms.TMSUtils;
import com.tms.TMSException;
import com.tms.ResourceWatcher;
import com.tms.userinfo.UserInfoFactory;
import com.tms.userinfo.User;
import com.tms.db.dao.tv.UserCheckInFactory;
import com.tms.zcore.shared.valueobject.ZCBaseVO;
import com.tms.common.factory.CommonObjectFactory;
import com.tms.datastore.common.Genre;
import com.tms.datastore.common.XmlContentCached;
import com.tms.datastore.tv.Station;
import com.tms.zcore.tvlistings.service.CommonService;
import com.tms.tvlistings.MatrixFilterParameter;
import com.tms.db.dao.tv.EpisodeCheckInTotal;
import com.tms.datastore.tv.EpisodeCheckInCache;
import com.tms.tvlistings.factory.TVObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collection;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.tms.common.factory.GenreMaskFactory;
import com.tms.datastore.common.GenreMask;
import com.tms.tvlistings.factory.GridRowFactory;
import java.util.List;
import org.apache.struts.util.MessageResources;

/**
 * @author pmarshall
 */
public class WebServicesUtil {

    public static final Logger ZC_LOG = Logger.getLogger("com.tms.zcore.tvlistings.util.WebServicesUtil");
    private static final String AID_REGEX = "AID";
    private static final String CALLBACK_BEGIN_REGEX = "CALLBACK_BEGIN";
    private static final String CALLBACK_END_REGEX = "CALLBACK_END";
    private static final String RVAR_REGEX = "RVAR";
    public static final int COMSCORE_WIDGET_ID = 60000;
    public static final int COMSCORE_DATA_ID = 30000;

    /**
     * Prints data to response using XStream object
     *
     * @param xstream - XStream object used for serialization
     * @param baseVO - Value object to be serialized
     */
    public static String getResponse(XStream xstream, ZCBaseVO baseVO) {
        return xstream.toXML(baseVO);
    }

    /**
     * Returns registered XStream object based on return type
     *
     * @param returnType -return type (JSON or XML)
     * @param valueObject - class to be processed
     */
    public static XStream getXStream(String returnType, Class valueObject) {
        XStream xstream = null;

        if (returnType != null && returnType.equalsIgnoreCase("json")) {
            xstream = new XStream(new JsonHierarchicalStreamDriver());
        } else {
            xstream = new XStream();
        }
        xstream.registerConverter(
                new CollectionConverter(xstream.getMapper()) {
            public boolean canConvert(Class type) {
                return (type == TreeSet.class) || (type == ArrayList.class);
            }
        });
        xstream.processAnnotations(valueObject);
        return xstream;
    }

    public static void omitFields(XStream xstream, Class classDefinedIn, String[] fields) {
        for (String field : fields) {
            xstream.omitField(classDefinedIn, field);
        }
    }

    public static void changeAlias(XStream xstream, Class classDefinedIn, Map<String, String> map) {
        Iterator iterator = map.entrySet().iterator();
        Map.Entry entry = null;

        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            String alias = (String) entry.getKey();
            String attribute = (String) entry.getValue();
            xstream.aliasField(alias, classDefinedIn, attribute);
        }
    }

    public static String customizeJsonData(String response) {
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("\n");
        dataBuilder.append(CALLBACK_BEGIN_REGEX);
        dataBuilder.append(RVAR_REGEX);
        dataBuilder.append(response);
        dataBuilder.append(CALLBACK_END_REGEX);
        dataBuilder.append("\n");
        return dataBuilder.toString();
    }

    public static void storeResponseInCache(String returnType, String cacheKey, String response) {
        XmlContentCached xmlCache = CommonObjectFactory.getInstance().getCachedXmlContent(cacheKey);

        if ("xml".equals(returnType)) {
            xmlCache.setXmlContent(response);
        } else if ("html".equals(returnType)) {
            xmlCache.setHtmlContent(response);
        } else if ("json".equals(returnType)) {
            xmlCache.setJsonContent(customizeJsonData(response));
        }

    }

    public static String getCachedResponse(String returnType, String cacheKey) {
        String response = null;

        XmlContentCached xmlCache = null;
        Calendar now = Calendar.getInstance();
        Calendar updated = Calendar.getInstance();
        Calendar expire = Calendar.getInstance();

        xmlCache = CommonObjectFactory.getInstance().getCachedXmlContent(cacheKey);

        if (xmlCache.getLastUpdateXml() != null && "xml".equals(returnType)) {
            updated.setTime(xmlCache.getLastUpdateXml());
        } else if (xmlCache.getLastUpdateHtml() != null && "html".equals(returnType)) {
            updated.setTime(xmlCache.getLastUpdateHtml());
        } else if (xmlCache.getLastUpdateJson() != null && "json".equals(returnType)) {
            updated.setTime(xmlCache.getLastUpdateJson());
        } else {
            updated.setTime(now.getTime());
        }

        // Item only lives in cache for an hour
        expire.setTime(updated.getTime());
        expire.add(Calendar.HOUR, 1);

        if (xmlCache != null && (now.after(updated) && now.before(expire))) {

            if ("xml".equals(returnType) && xmlCache.getXmlContent() != null && !xmlCache.getXmlContent().isEmpty()) {
                response = xmlCache.getXmlContent();
            } else if ("html".equals(returnType) && xmlCache.getHtmlContent() != null && !xmlCache.getHtmlContent().isEmpty()) {
                response = xmlCache.getHtmlContent();
            } else if ("json".equals(returnType) && xmlCache.getJsonContent() != null && !xmlCache.getJsonContent().isEmpty()) {
                response = xmlCache.getJsonContent();
            }
        }
        return response;
    }

    public static String replaceExpressions(String response, String aid, String callback, String returnVariable) {
        if (response != null && !response.isEmpty()) {
            response = replaceAidTemplate(response, aid);
            if (callback != null) {
                response = replaceCallbackTemplate(response, callback);
            } else if (returnVariable != null) {
                response = replaceReturnVarTemplate(response, returnVariable);
            } else {
                response = replaceEmptyTemplate(response);
            }
        }
        return response;
    }

    private static String replaceAidTemplate(String templateResponse, String affiliateId) {
        String customResponse;
        if (affiliateId != null && !affiliateId.isEmpty()) {
            customResponse = templateResponse.replaceAll(AID_REGEX, "?aid=" + affiliateId);
        } else {
            customResponse = templateResponse.replaceAll(AID_REGEX, "");
        }
        return customResponse;
    }

    private static String replaceCallbackTemplate(String templateResponse, String callback) {
        String customResponse;
        templateResponse = templateResponse.replaceAll(CALLBACK_BEGIN_REGEX, callback + "(");
        templateResponse = templateResponse.replaceAll(CALLBACK_END_REGEX, ")");
        customResponse = templateResponse.replaceAll(RVAR_REGEX, "");
        return customResponse;
    }

    private static String replaceReturnVarTemplate(String templateResponse, String returnVariable) {
        String customResponse;
        templateResponse = templateResponse.replaceAll(RVAR_REGEX, returnVariable + "=");
        templateResponse = templateResponse.replaceAll(CALLBACK_BEGIN_REGEX, "");
        customResponse = templateResponse.replaceAll(CALLBACK_END_REGEX, "");
        return customResponse;
    }

    private static String replaceEmptyTemplate(String templateResponse) {
        String customResponse;
        templateResponse = templateResponse.replaceAll(CALLBACK_BEGIN_REGEX, "");
        templateResponse = templateResponse.replaceAll(CALLBACK_END_REGEX, "");
        customResponse = templateResponse.replaceAll(RVAR_REGEX, "");
        return customResponse;
    }

    public static String setZipcodeFromTimezone(String timezone) {
        MessageResources messages = MessageResources.getMessageResources("MessageResources");
        String zipcode = null;

        if (timezone.equalsIgnoreCase("ET")) {
            zipcode = messages.getMessage("default.eastern.zipcode");
        } else if (timezone.equalsIgnoreCase("CT")) {
            zipcode = messages.getMessage("default.central.zipcode");
        } else if (timezone.equalsIgnoreCase("MT")) {
            zipcode = messages.getMessage("default.mountain.zipcode");
        } else if (timezone.equalsIgnoreCase("PT")) {
            zipcode = messages.getMessage("default.pacific.zipcode");
        } else if (timezone.equalsIgnoreCase("AKT")) {
            zipcode = messages.getMessage("default.alaska.zipcode");
        } else if (timezone.equalsIgnoreCase("HT")) {
            zipcode = messages.getMessage("default.hawaii.zipcode");
        }

        return zipcode;
    }

    public static String setLineupIdFromTimezone(String timezone) {
        MessageResources messages = MessageResources.getMessageResources("MessageResources");
        String lineupId = null;

        if (timezone.equalsIgnoreCase("ET")) {
            lineupId = messages.getMessage("default.eastern.lineupId");
        } else if (timezone.equalsIgnoreCase("CT")) {
            lineupId = messages.getMessage("default.central.lineupId");
        } else if (timezone.equalsIgnoreCase("MT")) {
            lineupId = messages.getMessage("default.mountain.lineupId");
        } else if (timezone.equalsIgnoreCase("PT")) {
            lineupId = messages.getMessage("default.pacific.lineupId");
        } else if (timezone.equalsIgnoreCase("AKT")) {
            lineupId = messages.getMessage("default.alaska.lineupId");
        } else if (timezone.equalsIgnoreCase("HT")) {
            lineupId = messages.getMessage("default.hawaii.lineupId");
        }

        return lineupId;
    }

    public static Calendar getStartCalendar(String date, String time, TimeZone tz) {
        long tim = 0;
        SimpleDateFormat sdf = null;
        Calendar start = Calendar.getInstance(tz);
        int primetimehr = TMSUtils.getInstance().getTimeZonePrimeTimeOffset(tz);
        Calendar cal = null;

        try {
            if (date != null && time != null) {
                sdf = new SimpleDateFormat("yyyy-MM-dd h:mma");
                sdf.setTimeZone(tz);
                start.setTimeInMillis(sdf.parse(date + " " + time).getTime());
            } else if (date != null && time == null) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(tz);
                start.setTime(sdf.parse(date));
                start.set(Calendar.HOUR_OF_DAY, primetimehr);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
            } else if (date == null && time != null) {
                sdf = new SimpleDateFormat("h:mma");
                sdf.setTimeZone(tz);
                cal = Calendar.getInstance(tz);
                cal.setTime(sdf.parse(time));
                start.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
                start.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
                start.set(Calendar.SECOND, 0);
            } else {
                start.set(Calendar.HOUR_OF_DAY, primetimehr);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
            }
        } catch (ParseException pe) {
            ZC_LOG.log(Level.WARNING, pe.getMessage(), pe);
        }
        tim = ZCActionUtil.getFromTime(start.getTimeInMillis()).getTimeInMillis();
        start.setTimeInMillis(tim);

        return start;
    }

    public static Calendar getOVDStartCalendar(String date, String time, TimeZone tz) {
        long tim = 0;
        SimpleDateFormat sdf = null;
        Calendar start = Calendar.getInstance(tz);
        int primetimehr = TMSUtils.getInstance().getTimeZonePrimeTimeOffset(tz);
        Calendar cal = null;

        try {
            if (date != null && time != null) {
                sdf = new SimpleDateFormat("yyyy-MM-dd h:mma");
                sdf.setTimeZone(tz);
                start.setTimeInMillis(sdf.parse(date + " " + time).getTime());
            } else if (date != null && time == null) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(tz);
                start.setTime(sdf.parse(date));
                start.set(Calendar.HOUR_OF_DAY, primetimehr);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
            } else if (date == null && time != null) {
                sdf = new SimpleDateFormat("h:mma");
                sdf.setTimeZone(tz);
                cal = Calendar.getInstance(tz);
                cal.setTime(sdf.parse(time));
                start.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
                start.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
                start.set(Calendar.SECOND, 0);
            } else {
                start.set(Calendar.HOUR_OF_DAY, primetimehr);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
            }
        } catch (ParseException pe) {
            ZC_LOG.log(Level.WARNING, pe.getMessage(), pe);
        }

        return start;
    }

    /**
     * @param Time
     * @return
     */
    public static long convertTimeInMillis(String time, TimeZone tz) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma");

        try {
            Calendar cal = Calendar.getInstance(tz);
            cal.setTime(dateFormat.parse(time));
            Calendar start = Calendar.getInstance(tz);
            start.setTimeZone(tz);

            start.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            start.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            return start.getTimeInMillis();
        } catch (ParseException e) {
            ZC_LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return 0;

    }

    /**
     * Calculate Start Time (date+Time) in milliseconds
     *
     * @param date
     * @param time
     * @param tz
     * @return
     */
    public static Calendar getCategoryStartTime(String date, String time, TimeZone tz, boolean isSmartPhone) {
        long tim = 0;
        Calendar start = Calendar.getInstance(tz);
        Calendar now = Calendar.getInstance(tz);
        int startHour = 0;
        boolean isET = false;
        boolean isPT = false;


        if (tz.getDisplayName(false, TimeZone.SHORT).equals("EST")
                || tz.getDisplayName(false, TimeZone.SHORT).equals("EDT")) {
            isET = true;
        }

        if (tz.getDisplayName(false, TimeZone.SHORT).equals("PST")
                || tz.getDisplayName(false, TimeZone.SHORT).equals("PDT")) {
            isPT = true;
        }


        if (isSmartPhone) {
            if (date != null && time != null) {
                // User selected Date + User Selected Time  (in timeinMillseconds)
                start.setTimeInMillis(Long.valueOf(date) + Long.valueOf(time));
            } else if (date == null && time != null) {
                //  current date with 2:36 AM
                start.setTimeInMillis(Long.valueOf(time));
            }
        } else {
            if (date != null) {
                start.setTimeInMillis(Long.valueOf(date));
            }

            if (isET || isPT) {
                startHour = 2;
            } else {
                startHour = 1;
            }
            if (date == null && time == null && now.get(Calendar.HOUR_OF_DAY) < startHour) {
                start.add(Calendar.DATE, -1);
            }
            start.set(Calendar.HOUR_OF_DAY, startHour);
        }

        start.add(Calendar.MINUTE, 0);
        start.add(Calendar.SECOND, 0);
        start.add(Calendar.MILLISECOND, 0);
        tim = ZCActionUtil.getFromTime(start.getTimeInMillis()).getTimeInMillis();
        start.setTimeInMillis(tim);

        return start;
    }

    public static Collection<Station> getStationList(String stationList) throws TMSException {
        List<Station> collStations = new ArrayList<Station>();
        if (stationList != null) {
            String[] zcAfflStnList = stationList.split(",");
            // ZC_LOG.info("PTGAction: getStationList: stationList size = "+zcAfflStnList.length);
            for (String stationStr : zcAfflStnList) {
                try {
                    Long stnNum = Long.parseLong(stationStr);
                    Station loStation = CommonService.getInstance().getStation(stnNum);
                    if (loStation != null) {
                        collStations.add(collStations.size(), loStation);
                    }

                } catch (NumberFormatException ex) {
                    ZC_LOG.log(Level.INFO, "getStationList: Invalid station number", ex);
                }
            }
        }
        return collStations;
    }

    public static MatrixFilterParameter setMatrixFilter(String filter) {
        MatrixFilterParameter sFilter = null;
        GenreMask genreMask = GenreMaskFactory.createGenreMask();

        if (filter != null) {
            String[] filters = filter.split(",");
            sFilter = new MatrixFilterParameter();

            for (String param : filters) {
                if ("new".equalsIgnoreCase(param)) {
                    sFilter.setNewSchedule(true);
                } else if ("live".equalsIgnoreCase(param)) {
                    sFilter.setLiveSchedule(true);
                } else if ("hd".equalsIgnoreCase(param)) {
                    sFilter.setHdtv(true);
                } else if ("cc".equalsIgnoreCase(param)) {
                    sFilter.setCloseCaptioned(true);
                } else if ("repeat".equalsIgnoreCase(param)) {
                    sFilter.setRepeat(true);
                } else if ("subtitle".equalsIgnoreCase(param)) {
                    sFilter.setSubtitled(true);
                } else if ("stereo".equalsIgnoreCase(param)) {
                    sFilter.setStereo(true);
                } else if ("movies".equalsIgnoreCase(param)) {
                    genreMask.addGenre(Genre.MOVIE);
                } else if ("talk".equalsIgnoreCase(param)) {
                    genreMask.addGenre(Genre.TALK);
                } else if ("sports-talk".equalsIgnoreCase(param)) {
                    genreMask.addGenre(Genre.SPORTS_TALK);
                } else if ("sports".equalsIgnoreCase(param)) {
                    genreMask.addGenre(Genre.SPORTS);
                }
            }
            sFilter.setGenreMask(genreMask);
        }
        return sFilter;
    }

    public static boolean isServerBusy(HttpServletRequest request) {
        boolean busy = false;

        if (!request.getRequestURL().toString().startsWith("http://test.zap2it.com")) {
            busy = ResourceWatcher.threadsBusy();
        }
        return busy;
    }

    public static int getCheckInCounter(String programIdGF) {
        int counter = 0;
        Collection<String> programIds = new ArrayList<String>();
        Collection<EpisodeCheckInTotal> totals = null;
        EpisodeCheckInCache episodeCache = TVObjectFactory.getInstance().getEpisodeCheckInCache();

        programIds.add(programIdGF);
        totals = episodeCache.getTotals(programIds);

        if (totals != null && !totals.isEmpty()) {
            counter = totals.iterator().next().getTotal();
        }

        return counter;
    }

    public static Map<String, Integer> getCheckInCounters(Collection<String> programIds) {
        String log = "";
        Collection<EpisodeCheckInTotal> totals = null;
        EpisodeCheckInCache episodeCache = TVObjectFactory.getInstance().getEpisodeCheckInCache();
        Map<String, Integer> counters = new HashMap<String, Integer>();

        totals = episodeCache.getTotals(programIds);

        if (totals != null) {
            for (EpisodeCheckInTotal total : totals) {
                counters.put(total.getDatabaseKey(), total.getTotal());
                log += total.getDatabaseKey() + ":" + total.getTotal() + ",";
            }
        }

        return counters;
    }

    public static String getUserCheckins(HttpServletRequest request, HttpServletResponse response, Collection<String> programIds) {
        String checkins = null;
        String userEmail = ZCActionUtil.getCookieAndMigrateToQuotedCookie(request, response, CookieNames.ZC_USER_EMAIL);
        User user = null;
        Collection<String> userCheckins = null;

        if (userEmail != null && programIds != null && !programIds.isEmpty()) {
            user = UserInfoFactory.getInstance().getUser(userEmail);
            if (user != null) {
                try {
                    userCheckins = UserCheckInFactory.getInstance().getUserCheckIns(user.getId(), programIds);
                    if (userCheckins != null && !userCheckins.isEmpty()) {
                        checkins = StringUtils.join(userCheckins.iterator(), ",");
                    }
                } catch (TMSException tme) {
                    ZC_LOG.log(Level.WARNING, "Error retrieving checkins for user " + user.getId() + ": " + tme.toString(), tme);
                }
            } else {
                ZC_LOG.warning("Error retrieving user with email " + userEmail);
            }
        } else {
            checkins = ZCActionUtil.getCookie(request, response, CookieNames.ZC_CHECKIN);
        }
        return checkins;
    }

    public static String getComscore(int type) {
        StringBuilder comscore = new StringBuilder();

        comscore.append("<script>");
        comscore.append("var _comscore = _comscore || [];");
        comscore.append("_comscore.push({c1: \"7\", c2: \"6036462\", c3: \"").append(type).append("\" });");
        comscore.append("(function() {");
        comscore.append("var s = document.createElement(\"script\"), el = document.getElementsByTagName(\"script\")[0]; s.async = true;");
        comscore.append("s.src = (document.location.protocol == \"https:\" ? \"https://sb\" : \"http://b\") +");
        comscore.append("\".scorecardresearch.com/beacon.js\";");
        comscore.append("el.parentNode.insertBefore(s, el);");
        comscore.append("})();");
        comscore.append("</script>");
        comscore.append("<noscript>");
        comscore.append("<img src=\"http://b.scorecardresearch.com/p?c1=7&c2=6036462&c3=").append(type).append("&c4=&c5=&c6=&c15=&cv=2.0&cj=1\" />");
        comscore.append("</noscript>");

        return comscore.toString();
    }

    public static Calendar getCategoryEndCalendar(Calendar start, int duration, boolean isSmartPhone) throws Exception {

        Calendar end = (Calendar) start.clone();

        if (isSmartPhone) {
            // Till Midnight.
            Calendar calEnd = Calendar.getInstance(start.getTimeZone());
            calEnd.set(Calendar.DAY_OF_YEAR, start.get(Calendar.DAY_OF_YEAR) + 1);
            calEnd.set(Calendar.HOUR_OF_DAY, 0);
            calEnd.set(Calendar.MINUTE, 0);
            calEnd.set(Calendar.SECOND, 0);
            calEnd.set(Calendar.MILLISECOND, 0);
            end.setTimeInMillis(calEnd.getTimeInMillis());
        } else {
            end.add(Calendar.HOUR, duration);
        }
        return end;
    }

    public static Calendar getCategoryOlympicsEndCalendar(Calendar start) throws Exception {
        int dateDuration = 14;
        Calendar olympicsEndDate = Calendar.getInstance();
        // August 13, 2012
        olympicsEndDate.set(2012, 8, 13);
        if (null != olympicsEndDate) {
            dateDuration = (int) ((olympicsEndDate.getTime().getTime() - start.getTime().getTime()) / (1000 * 60 * 60 * 24));
        }
        if (dateDuration > 14) {
            dateDuration = 14;
        }
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DATE, dateDuration);
        return end;
    }

    public static Map<String, String> getDayMap(TimeZone timezone, Locale locale) {
        Map<String, String> dayMap = new LinkedHashMap<String, String>();
        boolean loop = true;
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", locale);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(timezone);
        Calendar start = Calendar.getInstance(timezone);
        Calendar end = Calendar.getInstance(timezone);

        start.setTime(GridRowFactory.getInstance().getMatrixStartDate());
        end.setTime(GridRowFactory.getInstance().getMatrixEndDate());

        while (loop) {
            dayMap.put(simpleDateFormat.format(start.getTime()), sdf.format(start.getTime()));
            start.add(Calendar.DATE, 1);
            i++;
            if (start.after(end)) {
                break;
            }
        }
        return dayMap;
    }

    public static Map<String, String> getDayOfWeek(TimeZone timezone, Locale locale) {
        Map<String, String> dayOfWeek = new LinkedHashMap<String, String>();
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday"};
        Calendar now = Calendar.getInstance(timezone);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        int firstDay = 0;

        for (int week = 0; week < 7; week++) {
            now.add(Calendar.DAY_OF_YEAR, -1);
            if (firstDay == 0) {
                dayOfWeek.put("Yesterday", String.valueOf(now.getTimeInMillis()));
            } else {
                dayOfWeek.put(strDays[now.get(Calendar.DAY_OF_WEEK) - 1], String.valueOf(now.getTimeInMillis()));
            }
            firstDay--;
        }
        return dayOfWeek;
    }
}
