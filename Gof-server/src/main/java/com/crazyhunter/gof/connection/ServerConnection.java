package com.crazyhunter.gof.connection;

import com.crazyhunter.gof.config.ServerCapability;
import com.crazyhunter.gof.packet.mysql.HandshakePacket;
import com.crazyhunter.gof.packet.mysql.MySQLPacket;
import com.crazyhunter.gof.util.MySQLCharsetUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 前端连接
 */
public final class ServerConnection {
    private static final Logger LOGGER = Logger.getLogger(ServerConnection.class);

    private String charsetString = "utf-8";

    private final long id;
    private final AsynchronousSocketChannel socketChannel;

    private final BlockingQueue<ByteBuffer> writeQueue = new LinkedBlockingDeque<ByteBuffer>();
    private final WriteHandler writeHandler = new WriteHandler();
    private final ReadHandler readHandler = new ReadHandler();
    private final ByteBuffer readBuffer = ByteBuffer.allocate(1024);


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
        HandshakePacket handshakePacket = new HandshakePacket();
        handshakePacket.packetNum = 0;
        handshakePacket.protocal = 10;
        handshakePacket.version = "gof-server".getBytes();
        handshakePacket.threadId = id;
        handshakePacket.salt = new byte[]{1,2,3,4,5,6,7,8};
        handshakePacket.serverCapabilities = getServerCapabilites();
        handshakePacket.serverLanguage = (byte)(MySQLCharsetUtil.getIndex(charsetString));
        handshakePacket.serverStatus = 0x0002;
        handshakePacket.restOfSalt = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11};

        this.write(handshakePacket);

        this.asynRead();
    }

    private void asynRead()
    {
        this.socketChannel.read(readBuffer,this,readHandler);
    }

    private void read(int get)
    {
        LOGGER.info("======get msg========"+get);
        if(get==-1)
        {
            closeSocket();
            return;
        }
    }

    private void closeSocket()
    {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(MySQLPacket packet)
    {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        packet.write(bb);
        writeQueue.offer(bb);
        this.write();
    }

    private void write()
    {
        ByteBuffer bb = null;
        while( (bb = writeQueue.poll())!=null)
        {
            bb.flip();
            this.socketChannel.write(bb,this,writeHandler);
        }
    }

    private void finishWrite()
    {

    }


    private int getServerCapabilites()
    {
        int flag = 0;
        flag |= ServerCapability.CLIENT_LONG_PASSWORD;
        flag |= ServerCapability.CLIENT_FOUND_ROWS;
        flag |= ServerCapability.CLIENT_LONG_FLAG;
        flag |= ServerCapability.CLIENT_CONNECT_WITH_DB;
        // flag |= Capabilities.CLIENT_NO_SCHEMA;
        // flag |= Capabilities.CLIENT_COMPRESS;
        flag |= ServerCapability.CLIENT_ODBC;
        // flag |= Capabilities.CLIENT_LOCAL_FILES;
        flag |= ServerCapability.CLIENT_IGNORE_SPACE;
        flag |= ServerCapability.CLIENT_PROTOCOL_41;
        flag |= ServerCapability.CLIENT_INTERACTIVE;
        // flag |= Capabilities.CLIENT_SSL;
        flag |= ServerCapability.CLIENT_IGNORE_SIGPIPE;
        flag |= ServerCapability.CLIENT_TRANSACTIONS;
        // flag |= ServerDefs.CLIENT_RESERVED;
        flag |= ServerCapability.CLIENT_SECURE_CONNECTION;
        return flag;
    }

    static class WriteHandler implements CompletionHandler<Integer, ServerConnection> {
        @Override
        public void completed(Integer result, ServerConnection attachment) {
            attachment.finishWrite();
        }

        @Override
        public void failed(Throwable exc, ServerConnection attachment) {

        }
    }

    static class ReadHandler implements CompletionHandler<Integer, ServerConnection> {
        @Override
        public void completed(Integer result, ServerConnection attachment) {
            attachment.read(result);
            attachment.asynRead();
        }

        @Override
        public void failed(Throwable exc, ServerConnection attachment) {
            attachment.asynRead();
        }
    }
}
