package com.tester.testerswing.boot;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testerswing.capture.PointInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.atomic.AtomicInteger;

@Accessors(chain = true)
@Data
public class AccountInfo extends BaseDTO {
    // 账号alias
    private String account;

    // 警告信息
    private String warnMsg = "警告";

    // 截图起点
    private PointInfo st;

    // 截图终点
    private PointInfo ed;

    // 刷新次数
    private AtomicInteger refreshCount = new AtomicInteger(0);
}