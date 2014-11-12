package com.crazyhunter.gof.connection;

import org.apache.log4j.Logger;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * 前端连接
 */
public final class ServerConnection {
    private static final Logger LOGGER = Logger.getLogger(ServerConnection.class);

    private final long id;
    private final AsynchronousSocketChannel socketChannel;

    public ServerConnection(long id,AsynchronousSocketChannel socketChannel)
    {
        this.id = id;
        this.socketChannel = socketChannel;
    }

    /**
     * 注册前端连接。
     */
    public void regist()
    {
        LOGGER.info("=========regist serverConnection==========="+id);
    }
}
