package com.tester.testerwebapp.controller.common;


import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.util.qywx.wxdecode.WXBizMsgCrypt;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 供企业微信回调
 *
 * @author 温昌营
 * @date 2019/11/19
 */
@Api(tags = "企业微信配置接口")
@Slf4j
@RequestMapping("/qywx/api")
@RestController
public class QywxController extends BaseController {

    // 应用设置的token。可以自动生成
    private String token = "";
    // 应用设置的aesKey。可以自动生成
    private String aesKey = "";
    // 公司id
    private String corpId = "";

    // https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww56183d9cb492fef2&corpsecret=LOIpmQ14mlRGXdincnFXBS2RShx8D6LEsCspPDbGQZQ

    /**
     * 验证回调URL - GET。
     * <p>
     * 企业微信应用在设置 接收消息 时需要 设置API接收。在填写URL(http://domain/cloudoffice/qywx/api/callback)后点击保存时，企业微信会使用这个URL进行验证。
     * </p>
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void verifyCallbackURL(HttpServletRequest request, HttpServletResponse response) throws Exception {

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, aesKey, corpId);
        // 微信加密签名
        String sVerifyMsgSig = request.getParameter("msg_signature");
        // 时间戳
        String sVerifyTimeStamp = request.getParameter("timestamp");
        // 随机数
        String sVerifyNonce = request.getParameter("nonce");
        // 随机字符串
        String sVerifyEchoStr = request.getParameter("echostr");
        // 回调key值
        String sEchoStr;
        try {
            PrintWriter out = response.getWriter();
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
            if (!StringUtils.isNotBlank(sEchoStr)) {
                log.error("企业微信签名失败!");
            }
            out.write(sEchoStr);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息回调URL - POST。企业微信主动推动消息时会调用这个POST方法，暂时设置为过期
     *
     * @param request
     * @return void
     * @throws
     * @author 温昌营
     * @date 2019/11/19
     */
    @Deprecated
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void acceptMessage(HttpServletRequest request) throws Exception {
        // do nothing
    }


}
