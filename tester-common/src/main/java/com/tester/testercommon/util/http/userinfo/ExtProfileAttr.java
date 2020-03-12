package com.tester.testercommon.util.http.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ExtProfileAttr {
    String type;
    String name;
    Map<String, String> text;
    Map<String, String> web;
    Map<String, String> miniprogram;
}