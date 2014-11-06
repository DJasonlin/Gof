package com.crazyhunter.gof.cobar;

import com.crazyhunter.gof.cobar.config.Config;
import com.crazyhunter.gof.cobar.net.NIOAcceptor;
import com.crazyhunter.gof.cobar.net.NIOProcessor;
import com.crazyhunter.gof.cobar.util.ResourcePathUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by crazyhunter on 2014/11/6.
 */
public final class CobarServer {
    private static final Logger LOGGER = Logger.getLogger(CobarServer.class);

    private static final CobarServer INSTANCE = new CobarServer();

    private static final String LOG4J_RELA_PATH  = "conf/log4j.xml";
    private static final long LOG4J_WATCH_DELAY = 60000;//log4j.xml更改监控间隔
    private static final String  SERVER_NAME = "Gof-cobar";

    private NIOProcessor[] processors;//处理器列表
    private NIOAcceptor server;//前端连接接收线程

    public static CobarServer getInstance()
    {
        return INSTANCE;
    }

    private CobarServer()
    {
    }

    public void beforeStart()
    {
        String log4jPath = ResourcePathUtil.getPath(LOG4J_RELA_PATH);
        Log4jInitializer.configureAndWatch(log4jPath,LOG4J_WATCH_DELAY);
    }

    public void startup() throws IOException {
        LOGGER.info("=======begin to start server=========");

        //初始化处理器
        processors = new NIOProcessor[Config.COUNT_PROCESSORS];
        for (int i = 0;i<processors.length;i++)
        {
            processors[i] = new NIOProcessor(SERVER_NAME+"-processor-"+i,Config.COUNT_HANDLERS,Config.COUNT_EXECUTORS);
            processors[i].startup();
        }

        //初始化连接接收线程
        server = new NIOAcceptor(SERVER_NAME+"-serverAcceptor",Config.PORT_SERVER);
        server.setProcessors(processors);
        server.start();

        LOGGER.info("=======start server over=========");
    }
}
