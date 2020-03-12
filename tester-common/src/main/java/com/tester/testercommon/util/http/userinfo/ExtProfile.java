package com.tester.testercommon.util.http.userinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ExtProfile {
    List<ExtProfileAttr> external_attr;
    String external_corp_name;
}