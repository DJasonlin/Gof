package com.crazyhunter.gof.cobar;

import com.crazyhunter.gof.cobar.util.SimpleDataUtil;
import org.apache.log4j.helpers.LogLog;

/**
 * Created by crazyhunter on 2014/11/6.
 */
public final class CobarStartup {
    public static void main(String[] args)
    {
        try{
            CobarServer server = CobarServer.getInstance();
            server.beforeStart();
            server.startup();
        }catch (Throwable e)
        {
            LogLog.error(SimpleDataUtil.getDateSimple()+"startup error",e);
            System.exit(-1);
        }
    }
}
