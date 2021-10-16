package me.khanghoang.multicurrency.log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author khanghh on 2021/05/23
 */
public class MyLogger {
    
    private static final Map<String, MyLogger> loggerMap = new HashMap<>();
    
    private static LogLevel logLevel = LogLevel.INFO;
    
    private static Logger writer;

    public static MyLogger getLogger(String tag) {
        if (loggerMap.containsKey(tag)) {
            return loggerMap.get(tag);
        }
        loggerMap.put(tag, new MyLogger(tag));
        return loggerMap.get(tag);
    }
    
    public static void setLogLevel(LogLevel level) {
        MyLogger.logLevel = level;
    }
    
    public static void setWriter(Logger writer) {
        MyLogger.writer = writer;
    }
    
    private final String tag;
   
    private MyLogger(String tag) {
        this.tag = String.format("[%s] ", tag);
    }
    
    public void error(String msg, Throwable err) {
        if (logLevel.ordinal() >= LogLevel.ERROR.ordinal()) {
            writer.log(Level.SEVERE, tag + msg, err);
        }
    }

    public void warn(String format, Object... args) {
        if (logLevel.ordinal() >= LogLevel.WARN.ordinal()) {
            writer.warning(tag + String.format(format, args));
        }
    }

    public void info(String format, Object... args) {
        if (logLevel.ordinal() >= LogLevel.INFO.ordinal()) {
            writer.info(tag + String.format(format, args));
        }
    }
    
    public void debug(String format, Object... args) {
        if (logLevel.ordinal() >= LogLevel.DEBUG.ordinal()) {
            writer.info(tag + String.format(format, args));
        }
    }
    
    public void verbose(String format, Object... args) {
        if (logLevel.ordinal() >= LogLevel.VERBOSE.ordinal()) {
            writer.info(tag + String.format(format, args));
        }
    }
}
