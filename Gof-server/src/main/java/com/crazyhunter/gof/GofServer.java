package com.crazyhunter.gof;

import com.crazyhunter.gof.config.Config;
import com.crazyhunter.gof.net.NetAcceptor;
import com.crazyhunter.gof.util.ResourcePathUtil;

import java.io.IOException;

/**
 * 核心server
 */
public final class GofServer {

    private static final GofServer INSTANCE = new GofServer();

    private static final String LOG4J_RELA_PATH  = "conf/log4j.xml";
    private static final long LOG4J_WATCH_DELAY = 60000;//log4j.xml更改监控间隔
    private static final String  SERVER_NAME = "Gof-server";

    private NetAcceptor server;

    private GofServer()
    {

    }

    public static GofServer getInstance()
    {
        return INSTANCE;
    }

    public void beforeStart()
    {
        String log4jPath = ResourcePathUtil.getPath(LOG4J_RELA_PATH);
        Log4jInitializer.configureAndWatch(log4jPath,LOG4J_WATCH_DELAY);
    }

    public void startup() throws IOException {
        server = new NetAcceptor(Config.SERVER_PORT);
        server.startup();
    }
}
