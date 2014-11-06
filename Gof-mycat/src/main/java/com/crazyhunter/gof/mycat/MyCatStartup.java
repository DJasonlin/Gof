package com.crazyhunter.gof.mycat;

import com.crazyhunter.gof.mycat.util.SimpleDataUtil;
import org.apache.log4j.helpers.LogLog;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class MyCatStartup {
    public static void main(String[] args)
    {
        try {
            MyCatServer server = MyCatServer.getInstance();
            server.beforeStart();;
            server.startup();

            while (true)
            {
                Thread.sleep(300*1000);
            }
        }catch (Throwable e)
        {
            LogLog.error(SimpleDataUtil.getDateSimple() + "startup error", e);
            System.exit(-1);
        }
    }
}
