package com.tester.testerswing.boot;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testerswing.capture.PointInfoDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.opencv.core.Mat;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Accessors(chain = true)
@Data
public class AccountInfo extends BaseDTO {

    // 序号。从1开始
    private Integer serialNo;

    // leader序号。默认为自己的序号
    private Integer leaderSerialNo;

    // 是否环绕残骸。默认为false
    private Boolean arroundTrash = false;

    // 所有follow。自己也是自己的follow
//    private List<AccountInfo> follows = new ArrayList<>();
    private Map<Integer, AccountInfo> follows = new HashMap<>();

    // 账号alias
    private String account;

    // 警告信息
    private String warnMsg = "警告";

    // red 截图起点
    private PointInfoDTO redSt;

    // red 截图终点
    private PointInfoDTO redEd;

    // 刷新次数
    private AtomicInteger refreshCount = new AtomicInteger(0);

    // 异常时是否需要警告
    private boolean needWarn = false;

    // 异常时是否需要提醒
    private boolean needInfo = false;

    // 是否自动化
    private boolean ifAuto = false;

    // 自动化跑路时，上次跑路操作时间
    private Date lastQuickRunTime;

    // 自动化操作
    private MyConsumer consumer;

    // 刷新次数
//    private Mat hisMat;

    // 提醒次数
    private AtomicInteger noticeTime = new AtomicInteger(0);
}
