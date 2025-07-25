package com.tester.testercommon.util.endecrypt.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class ApiServiceOption {

    private String baseHttpUrl;
    // if HTTP, set HTTP method.
    private String httpMethod;
    // Set service task group
    private String serviceName;
    // Set service function
    private String methodName;

    public String httpUrl() {
        return UrlConfigLoader.getUrl(serviceName, methodName);
    }
}