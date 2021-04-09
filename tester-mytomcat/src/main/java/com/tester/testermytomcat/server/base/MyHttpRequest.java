package com.tester.testermytomcat.server.base;

import javax.servlet.ServletRequest;

public interface MyHttpRequest extends ServletRequest {
    String getUri();
}
