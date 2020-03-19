package com.tester.testercommon.util.http.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class QywxUserExtInfo {
    String name;
    Map<String, String> text;
    int type;
}