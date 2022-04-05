package com.tester.testercv.controller;


import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.testercommon.util.SpringBeanContextUtil;
import com.tester.testercommon.util.file.MyFileReaderWriter;
import com.tester.testercv.config.OutBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;

@RestController
@RequestMapping("/cv")
@Slf4j
public class CvController extends BaseController {

    @Autowired
    private OutBean outBean;

    @PostMapping(value = "/test_context")
    public RestResult test_context(@RequestBody @Valid IdAndNameRequest model){
//        aopManager.test_param(model);
        outBean.doPrint();
        OutBean bean = SpringBeanContextUtil.getBean("containerBeanNamecontainerBeanName",OutBean.class);
        bean.doPrint();
        return success();
    }

    @PostMapping(value = "/test_spi")
    public RestResult test_spi() throws Exception {
        test_springboot_spi();
        return success();
    }
    public void test_springboot_spi() throws Exception {
        String file = "/META-INF/my.spring.factories";
        URL resource = this.getClass().getResource(file);
        String path = resource.getPath();
        byte[] bytes = MyFileReaderWriter.file2Byte(new File(path));
        String s = new String(bytes, "UTF-8");
        String[] split = s.split("=\\\\\r\n");
        System.out.println("split size:"+split.length);

        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?> aClass = classLoader.loadClass(split[1]);

        ServiceLoader<?> load = ServiceLoader.load(aClass);
        Iterator<?> iterator = load.iterator();
        while (iterator.hasNext()) {
            Object ser = iterator.next();
            System.out.println(ser);
        }
//
//        Object o = aClass.newInstance();
//        if(aClass.isAssignableFrom(SpringBeanContextUtil.class)){
//            SpringBeanContextUtil aa = (SpringBeanContextUtil)o;
//            String s1 = aa.toString();
//            System.out.println(s1);
//        }
    }
}
