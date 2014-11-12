package com.crazyhunter.gof.util;

/**
 * 资源路径工具类
 */
public final class ResourcePathUtil {
    public static final String getPath(String relaPath)
    {
        return ClassLoader.getSystemClassLoader().getResource(relaPath).getPath();
    }
}
