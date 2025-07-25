package com.tester.testercommon.util.endecrypt.http.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.PagerInfo;
import com.tester.testercommon.util.endecrypt.http.ApiServiceOption;
import com.tester.testercommon.util.endecrypt.http.BaseApi;

public class SearchApi extends BaseApi {
    public SearchApi() {
        super.serviceOption = ApiServiceOption.builder()
                .httpMethod("post")
                .serviceName("tester-search")
                .build();
    }

    public RestResult<PagerInfo<Object>> search(Object param) {
        super.serviceOption.setMethodName("search");
        return super.call(param, new TypeReference<RestResult<PagerInfo<Object>>>() {});
    }

    public static void main(String[] args) {
        JSONObject params = new JSONObject();
        params.put("all", "2.11.0");
        params.put("type", "1");
        params.put("pageNum", 1);
        params.put("pageSize", 5);
        SearchApi api = new SearchApi();
        RestResult<PagerInfo<Object>> search = api.search(params);
        System.out.println("search = " + search);
    }
}