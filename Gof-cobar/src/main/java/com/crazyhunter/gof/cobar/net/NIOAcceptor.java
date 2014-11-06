package com.crazyhunter.gof.cobar.net;

import com.crazyhunter.gof.cobar.connection.ServerConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class NIOAcceptor extends Thread {
    private static final Logger LOGGER = Logger.getLogger(NIOAcceptor.class);
    private final AcceptIdGenerator ID_GENERATOR = new AcceptIdGenerator();
    private final Selector selector;
    private final ServerSocketChannel serverChannel;

    private NIOProcessor[] processors;
    private int curProcIndex;

    public NIOAcceptor(String name,int port) throws IOException {
        super(name);
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void setProcessors(NIOProcessor[] processors)
    {
        this.processors = processors;
    }

    @Override
    public void run()
    {
        while (true)
        {
            final Selector selecor = this.selector;
            Set<SelectionKey> skeys = null;
            try {
                selector.select(1000l);
                skeys = selector.selectedKeys();
                for (SelectionKey key:skeys)
                {
                    if(key.isAcceptable())
                    {
                        accept();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(skeys!=null)
                {
                    skeys.clear();
                }
            }
        }
    }

    private void accept()
    {
        LOGGER.info("=========accept===========");
        SocketChannel sc = null;
        try{
            sc = serverChannel.accept();

            ServerConnection serverConnection = new ServerConnection(sc);
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
}
