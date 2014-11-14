package com.crazyhunter.gof.packet.mysql;

import java.nio.ByteBuffer;

/**
 * MySQL数据包基类
 */
public abstract  class MySQLPacket {

    public static byte NULL_BYTE = 0x00;

    public int packetLen;
    public byte packetNum;

    public abstract void write(ByteBuffer bb);

    public abstract int getLength();
}

