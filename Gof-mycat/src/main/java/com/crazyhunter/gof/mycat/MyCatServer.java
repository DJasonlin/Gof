package com.crazyhunter.gof.mycat;

import com.crazyhunter.gof.mycat.config.Config;
import com.crazyhunter.gof.mycat.net.NIOAcceptor;
import com.crazyhunter.gof.mycat.net.NIOProcessor;
import com.crazyhunter.gof.mycat.util.ResourcePathUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Administrator on 2014/11/6.
 */
public class MyCatServer {
    private static final Logger LOGGER = Logger.getLogger(MyCatServer.class);

    private static final MyCatServer INSTANCE = new MyCatServer();

    private static final String LOG4J_RELA_PATH  = "conf/log4j.xml";
    private static final long LOG4J_WATCH_DELAY = 60000;//log4j.xml更改监控间隔
    private static final String  SERVER_NAME = "Gof-mycat";

    private NIOAcceptor server;
    private NIOProcessor[] processors;
    private MyCatServer()
    {
    }
    public static MyCatServer getInstance()
    {
        return INSTANCE;
    }

    public void beforeStart()
    {
        String log4jPath = ResourcePathUtil.getPath(LOG4J_RELA_PATH);
        Log4jInitializer.configureAndWatch(log4jPath,LOG4J_WATCH_DELAY);
    }

    public void startup() throws IOException {
        LOGGER.info("=======begin to start server=========");

        processors = new NIOProcessor[Config.COUNT_PROCESSORS];
        for (int i = 0;i<processors.length;i++)
        {
            processors[i] = new NIOProcessor(SERVER_NAME+"-processor-"+i);
        }

        server = new NIOAcceptor(SERVER_NAME+"-server",Config.PORT_SERVER);
        server.setProcessors(processors);
        server.start();

        LOGGER.info("=======start server over=========");
    }
}
