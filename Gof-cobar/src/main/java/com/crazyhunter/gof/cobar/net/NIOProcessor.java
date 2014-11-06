package com.crazyhunter.gof.cobar.net;

import java.io.IOException;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class NIOProcessor {
    private final NIOActor actor;
    private final String name;


    public NIOProcessor(String name,int handlers,int executors) throws IOException {
        this.name = name;
        //初始化handler线程池

        //初始化executor线程池

        actor = new NIOActor();
    }

    public void startup()
    {
        this.actor.startup(this.name);
    }
}
