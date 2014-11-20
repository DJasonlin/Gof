package com.crazyhunter.gof.connection.handler;

import com.crazyhunter.gof.connection.ServerConnection;
import com.crazyhunter.gof.packet.mysql.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/11/18.
 */
public class CommandServerHandler implements IServerHandler{
    private static final Logger LOGGER = Logger.getLogger(CommandServerHandler.class);

    private final ServerConnection source;

    public CommandServerHandler(ServerConnection source)
    {
        this.source = source;
    }

    @Override
    public void handle(byte[] data) {
        MySQLMessage mm = new MySQLMessage(data);
        mm.setPosition(5);
        String sql = null;
        try {
            sql = mm.readString(source.getCharset());
            LOGGER.info("========命令处理======="+sql);

            if(sql.equalsIgnoreCase("select @@version_comment limit 1"))
            {
                //返回结果集
                ResultSetHeaderPacket resultSetHeaderPacket = new ResultSetHeaderPacket();
                resultSetHeaderPacket.packetNum = 1;
                resultSetHeaderPacket.fieldCount = 1;

                FieldPacket fieldPacket = new FieldPacket();
                fieldPacket.packetNum = 2;
                fieldPacket.name = "@@version_comment".getBytes();

                EOFPacket midEofPacket = new EOFPacket();
                midEofPacket.packetNum = 3;

                RowDataPacket rowDataPacket = new RowDataPacket();
                rowDataPacket.packetNum=4;
                rowDataPacket.fieldCount=1;
                rowDataPacket.fieldValues = new ArrayList<byte[]>();
                rowDataPacket.fieldValues.add("test".getBytes());

                EOFPacket endEofPacket = new EOFPacket();
                endEofPacket.packetNum = 5;

                ResultsetPacket resultsetPacket = new ResultsetPacket(resultSetHeaderPacket,fieldPacket,midEofPacket,rowDataPacket,endEofPacket);
                source.write(resultsetPacket);
            }else
            {
                //返回结果集
                ResultSetHeaderPacket resultSetHeaderPacket = new ResultSetHeaderPacket();
                resultSetHeaderPacket.packetNum = 1;
                resultSetHeaderPacket.fieldCount = 1;

                FieldPacket fieldPacket = new FieldPacket();
                fieldPacket.packetNum = 2;
                fieldPacket.name = "Hello".getBytes();

                EOFPacket midEofPacket = new EOFPacket();
                midEofPacket.packetNum = 3;

                RowDataPacket rowDataPacket = new RowDataPacket();
                rowDataPacket.packetNum=4;
                rowDataPacket.fieldCount=1;
                rowDataPacket.fieldValues = new ArrayList<byte[]>();
                rowDataPacket.fieldValues.add(sql.getBytes());

                EOFPacket endEofPacket = new EOFPacket();
                endEofPacket.packetNum = 5;

                ResultsetPacket resultsetPacket = new ResultsetPacket(resultSetHeaderPacket,fieldPacket,midEofPacket,rowDataPacket,endEofPacket);
                source.write(resultsetPacket);
            }
        }catch (Throwable e)
        {
            LOGGER.error("===error===",e);
        }
    }
}
