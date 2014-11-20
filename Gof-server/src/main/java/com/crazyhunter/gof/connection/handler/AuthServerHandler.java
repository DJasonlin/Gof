package com.crazyhunter.gof.connection.handler;

import com.crazyhunter.gof.config.Config;
import com.crazyhunter.gof.connection.ServerConnection;
import com.crazyhunter.gof.packet.mysql.AuthPacket;
import com.crazyhunter.gof.util.MySQLCharsetUtil;
import com.crazyhunter.gof.util.SecurityUtil;
import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2014/11/18.
 */
public class AuthServerHandler implements IServerHandler{
    private static final Logger LOGGER = Logger.getLogger(AuthServerHandler.class);
    private static final byte[] AUTH_OK = new byte[] { 7, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0 };

    private final ServerConnection source;

    public AuthServerHandler(ServerConnection source)
    {
        this.source = source;
    }

    @Override
    public void handle(byte[] data) {
        AuthPacket authPacket = new AuthPacket();
        authPacket.read(data);
        LOGGER.info("======login========="+authPacket.username+";"+authPacket.db+";");
        if(!Config.USERNAME.equals(authPacket.username))
        {
            LOGGER.error("=====用户名不正确====");
            return;
        }
        if(!Config.DBNAME.equals(authPacket.db))
        {
            LOGGER.error("=====数据库名不正确====");
            return;
        }
        if(!checkPassword(authPacket.password))
        {
            LOGGER.error("=====密码不正确====");
            return;
        }

        success(authPacket);
    }

    private void success(AuthPacket authPacket)
    {
        LOGGER.info("======认证通过，登录成功=========");

        //切换前端连接的命令处理器。
        source.setServerHandler(new CommandServerHandler(this.source));

        this.source.setCharset(MySQLCharsetUtil.getCharset(authPacket.charset));

        //返回OKPacket
        source.write(AUTH_OK);
    }

    private boolean checkPassword(byte[] password) {
        String pass = Config.PASSWORD;

        // check null
        if (pass == null || pass.length() == 0) {
            if (password == null || password.length == 0) {
                return true;
            } else {
                return false;
            }
        }
        if (password == null || password.length == 0) {
            return false;
        }
        // encrypt
        byte[] encryptPass = null;
        try {
            encryptPass = SecurityUtil.scramble411(pass.getBytes(),
                    source.getSeed());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn(source.toString(), e);
            return false;
        }
        if (encryptPass != null && (encryptPass.length == password.length)) {
            int i = encryptPass.length;
            while (i-- != 0) {
                if (encryptPass[i] != password[i]) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}

