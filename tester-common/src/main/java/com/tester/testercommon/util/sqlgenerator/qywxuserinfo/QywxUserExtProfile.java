package com.tester.testercommon.util.sqlgenerator.qywxuserinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QywxUserExtProfile {
    String external_corp_name;
    List<QywxUserExtProfileAttr> external_attr;
}