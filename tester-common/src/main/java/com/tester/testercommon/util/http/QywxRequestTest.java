package com.tester.testercommon.util.http;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.util.http.userinfo.QywxUserExtInfo;
import com.tester.testercommon.util.http.userinfo.QywxUserInfoMessage;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLSocketFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date
 */
public class QywxRequestTest {

    private static String getUrl =  "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
    private static String updateUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=%s";
    private static String createUserInfoURI ="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";

    private static String deleteDeptURI = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";

    //    private static String token = "AZ2z2sNoWyYVeu9WGYIRT1Ut2eT1WGi0dQLGX-P6BUg017GGZCnw7f6nf83HxSYxLIIuNS3qQ5NpZ9RP79pqsL78tRgAT19uzIQtASzuoUW2cVGiWTBSxHYnlERQ2PhdUj-hjXmlZDYMMZQyjiIGtg5Lma4CAx4MZIy6G5QRxxiL1YooS_D6WPpFa-bcjlbtcXL_Lkkp9wKjUjBulQzz6w";
//    private static String userId = "liufei";

    private static String getToken = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static String corpId = "ww4749ac23812cffae";
    private static String contactSecret = "5JMxsYqj1VnHTT5suo4xpQG1RA19aTix_hwAp_SGJUw";
    private static String testSecret = "75eMTOfJJ2tXs3ogQZ1VMCj0InSj0yh50fP-TruHRHk";

    private static String checkInDataUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=%s";
    private static String checkInAgentId = "3010011";
    private static String checkInSecret = "bOBBdpvy0bxrEZZ27HbigAAlyxuRnJx4WmuV-WJ9lyI";

    public static SSLSocketFactory ssf;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";


    private static String hdSecret = "fGZ2tzugl2D_b27B5QR73ulkFPWdKuVDL8qlFftU2bo";
    private static String getUserURI = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
    private static String getUserURI_miapp = "https://qyapi.weixin.qq.com/cgi-bin/miniprogram/jscode2session?access_token=%s&js_code=%s&grant_type=authorization_code";

    public static void main(String[] args){
        String token = "";
        String userId = "liufei";
//        String userId = "oops";
//        token = getAccessToken(contactSecret);
//        System.out.println(token);
//        token = getAccessToken(contactSecret);

//        createUser();


        // 3.根据code获取用户信息
//        String code = "yW6kd6bw-GPrfASVKGsq0_pO2fmPGw8BlrJIYD7m0qQ";
//        JSONObject user1 = getUserByCode(code);
//        System.out.println(user1);
//        userId = String.valueOf(user1.get("userid"));
//        System.out.println(userId);

        // 1.打卡数据
//        getCheckInData();

        // 删除部门
        deleteDept();

        // 2.更新企业微信用户的手机号 - 失败
        // 获取user
//        System.out.println("\n\n");
//        JSONObject user = getUser(userId, token);
////        user.put("mobile", "188833944804");
////        user.put("position", "d当");
//        System.out.println(user.toString());
//        QywxUserInfoMessage esds = JSONObject.toJavaObject(user, QywxUserInfoMessage.class);
//        System.out.println(esds);
//        List<QywxUserExtInfo> attrs = new ArrayList<>();
//        // 公司
//        attrs.add(generateAttr("公司","永旺数字科技有限公司"));
//        attrs.add(generateAttr("公司id","50001245"));
//        // 店铺
//        attrs.add(generateAttr("店铺","天河店"));
//        attrs.add(generateAttr("店铺id","51001245"));
//        // 组织
//        attrs.add(generateAttr("组别","销售组"));
//        attrs.add(generateAttr("组别id","52001245"));
//
//
//        esds.getExtattr().put("attrs",attrs);
//        System.out.println((JSONObject)JSONObject.toJSON(esds));
//        /*JSONObject extattr = (JSONObject)user.get("extattr");
//        System.out.println(extattr);
//        JSONArray attrs = (JSONArray)extattr.get("attrs");
//        System.out.println(attrs);
//        if(!CollectionUtils.isEmpty(attrs)){
//            JSONObject one = (JSONObject)attrs.get(0);
//        }*/
//
//        // 更新user
//        System.out.println("\n\n");
//        JSONObject jsonObject = updateUser((JSONObject)JSONObject.toJSON(esds), token);
//        System.out.println(jsonObject);
    }

    private static void deleteDept(){
        String token = getAccessToken(contactSecret);
        String deptId = "1000";
        String URI = String.format(deleteDeptURI, token,deptId);
        JSONObject jsonObject = HttpsClient.commonGetRequest(URI);
        System.out.println(jsonObject);

    }

    private static void getCheckInData(){
        String token = getAccessToken(checkInSecret);
        String URI = String.format(checkInDataUrl, token);
        JSONObject obj = new JSONObject();
        obj.put("opencheckindatatype", "3");
        obj.put("starttime", "1492617600");
        obj.put("endtime", "1492790400");
        obj.put("useridlist",new ArrayList<String>());
        JSONObject jsonObject = HttpsClient.commonPostRequest(obj, URI);
        System.out.println(jsonObject);
    }

    private static void createUser(){
        String token = getAccessToken(contactSecret);
        String URI = String.format(createUserInfoURI, token);
        QywxUserInfoMessage nu = new QywxUserInfoMessage();
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(4);
        integers.add(null);
        nu.setEmail("00005@qq.com")
                .setName("ccccc")
                .setUserid("00000125")
                .setEnable(1)
                .setDepartment(integers);
        JSONObject o = (JSONObject)JSONObject.toJSON(nu);
        JSONObject jsonObject = HttpsClient.commonPostRequest(o, URI);
        System.out.println(jsonObject);

    }

    public static QywxUserExtInfo generateAttr(String name, String value){
        QywxUserExtInfo ex = new QywxUserExtInfo();
        ex.setName(name);
        ex.setType(0);
        Map<String,String> newmap = new HashMap<>();
        newmap.put("value",value);
        ex.setText(newmap);
        return ex;
    }

    public static JSONObject getUserByCode(String code){
        String token = getAccessToken(hdSecret);
//        String token = getAccessToken(contactSecret);
        String url = String.format(getUserURI_miapp,token,code);
        String wenc = HttpsClient.httpsRequestRetry(url,
                GET_METHOD,
                null,
                0,
                false,
                true,
                "getUserByCode");
        return (JSONObject)JSONObject.parse(wenc);
    }

    public static String DateToTimeStamp(String dateStr, String formats) {
        try {
            if (StringUtils.isEmpty(formats)){
                formats = "yyyyMMddHHmmss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formats);
            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getAccessToken(String secret){
        String newToken = HttpsClient.httpsRequestRetry(String.format(getToken, corpId, secret),
                GET_METHOD,
                null,
                0,
                false,
                true,
                "getToken");
        System.out.println(newToken);
        JSONObject parse1 = (JSONObject)JSONObject.parse(newToken);
        return String.valueOf(parse1.get("access_token"));
    }
    public static JSONObject getUser(String userId, String token){
        String wenc = HttpsClient.httpsRequestRetry(String.format(getUrl, token, userId),
                GET_METHOD,
                null,
                0,
                false,
                true,
                "getUser");
        return (JSONObject)JSONObject.parse(wenc);
    }
    /** InfoMessage*/
    public static JSONObject updateUser(QywxUserInfoMessage user, String token){
        String wenc1 = HttpsClient.httpsRequestRetry(String.format(updateUrl, token),
                POST_METHOD,
                user.toString(),
                0,
                false,
                true,
                "updateUser");
        return (JSONObject)JSONObject.parse(wenc1);
    }
    /** JSONObject*/
    public static JSONObject updateUser(JSONObject user, String token){
        String wenc1 = HttpsClient.httpsRequestRetry(String.format(updateUrl, token),
                POST_METHOD,
                user.toString(),
                0,
                false,
                true,
                "updateUser");
        return (JSONObject)JSONObject.parse(wenc1);
    }
}
