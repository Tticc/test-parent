package com.tester.testerrpc.service;


import com.alibaba.fastjson.JSONObject;
import com.tester.testerrpc.annotation.RpcServiceMe;
import org.springframework.stereotype.Service;

@RpcServiceMe
@Service
public class HelloManager{

    public JSONObject getVO(){
        JSONObject res =  new JSONObject();
        res.put("nema","深圳");
        res.put("id",11);
        JSONObject ret =  (JSONObject)JSONObject.toJSON(res);
        System.out.println(ret);
        return ret;
    }
}
