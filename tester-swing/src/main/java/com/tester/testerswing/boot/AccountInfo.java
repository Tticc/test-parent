package com.tester.testerswing.boot;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testerswing.capture.PointInfoDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicInteger;

@Accessors(chain = true)
@Data
public class AccountInfo extends BaseDTO {
    // 账号alias
    private String account;

    // 警告信息
    private String warnMsg = "警告";

    // 提醒信息
    private String infoMsg = "提醒";

    // number 截图起点
    private PointInfoDTO st;

    // number 截图终点
    private PointInfoDTO ed;

    // red 截图起点
    private PointInfoDTO redSt;

    // red 截图终点
    private PointInfoDTO redEd;

    // 刷新次数
    private AtomicInteger refreshCount = new AtomicInteger(0);

    // 异常时是否需要警告
    private boolean needWarn = true;

    // 异常时是否需要提醒
    private boolean needInfo = false;

    // 刷新次数
    private Mat hisMat;

    // 提醒次数
    private AtomicInteger noticeTime = new AtomicInteger(0);
}
