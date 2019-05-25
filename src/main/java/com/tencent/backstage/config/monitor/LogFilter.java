package com.tencent.backstage.config.monitor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/24
 * Time:14:39
 * 定义输出日志的过滤器
 */
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        LogMessage logMessage = new LogMessage(
                iLoggingEvent.getFormattedMessage(),
                DateFormat.getDateTimeInstance().format(new Date(iLoggingEvent.getTimeStamp())),
                iLoggingEvent.getThreadName(),
                iLoggingEvent.getLoggerName(),
                iLoggingEvent.getLevel().levelStr
        );
        LoggerQueue.getInstance().push(logMessage);
        return  FilterReply.ACCEPT;
    }
}
