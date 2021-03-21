package com.tester.testeraop.intercepts.aop;

import com.alibaba.fastjson.JSON;
import com.tester.testeraop.intercepts.filter.RequestWrapper;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.endecrypt.RSAEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;



/**
 * 验签。
 * <br/>
 * 启动类加上注解：@ServletComponentScan
 * @Date 18:15 2021/3/15
 * @Author 温昌营
 **/
@Aspect
@Component
@Order(Integer.MIN_VALUE+1)
@Slf4j
public class SignatureAspect {

    @Value("${card.platform.publicKey:30819f300d06092a864886f70d010101050003818d003081890281810099c1da741e52942928c097625e4016268f46d8b6f8eb8f5fff3e1aa659d68a1ec84692aa3b42a77cbdbd265fd67e25eaa391fa7024d37176c97ad11f9280e679d310120926009a1bf0c2cb0af9a697513597e92f8557fd3c287f2ae4f6e1080559f87abb5a7323c75a6481fe5ad3e2a33934573c31c5cda01ab9ca4dbefe2f970203010001}")
    private String publicKey;

    @Value("${card.platform.privateKey:30820277020100300d06092a864886f70d0101010500048202613082025d0201000281810099c1da741e52942928c097625e4016268f46d8b6f8eb8f5fff3e1aa659d68a1ec84692aa3b42a77cbdbd265fd67e25eaa391fa7024d37176c97ad11f9280e679d310120926009a1bf0c2cb0af9a697513597e92f8557fd3c287f2ae4f6e1080559f87abb5a7323c75a6481fe5ad3e2a33934573c31c5cda01ab9ca4dbefe2f9702030100010281810088d215e0aa72f560c2e0ce553c656ed0954287ea1a0b13a46aef0aabe8d99e42d218eabe0cc9bd5ca542c1f91a575aca370fd9791d6c02559fe8685addf55220c7d45d038d418c2c352d51c6644569408c6ac0b96531a7ac5e22c34206e0e6ec968778f8e1a51bd586e91860b78ed084d06252c0442834625a8a669b6ccd9311024100d6ef7e64b1858a65c065ff3f88a27dd88f1fdee0592534ef6f557b50c0d8459faebae9661e32a4cc434beb2ee5c9206da058b7c8460fcbf99b566992027f4743024100b722208251be8f6faa9c10bdb1514c9edc21c933411d7d0aca5b5857c5d1ea3d9357b292058558c515a50bbea51392a71808332e9a5e067346344e6293261f1d024100ce4e8fa9a3642515c9f99d69a48323fc8c54f11fb331ad7fee03c51c1c1efba56c986893e68481feb6d1d66aeb918498f58272d5bfba45f5cf0ec0c2afb6eff502402cd18e51367e438e599bbe411e6f3e2836c991fb1839864ba8c0783b6aef77bb401f1b55bf42263a7d9499ee3b6a31def0f3b8caf3fe84e79b4bdbcf26221345024001e4c24264a6b3556764cc4d3408b1eef6cbc9ff494da300f95d2da4f42a65bff2efd1feda39b14d083ff3d6c8cf28b9a31f94575bd75b86b24518f83ffe63d9}")
    private String privateKey;

    /**
     * header签名参数 - request
     */
    public static final String REQUEST_SIGNATURE = "request-signature";


    /**
     * header签名参数 - response
     */
    public static final String RESPONSE_SIGNATURE = "response-signature";



    @Around(value = "execution(* com.tester.testeraop.controller.SignatureTestController.*(..))")
    public Object signChecker(ProceedingJoinPoint pjp) throws Throwable {
        // 获取请求数据
        Object[] args = pjp.getArgs();
        // 获取签名
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String signature = request.getHeader(REQUEST_SIGNATURE);

        // 验证签名
        boolean verify = false;
        if(request instanceof RequestWrapper){
            RequestWrapper requestWrapper = (RequestWrapper)request;
            String reqData = requestWrapper.getBody();
            verify = RSAEncrypt.verify(reqData, signature, publicKey);
        }
        Object res;
        if(verify){
            try {
                res = pjp.proceed(args);
            }catch (BusinessException be){
                res = buildExceptionRestResult(be);
            }catch (Exception e){
                BusinessException businessException = new BusinessException(5000L, e);
                res = buildExceptionRestResult(businessException);
            }
        }else {
            res = verifyFailed();
        }
        HttpServletResponse response = attributes.getResponse();
        response.setHeader(RESPONSE_SIGNATURE,RSAEncrypt.sign(JSON.toJSONString(res),privateKey));
        return res;
    }

    private RestResult buildExceptionRestResult(BusinessException businessException) {
        RestResult restResult = new RestResult();
        restResult.setCode(businessException.getExCode());
        restResult.setMessage(businessException.getExDesc());
        return restResult;
    }

    private RestResult verifyFailed(){
        // 校验失败
        RestResult<String> rest = new RestResult<>();
        rest.setCode(403);
        rest.setData("fail");
        rest.setTimestamp(new Date().getTime());
        rest.setMessage("验签失败");
        return rest;
    }

}
