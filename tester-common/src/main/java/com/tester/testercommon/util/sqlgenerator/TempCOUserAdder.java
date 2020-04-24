package com.tester.testercommon.util.sqlgenerator;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.util.sqlgenerator.qywxuserinfo.QywxSingleUserInfoBO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
public class TempCOUserAdder {

    /** 创建企业微信用户信息的URI*/
    private static String createUserInfoURI = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";

    /** 获取企业微信token的url*/
    private static String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    private static String corpId = "ww4749ac23812cffae";// test
//    private static String corpId = "wxefcccbb736b34cd5";// prod
    private static String secret = "5JMxsYqj1VnHTT5suo4xpQG1RA19aTix_hwAp_SGJUw";// test
//    private static String secret = "1QttF84qBPVKju2RsTdAA8xXaAJgMvjeLyI2kb-oTZk";// prod

    private static List<Integer> depts = Arrays.asList(133);// test
//    private static List<Integer> depts = Arrays.asList(366);// prod

    private static List<Integer> isLeader = Arrays.asList(0);

    public static void main(String[] args) {
        // 0. 设置部门 depts，corpId、secret到生产数据！！！
        // 1.设置ifPush=false,复制岗位的人员数据sql，generateAndPushUser
        // 1.1 拿到sql后，先执行查询，看看提供的ext_person_id和wechatid是否已存在
        // 1.2 不存在则执行sql
        // 2.找到组织
        // 3.找到组织对应的企业微信部门id
        // 4.找到组织的一个岗位StationOrg
        // 5.执行printStationOrgSQL
        // 6.设置ifPush=true,插入qywx
        boolean ifPush = false;
        generateAndPushUser(ifPush);
//        printStationOrgSQL();
    }
    public static void generateAndPushUser(boolean ifPush){
        StringBuilder sb = new StringBuilder();
        List<QywxSingleUserInfoBO> newQywxUsers = new ArrayList<>();
        generateInsertSQL(newQywxUsers);
        for (QywxSingleUserInfoBO newQywxUser : newQywxUsers) {
            JSONObject userToQywx = createUserToQywx(newQywxUser,ifPush);
            sb.append(userToQywx).append("\n");
        }
        if(ifPush){
            System.out.println("\npush message print here:");
            System.out.println(sb.toString());
        }
    }

    private static void printStationOrgSQL(){
        String s = generateStationOrgSQL();
        System.out.println();
        System.out.println(s);
    }


    private static void generateInsertSQL(List<QywxSingleUserInfoBO> newQywxUsers){
        String[] split = getStr().split("\\n");
        System.out.println("userLength:"+split.length+"\n");
        StringBuilder sbInsert = new StringBuilder();
        StringBuilder sbSearcha = new StringBuilder();
        StringBuilder sbSearchMap = new StringBuilder();
        sbSearchMap.append("select uso.* from u_person as up left join u_station_org as uso on up.id = uso.user_id where up.ext_person_id in (");
        sbSearcha.append("select * from u_person where ext_person_id in (");
        for (String s : split) {
            String[] split1 = s.split(",");
            sbInsert.append("INSERT INTO `cloud_office`.`u_person`(`name`, `ext_person_id`, `employee_id`, `cellphone`, `gender`, `email`, `enname`, `wechatid`, `status`, `note`, `create_time`, `update_time`, `revision`, `deleted`, `data_from`, `main_corp_id`, `badge`, `emp_sub_group_id`) VALUES (")
                    .append("'").append(split1[0]).append("'")
                    .append(", '").append(split1[1]).append("'")
                    .append(", '").append(split1[1]).append("'")
                    .append(", '").append(split1[2]).append("'")
                    .append(", ").append(Objects.equals(split1[5],"女")?0:1)
                    .append(", '', '', ")
                    .append("'").append(split1[1]).append("'")
                    .append(", 3, NULL, SYSDATE(), SYSDATE(), 0, 0, 99, 0, ")
                    .append("'").append(split1[1]).append("'")
                    .append(", null);\n");
            sbSearcha.append("'").append(split1[1]).append("',");
            sbSearchMap.append("'").append(split1[1]).append("',");
            newQywxUsers.add(generateQywxUserInfo(split1[1],split1[0],split1[2],split1[5]));
        }
        sbSearcha.replace(sbSearcha.length()-1,sbSearcha.length(),"");
        sbSearchMap.replace(sbSearchMap.length()-1,sbSearchMap.length(),"");
        sbSearcha.append(");");
        sbSearchMap.append(") and uso.deleted = 0;\n");
        System.out.println(sbInsert.toString());
        System.out.println("\n\n\n");
        System.out.println(sbSearcha.toString());
        System.out.println("\n\n\n");
        System.out.println(sbSearchMap.toString());
    }

    private static String generateStationOrgSQL(){
        String[] split = getUserIds().split("\\n");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append("INSERT INTO `cloud_office`.`u_station_org`(`station_id`, `user_id`, `org_id`, `parent_org_id`, `org_name`, `org_owner`, `shop_id`, `corp_id`, `business_type`, `up_station_id`, `status`, `note`, `create_time`, `update_time`, `revision`, `deleted`, `data_from`) VALUES (24476, ")
                    .append(s)
                    .append(", 4656, '3556', '顾客服务&电子商务', NULL, 3552, 3176, '02', NULL, 1, NULL, SYSDATE(), SYSDATE(), 0, 0, 99);\n");
        }
        return sb.toString();
    }

    private static JSONObject createUserToQywx(QywxSingleUserInfoBO user, boolean ifPush){
        if(!ifPush){
            return new JSONObject();
        }
        String accessToken = getQywxTokenWithoutOuterService();
        String URI = createUserInfoURI;
        return httpPostRequest((JSONObject)JSONObject.toJSON(user),URI,accessToken);
    }

    private static JSONObject httpPostRequest(JSONObject content, String url, String token){
        RestTemplate restTemplate = new RestTemplate();
        int maxTime = 3;
        int count = 0;
        boolean failed = true;
        String URI = String.format(url, token);
        ResponseEntity<JSONObject> httpResult;
        do{
            httpResult = restTemplate.postForEntity(URI, content, JSONObject.class);
            if(httpResult.getStatusCodeValue() == 200){
                if(!Objects.equals(String.valueOf(httpResult.getBody().get("errcode")),"40014")){
                    // token 不过期。设置failed=false，跳出循环
                    failed = false;
                }else{
                    URI = String.format(url, getQywxTokenWithoutOuterService());
                }
            }
        }while(failed && maxTime > ++count);
        return httpResult.getBody();
    }

    private static String getQywxTokenWithoutOuterService(){
        RestTemplate restTemplate = new RestTemplate();
        String url = getTokenUrl.format(getTokenUrl, corpId, secret);
        ResponseEntity<JSONObject> httpResult = null;
        int count = 1;
        do{
            httpResult = restTemplate.getForEntity(url, JSONObject.class);
            if(httpResult.getStatusCodeValue() == 200){
                if(Objects.equals("0",String.valueOf(httpResult.getBody().get("errcode")))){
                    return String.valueOf(httpResult.getBody().get("access_token"));
                }
            }
            httpResult = null;
            ++count;
        }while(null == httpResult && count < 3);
        return "";
    }

    private static QywxSingleUserInfoBO generateQywxUserInfo(String userId, String userName, String mobile, String gender){
        QywxSingleUserInfoBO newOne = new QywxSingleUserInfoBO();
        newOne.setUserid(userId)
                .setDepartment(depts)
                .setGender(Objects.equals(gender,"男")?1:2)
                .setIs_leader_in_dept(isLeader)
                .setMobile(mobile)
                .setName(userName)
                .setEnable(1)
                .setAlias(null)
                // order 先设置为null,如果不行，调整到 setUserQywxDept() 方法
                .setOrder(null)
                .setPosition(null)
                .setTelephone(null)
                .setExternal_profile(null)
                .setExternal_position(null)
                .setAddress(null);
        return newOne;
    }





    private static String getUserIds(){
        return "43560\n" +
                "43561\n" +
                "43562\n" +
                "43563\n" +
                "43564\n" +
                "43565\n" +
                "43566\n" +
                "43567\n" +
                "43568\n" +
                "43569\n" +
                "43570\n" +
                "43571\n" +
                "43572";
    }


    private static String getStr(){
        return "陈  容,1900635,15133196102,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "彭紫涵,1900636,13131895029,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "王祎萌,1900637,17367934741,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "张思雨,1900639,17310778186,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "李甜源,1900640,15032818538,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "薛雅洁,1900642,15133109530,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "廉丝雨,1900644,13230195462,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "许素瑾,1900648,17330095670,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "郝高欢,1900655,18233189657,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "颜锡尧,1900657,17303308363,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "苏粢钰,1900659,13012148386,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "刘晓萌,1900660,19943425261,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女\n" +
                "武瑞婷,1900652,15732907973,cindy_du@aeonbj.com,永旺商业有限公司北京丰台分公司->行政部>顾客服务&电子商务,女";
    }

}
