package com.tester.testerwebapp.controller.demo;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testercommon.model.request.convert.ConvertRequest;
import com.tester.testercommon.model.request.convert.TestConvertRequest;
import com.tester.testercommon.util.SpringBeanContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2021-4-1 22:28:12
 */
@RestController
@RequestMapping("/demo/req")
@Slf4j
public class RequestParameterController extends BaseController {

    /**
     * url变量
     * <br/>
     * http://localhost:8004/demo/req/testPathVariable/1/wenc
     * @throws BusinessException
     */
    @GetMapping(value = "/testPathVariable/{id}/{name}")
    public RestResult<String> testPathVariable(@PathVariable("id") Long id,
                                        @PathVariable("name") String name,
                                        @PathVariable Map<String, String> pv) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("id is:"+id).append(",");
        sb.append("name is:"+name).append(",");
        sb.append("pv is:"+pv);
        System.out.println(sb.toString());
        return success(sb.toString());
    }


    /**
     * 请求头
     * <br/>
     * http://localhost:8004/demo/req/testRequestHeader
     * @throws BusinessException
     */
    @GetMapping(value = "/testRequestHeader")
    public RestResult<String> testRequestHeader(@RequestHeader("User-Agent") String userAgent,
                                                @RequestHeader Map<String, String> headers) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("userAgent is:"+userAgent).append(",").append("<br/><br/>");
        sb.append("headers is:"+headers);
        System.out.println(sb.toString());
        return success(sb.toString());
    }

    /**
     * 问号后参数
     * <br/>
     * http://localhost:8004/demo/req/testRequestParam?name=wenc&id=1&oom=out
     * @throws BusinessException
     */
    @GetMapping(value = "/testRequestParam")
    public RestResult<String> testRequestParam(@RequestParam("name") String name,
                                                @RequestParam Map<String, String> params) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("name is:"+name).append(",").append("<br/><br/>");
        sb.append("params is:"+params);
        System.out.println(sb.toString());
        return success(sb.toString());
    }
    /**
     * cookie
     * <br/>
     * http://localhost:8004/demo/req/testCookieValue
     * @throws BusinessException
     */
    @GetMapping(value = "/testCookieValue")
    public RestResult<String> testCookieValue(@CookieValue("_ga") String _ga,
                                                @CookieValue("_ga") Cookie params) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("_ga is:"+_ga).append(",").append("<br/><br/>");
        sb.append("params is:"+params);
        System.out.println(sb.toString());
        return success(sb.toString());
    }

    /**
     * RequestBody
     * <br/>
     * http://localhost:8004/demo/req/testRequestBody
     * @throws BusinessException
     */
    @PostMapping(value = "/testRequestBody")
    public RestResult<String> testRequestBody(@RequestBody @Valid TextRequest request) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("request is:"+request).append(",").append("<br/><br/>");
        System.out.println(sb.toString());
        if(true) {
            throw new BusinessException(1L);
        }
        return success(sb.toString());
    }

    /**
     * postman body类型：form-data
     * <br/>
     * @throws BusinessException
     */
    @PostMapping(value = "/testRequestBody1")
    public RestResult<String> testRequestBody1(TextRequest request) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("request is:"+request).append(",").append("<br/><br/>");
        System.out.println(sb.toString());
        return success(sb.toString());
    }

    /**
     * 这里使用了自定义的convert。
     * <br/>
     * postman 请求类型：form-data
     * 参数：
     *  testConvertRequest:wenc,12
     * <br/>
     * 配置位置：com.tester.testerwebapp.config.MyWebMvcConfigurer#addFormatters(org.springframework.format.FormatterRegistry)
     * @param request
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = "/testMyRequestConvert")
    public RestResult<String> testMyRequestConvert(ConvertRequest request) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("request is:"+request).append(",").append("<br/><br/>");
        System.out.println(sb.toString());
        return success(sb.toString());
    }


    /**
     * 测试response消息转换器
     * <br/>
     * header 设置：
     *  Accept:application/x-my-restype
     * <br/>
     * com.tester.testerwebapp.config.MyWebMvcConfigurer#extendMessageConverters(java.util.List)
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = "/testMyResponseConvert")
    public ConvertRequest testMyResponseConvert() throws BusinessException {
        ConvertRequest res = new ConvertRequest();
        TestConvertRequest resData = new TestConvertRequest();
        resData.setName("cvv");
        resData.setAge(199);
        res.setTestConvertRequest(resData);
        return res;
    }

    /**
     * 数据绑定测试
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/testRequestDataBinder")
    public RestResult<String> testRequestDataBinder() throws Exception {
        TextRequest target = new TextRequest();
        String paramName = "textRequest";
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = SpringBeanContextUtil.getBean("requestMappingHandlerAdapter", RequestMappingHandlerAdapter.class);
        WebBindingInitializer webBindingInitializer = requestMappingHandlerAdapter.getWebBindingInitializer();
        DefaultDataBinderFactory defaultDataBinderFactory = new DefaultDataBinderFactory(webBindingInitializer);



        WebDataBinder binder = defaultDataBinderFactory.createBinder(null, target, paramName);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("text","ttt");
        binder.bind(propertyValues);
        System.out.println(target);
        return success(target.toString());
    }

}
