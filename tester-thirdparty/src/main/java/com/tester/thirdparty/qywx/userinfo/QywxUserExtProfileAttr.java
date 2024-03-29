package com.tester.thirdparty.qywx.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class QywxUserExtProfileAttr {
    String type;
    String name;
    Map<String, String> text;
    Map<String, String> web;
    Map<String, String> miniprogram;
}