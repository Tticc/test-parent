package com.tester.testerwebapp.controller.demo;

import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 温昌营
 * @Date 2021-4-1 22:28:12
 */
@Controller
@RequestMapping("/demo/reqA")
@Slf4j
public class RequestAttributeController extends BaseController {


    /**
     * RequestAttribute
     * <br/>
     * http://localhost:8004/demo/req/testRequestAttribute
     * @throws BusinessException
     */
    @GetMapping("/testRequestAttribute")
    public String testRequestAttribute(HttpServletRequest request) throws BusinessException {
        request.setAttribute("name","wenc");
        request.setAttribute("id",1);
        return "forward:requestAttributeSub";
    }
    @ResponseBody
    @GetMapping("/requestAttributeSub")
    public RestResult<String> requestAttributeSub(@RequestAttribute("name") String name,@RequestAttribute("id") Long id,HttpServletRequest request) throws BusinessException {
        StringBuilder sb = new StringBuilder();
        sb.append("name is:"+name).append(",").append(",");
        sb.append("id is:"+id).append(",").append(",");
        Object name1 = request.getAttribute("name");
        sb.append("from getAttribute:name1="+name1);
        System.out.println(sb.toString());
        return success(sb.toString());
    }






}
