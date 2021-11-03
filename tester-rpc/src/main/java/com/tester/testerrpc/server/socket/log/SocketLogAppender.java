package com.tester.testerrpc.server.socket.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.status.ErrorStatus;
import com.tester.testercommon.constant.ConstantList;

public class SocketLogAppender<E> extends OutputStreamAppender<E> {

    public SocketLogAppender(String name) {
        this.name = name;
    }

    @Override
    protected void append(E e) {
        ILoggingEvent ele = (ILoggingEvent)e;
        String traceId = ele.getMDCPropertyMap().get(ConstantList.MDC_TRACE_ID_KEY);
        byte[] byteArray = this.encoder.encode(e);
        SocketPusher.putEle(traceId, new String(byteArray));
    }

    @Override
    public void start() {
        int errors = 0;
        if (this.encoder == null) {
            addStatus(new ErrorStatus("No encoder set for the appender named \"" + name + "\".", this));
            errors++;
        }

        // 不需要 outputStream。 数据将会由 append 方法转换为String，再写出去
//        if (this.outputStream == null) {
//            addStatus(new ErrorStatus("No output stream set for the appender named \"" + name + "\".", this));
//            errors++;
//        }
        // only error free appenders should be activated
        if (errors == 0) {
            started = true;
        }
    }
}
