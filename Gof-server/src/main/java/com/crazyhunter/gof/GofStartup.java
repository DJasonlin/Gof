package com.crazyhunter.gof;

/**
 * 程序启动入口
 */
public final class GofStartup {
    public static void main(String[] args)
    {
        try {
            GofServer gofServer = GofServer.getInstance();
            gofServer.beforeStart();
            gofServer.startup();

            while (true)
            {
                Thread.sleep(30000);
            }
        }catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
