package com.crazyhunter.gof.cobar.connection;

import com.crazyhunter.gof.cobar.net.NIOProcessor;
import org.apache.log4j.Logger;

import java.nio.channels.SocketChannel;

/**
 * Created by Administrator on 2014/11/6.
 */
public class ServerConnection {
    private static final Logger LOGGER = Logger.getLogger(ServerConnection.class);

    private final SocketChannel channel;

    private NIOProcessor processor;
    private long id;

    public ServerConnection(SocketChannel channel)
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
