package com.tester.testerswing.voice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.http.HttpsClient;
import com.tester.testerswing.voice.dto.QywxMessageTaskDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author 温昌营
 * @Date 2022-10-8 16:00:16
 */
public class QywxHelper {

    // 获取应用token url
    private static String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    // 发送应用消息url
    private static String sendMsg = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    // 企业id
    private static String corpId = "ww56183d9cb492fef2";

    // 应用secret
    private static String appSecret = "LOIpmQ14mlRGXdincnFXBS2RShx8D6LEsCspPDbGQZQ";

    public static TokenHolder tokenHolder = new TokenHolder();


    public static void main(String[] args) throws BusinessException {
        send(new QywxMessageTaskDTO(new Date().toString()));
    }

    public static void send(QywxMessageTaskDTO message) {
        try {
            String token = getToken();
            if (StringUtils.isEmpty(token)) {
                return;
            }
            String s = JSON.toJSONString(message.getMessage());
            JSONObject jsonObject = JSONObject.parseObject(s, JSONObject.class);
            HttpsClient.commonHttpsPostRequest(String.format(sendMsg, token), jsonObject);
        } catch (Exception e) {

        }
    }


    public static String getToken() throws BusinessException {
        Long currentTime = new Date().getTime();
        String token;
        if (currentTime > tokenHolder.getTime()) {
            String format = String.format(getTokenUrl, corpId, appSecret);
            JSONObject jsonObject = HttpsClient.commonHttpsGetRequest(format);
            token = (String) jsonObject.get("access_token");
            tokenHolder.setTime(currentTime + 7000 * 1000);
        } else {
            token = tokenHolder.getToken();
        }
        return token;
    }

    @Accessors(chain = true)
    @Data
    static class TokenHolder {
        private Long time = 0L;
        private String token = "";
    }
}
