package com.crazyhunter.gof.mycat.connection;

import com.crazyhunter.gof.mycat.net.NIOProcessor;
import org.apache.log4j.Logger;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by Administrator on 2014/11/6.
 */
public class ServerConnection {
    private static final Logger LOGGER = Logger.getLogger(ServerConnection.class);

    private final AsynchronousSocketChannel channel;

    private NIOProcessor processor;
    private long id;

    public ServerConnection(AsynchronousSocketChannel channel)
    {
        this.channel = channel;
    }

    public void setProcessor(NIOProcessor processor)
    {
        this.processor = processor;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void regist()
    {
        LOGGER.info("========regist ServerConnection========id:"+id);
    }
}
