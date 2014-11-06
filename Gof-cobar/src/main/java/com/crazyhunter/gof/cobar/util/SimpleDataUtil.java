package com.crazyhunter.gof.cobar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class SimpleDataUtil {
    public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDateSimple()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        return sdf.format(new Date());
    }
}
