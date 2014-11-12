package com.crazyhunter.gof.net;

import com.crazyhunter.gof.connection.ServerConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端请求接收处理
 */
public final class NetAcceptor {
    private static final Logger LOGGER = Logger.getLogger(NetAcceptor.class);

    private final GIDGenerator GID_GENERATOR = new GIDGenerator();

    private final AsynchronousServerSocketChannel serverChannel;

    public NetAcceptor(int port) throws IOException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(),1);//server连接接收线程池
        serverChannel = AsynchronousServerSocketChannel.open(group);
        serverChannel.bind(new InetSocketAddress(port));
    }

    public void startup()
    {
        this.pendingAccept();
    }

    private void pendingAccept()
    {
        serverChannel.accept(this,new CompletionHandler<AsynchronousSocketChannel, NetAcceptor>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, NetAcceptor attachment) {
                try {
                    accept(channel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                attachment.pendingAccept();
            }

            @Override
            public void failed(Throwable exc, NetAcceptor attachment) {
                attachment.pendingAccept();
            }
        });
    }

    private void accept(AsynchronousSocketChannel channel) throws IOException {
        LOGGER.info("======accept========"+channel.getRemoteAddress()+";");
        ServerConnection serverConnection = new ServerConnection(GID_GENERATOR.getGid(),channel);
        serverConnection.regist();
    }

    final  class GIDGenerator{
        private AtomicLong gid = new AtomicLong(0);
        public long getGid()
        {
            return gid.getAndIncrement();
        }
    }
}

