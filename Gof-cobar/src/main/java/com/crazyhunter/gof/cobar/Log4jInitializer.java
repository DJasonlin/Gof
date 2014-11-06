package com.crazyhunter.gof.cobar;

import com.crazyhunter.gof.cobar.util.SimpleDataUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Created by Administrator on 2014/11/6.
 */
public final class Log4jInitializer {

    public static void configureAndWatch(String filename, long delay) {
        XMLWatchdog xdog = new XMLWatchdog(filename);
        xdog.setName("Log4jWatchdog");
        xdog.setDelay(delay);
        xdog.start();
    }

    private static final class XMLWatchdog extends FileWatchdog {

        public XMLWatchdog(String filename) {
            super(filename);
        }

        @Override
        public void doOnChange() {
            new DOMConfigurator().doConfigure(filename, LogManager.getLoggerRepository());
            LogLog.warn(SimpleDataUtil.getDateSimple() + " [" + filename + "] load completed.");
        }
    }
}
