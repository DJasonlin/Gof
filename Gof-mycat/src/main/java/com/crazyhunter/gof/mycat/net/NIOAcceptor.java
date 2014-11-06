package com.crazyhunter.gof.mycat.net;

import com.crazyhunter.gof.mycat.connection.ServerConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class NIOAcceptor{
    private static final Logger LOGGER = Logger.getLogger(NIOAcceptor.class);
    private final AcceptIdGenerator ID_GENERATOR = new AcceptIdGenerator();

    private final String name;
    private final AsynchronousServerSocketChannel serverChannel;
    private final AcceptHandler handler;

    private NIOProcessor[] processors;
    private int curProcIndex;

    public NIOAcceptor(String name,int port) throws IOException {
        this.name = name;
        handler = new AcceptHandler();
        serverChannel = AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
    }

    public void setProcessors(NIOProcessor[] processors)
    {
        this.processors = processors;
    }

    public void start()
    {
        this.pendingAccept();
    }

    private void pendingAccept()
    {
        if(serverChannel.isOpen())
        {
            serverChannel.accept(this,handler);
        }else
        {
            throw new IllegalStateException(
                    "Gof-mycat Server Channel has been closed");
        }
    }


    private void accept(AsynchronousSocketChannel channel)
    {
        LOGGER.info("=========accept===========");
        SocketChannel sc = null;
        try{
            ServerConnection serverConnection = new ServerConnection(channel);
            NIOProcessor processor = nextProcessor();
            serverConnection.setProcessor(processor);
            serverConnection.setId(ID_GENERATOR.getId());
            serverConnection.regist();
        }catch (Throwable e)
        {
            LOGGER.error("accept error",e);
        }
    }

    private NIOProcessor nextProcessor()
    {
        if(curProcIndex == processors.length)
        {
            curProcIndex = 0 ;
        }
        return processors[curProcIndex++];
    }
    /**
     * 前端连接ID生成器
     */
    private static class AcceptIdGenerator {

        private static final long MAX_VALUE = 0xffffffffL;

        private long acceptId = 0L;
        private final Object lock = new Object();

        private long getId() {
            synchronized (lock) {
                if (acceptId >= MAX_VALUE) {
                    acceptId = 0L;
                }
                return ++acceptId;
            }
        }
    }

    private static class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, NIOAcceptor>{

        @Override
        public void completed(AsynchronousSocketChannel channel, NIOAcceptor acceptor) {
            acceptor.accept(channel);
            acceptor.pendingAccept();
        }

        @Override
        public void failed(Throwable exc, NIOAcceptor acceptor) {
            LOGGER.info("acception connect failed:"+exc,exc);
            acceptor.pendingAccept();
        }
    }
}
