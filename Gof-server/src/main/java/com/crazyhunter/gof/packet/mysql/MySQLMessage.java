package com.crazyhunter.gof.packet.mysql;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2014/11/15.
 */
public class MySQLMessage {
    public static final long NULL_LENGTH = -1;
    private static final byte[] EMPTY_BYTES = new byte[0];

    private final byte[] data;
    private int pos = 0;
    private int length = 0;

    public MySQLMessage(byte[] data)
    {
        this.data = data;
        this.pos = 0;
        this.length = data.length;
    }

    public int readUB2()
    {
        return data[pos++]+(data[pos++]<<8);
    }

    public int readUB3()
    {
        return data[pos++]+(data[pos++]<<8)+(data[pos++]<<16);
    }

    public int readUB4()
    {
        return data[pos++]+(data[pos++]<<8)+(data[pos++]<<16)+(data[pos++]<<24);
    }

    public byte read()
    {
        return  data[pos++];
    }

    public long readLength() {
        int length = data[pos++] & 0xff;
        switch (length) {
            case 251:
                return NULL_LENGTH;
            case 252:
                return readUB2();
            case 253:
                return readUB3();
            case 254:
                return readLong();
            default:
                return length;
        }
    }

    public long readLong()
    {
        long l = (long) (data[pos++] & 0xff);
        l |= (long) (data[pos++] & 0xff) << 8;
        l |= (long) (data[pos++] & 0xff) << 16;
        l |= (long) (data[pos++] & 0xff) << 24;
        l |= (long) (data[pos++] & 0xff) << 32;
        l |= (long) (data[pos++] & 0xff) << 40;
        l |= (long) (data[pos++] & 0xff) << 48;
        l |= (long) (data[pos++] & 0xff) << 56;
        return l;
    }

    public String readStringWithNull()
    {
        final byte[] b = this.data;
        if (pos >= length) {
            return null;
        }
        int offset = -1;
        for (int i = pos; i < length; i++) {
            if (b[i] == 0) {
                offset = i;
                break;
            }
        }
        if (offset == -1) {
            String s = new String(b, pos, length - pos);
            pos = length;
            return s;
        }
        if (offset > pos) {
            String s = new String(b, pos, offset - pos);
            pos = offset + 1;
            return s;
        } else {
            pos++;
            return null;
        }
    }

    public byte[] readBytesWithLength()
    {
        int length = (int) readLength();
        if (length < 0) {
            return null;
        }
        byte[] ab = new byte[length];
        System.arraycopy(data, pos, ab, 0, ab.length);
        pos += length;
        return ab;
    }

    public String readString(String charset)
    {
        String s = "";
        try {
            s = new String(data, pos, length - pos,charset);
            pos = length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public int getPosition()
    {
        return pos;
    }

    public void setPosition(int pos)
    {
       this.pos = pos;
    }

    public byte[] bytes(){
        return data;
    }

    public boolean hasRemaining()
    {
        return this.length>this.pos;
    }

}
