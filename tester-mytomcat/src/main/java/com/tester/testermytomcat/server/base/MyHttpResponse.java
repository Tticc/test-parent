package com.tester.testermytomcat.server.base;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public interface MyHttpResponse extends ServletResponse {

    void sendStaticResource() throws IOException;

    void finishResponse();
}
