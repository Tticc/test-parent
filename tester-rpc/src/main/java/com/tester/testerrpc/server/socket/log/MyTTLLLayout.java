package com.tester.testerrpc.server.socket.log;

import ch.qos.logback.classic.layout.TTLLLayout;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.util.CachingDateFormatter;

/**
 * @Author 温昌营
 * @Date 2021-10-29 10:04:41
 */
public class MyTTLLLayout extends TTLLLayout {
    CachingDateFormatter cachingDateFormatter = new CachingDateFormatter("HH:mm:ss.SSS");
    ThrowableProxyConverter tpc = new ThrowableProxyConverter();

    @Override
    public String doLayout(ILoggingEvent event) {
        if (!isStarted()) {
            return CoreConstants.EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();

        long timestamp = event.getTimeStamp();

        sb.append(cachingDateFormatter.format(timestamp));
        sb.append(" [");
        sb.append(event.getThreadName());
        sb.append("] ");
        sb.append(event.getLevel().toString());
        // 不打印log类名
//        sb.append(" ");
//        sb.append(event.getLoggerName()); // com.yw.devops.tools.auto.release.service.socket.log.SocketPusher
        sb.append(" - ");
        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            String stackTrace = tpc.convert(event);
            sb.append(stackTrace);
        }
        return sb.toString();
    }
}
