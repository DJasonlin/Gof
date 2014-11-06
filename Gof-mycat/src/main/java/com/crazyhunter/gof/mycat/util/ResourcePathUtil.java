package com.crazyhunter.gof.mycat.util;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class ResourcePathUtil {
    public static final String getPath(String relaPath)
    {
        return ClassLoader.getSystemClassLoader().getResource(relaPath).getPath();
    }
}
