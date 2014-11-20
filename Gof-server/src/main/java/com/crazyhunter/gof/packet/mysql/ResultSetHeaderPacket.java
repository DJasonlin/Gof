package com.crazyhunter.gof.packet.mysql;

import com.crazyhunter.gof.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2014/11/18.
 */
public class ResultSetHeaderPacket extends MySQLPacket{
    public int fieldCount;

    @Override
    public void write(ByteBuffer bb) {
        ByteBufferUtil.writeUB3(bb, getLength());
        bb.put(packetNum);
        ByteBufferUtil.writeLength(bb,fieldCount);
    }

    @Override
    public int getLength() {
        return ByteBufferUtil.getLength(fieldCount);
    }

    @Override
    public void read(byte[] data) {

    }
}
