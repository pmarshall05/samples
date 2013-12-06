package com.tms.zcore.shared.valueobject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for all ZCore ValueObject
 * User: DKasukaranpalayam
 * Date: Oct 23, 2006
 * Time: 2:06:01 PM
 */
public abstract class ZCBaseVO {
	private static final Logger ZC_LOG = Logger.getLogger("com.tms.zcore.shared.valueobject.ZCBaseVO");
    private static final String LINE_SEP = System.getProperty("line.separator");
	private Locale locale;

	/** overriden objects toString method that constructs a String based on all the getter methods
     *
     * @return String returns the values of the getter methods in the VO
     */
    public final String toString() {
        return toStringHelper(this);
    }

	public static String toStringHelper(ZCBaseVO object) {
		String sysCon =System.getProperty("sysConfig");
        if(sysCon ==null || !sysCon.equals("dev") ){
         return "";
        }
        Class cl = object.getClass();
        Method meths[] = cl.getMethods();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("############   ");
        strBuilder.append(cl.getName());
        strBuilder.append("  ############");
        strBuilder.append(LINE_SEP);
        for (Method meth : meths) {
            Object returnObj = null;
            String methname = meth.getName();
            if (methname.startsWith("get") || methname.startsWith("is")) {
                try {
                    returnObj = meth.invoke(object);
                    strBuilder.append(methname);
                    strBuilder.append(" : ");
                } catch (IllegalAccessException e) {
                	ZC_LOG.log(Level.INFO, e.getMessage(), e);
                } catch (InvocationTargetException e) {
                	ZC_LOG.log(Level.INFO, e.getMessage(), e);
                }
                if (returnObj != null) {
                    strBuilder.append(returnObj);
                } else {
                    strBuilder.append("void | null");
                }
                strBuilder.append(LINE_SEP);
            }
        }
        strBuilder.append("####### END #####");
        return strBuilder.toString();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}

