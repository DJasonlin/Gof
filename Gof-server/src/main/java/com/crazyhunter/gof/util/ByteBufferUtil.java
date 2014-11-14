package com.crazyhunter.gof.util;

import java.nio.ByteBuffer;

/**
 * byteBuffer写入工具类
 */
public final class ByteBufferUtil {
    public static void writeUB2(ByteBuffer bb,int val)
    {
        bb.put((byte)val);
        bb.put((byte)(val>>>8));
    }
    public static void writeUB3(ByteBuffer bb,int val)
    {
        bb.put((byte)val);
        bb.put((byte)(val>>>8));
        bb.put((byte)(val>>>16));
    }
    public static void writeUB4(ByteBuffer bb,long val)
    {
        bb.put((byte)val);
        bb.put((byte)(val>>>8));
        bb.put((byte)(val>>>16));
        bb.put((byte)(val>>>24));
    }
}
