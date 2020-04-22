package com.tester.testercommon.util.sqlgenerator.qywxuserinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class QywxUserExtProfileAttr {
    private int type;
    private String name;
    private Map<String, String> text;
    private Map<String, String> web;
    private Map<String, String> miniprogram;
}