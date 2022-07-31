package com.tester.testerswing.boot;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testerswing.capture.PointInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AccountInfo extends BaseDTO {
    // 账号alias
    private String account;

    // 截图起点
    private PointInfo st;

    // 截图终点
    private PointInfo ed;
}
