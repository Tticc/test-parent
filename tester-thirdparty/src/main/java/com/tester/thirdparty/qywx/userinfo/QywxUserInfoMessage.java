package com.tester.thirdparty.qywx.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class QywxUserInfoMessage {
        int errcode;
        String errmsg;
        String userid;
        String name;
        List<Integer> department;
        List<Integer> order;
        String position;
        String mobile;
        int gender;
        String email;
        int isleader;
        List<Integer> is_leader_in_dept;
        String avatar;
        String thumb_avatar;
        String telephone;
        int enable;
        String alias;
        String address;
        int hide_mobile;
        //        String extattr;
        Map<String, List<QywxUserExtInfo>> extattr;
        int status;
        String qr_code;
        private String external_position;
        //        String external_profile;
        QywxUserExtProfile external_profile;
    }