package com.crazyhunter.gof.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date工具类
 */
public final class SimpleDateUtil {
    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDateSimple()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        return sdf.format(new Date());
    }
}
