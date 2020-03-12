package com.tester.testercommon.util.http.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class UserInfoMessage {
        int errcode;
        String errmsg;
        int isleader;
        List<Integer> is_leader_in_dept;
        String address;
        int gender;
        String mobile;
        String telephone;
        String avatar;
        int hide_mobile;
        String userid;
        String thumb_avatar;
        int enable;
        String name;
        Map<String, List<ExtInfo>> extattr;
//        String extattr;
        String qr_code;
        String alias;
        String position;
        List<Integer> department;
//        String external_profile;
        ExtProfile external_profile;
        String email;
        int status;
        List<Integer> order;
    }