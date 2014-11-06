package com.crazyhunter.gof.cobar.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class NIOActor {
    private final W w;
    private final R r;

    public NIOActor() throws IOException {
        this.w = new W();
        this.r = new R();
    }

    public void startup(String name)
    {
        new Thread(r,name+"-R").start();
        new Thread(r,name+"-W").start();
    }

    private class W implements Runnable{

        public W()
        {

        }

        @Override
        public void run() {

        }
    }
    private class R implements Runnable{
        private final Selector selector;
        public R() throws IOException {
            selector = Selector.open();
        }
        @Override
        public void run() {
            while (true)
            {
                final Selector selecor = this.selector;
                Set<SelectionKey> skeys = null;
                try {
                    selector.select(1000l);
                    skeys = selector.selectedKeys();
                    for (SelectionKey key:skeys)
                    {
                        if(key.isReadable())
                        {
                            read();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(skeys!=null)
                    {
                        skeys.clear();
                    }
                }
            }
        }
        private void read()
        {

        }
    }
}
