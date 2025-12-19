package com.tester.testerswing.boot;

import com.tester.base.dto.model.BaseDTO;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testerswing.capture.PointInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.opencv.core.Mat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    // 敌对 截图起点
    private PointInfoDTO enemySt;

    // 敌对 截图终点
    private PointInfoDTO enemyEd;

    // 旗舰 截图起点。同敌对
    private PointInfoDTO capitalSt;

    // 旗舰 截图终点。同敌对
    private PointInfoDTO capitalEd;

    // 本地 截图起点
    private PointInfoDTO localSt;

    // 本地 截图终点
    private PointInfoDTO localEd;

    // 刷新次数
    private AtomicInteger refreshCount = new AtomicInteger(0);

    // 异常时是否需要警告
    private boolean needWarn = false;

    // 是否自动化
    private boolean ifAuto = false;

    // 是否有警卫
    private boolean hasGuard = false;

    // 是否监控旗舰
    private boolean watchCapitalEnable = false;

    // 警卫状态。1=待命, 2=警戒中, 3=跑路
    private Integer guardStatus = 1;

    // 警戒时间
    private Date guardTime;

    // 自动回归时间
    private Long autoReturnTime;

    // 刷新次数
    private AtomicInteger guardingCount = new AtomicInteger(0);

    // 自动化跑路时，上次跑路操作时间
    private Date lastQuickRunTime;

    // 上一次检测本地账号ocr识别检测时间。(检测是否有高危账号)
    private Date lastOcrTime;

    // 自动化操作
    private MyConsumer consumer;

    // 开始警戒
    private MyConsumer toWatch;

    // 解除警戒
    private MyConsumer toStandBy;

    // 打开
    private MyConsumer openConsumer;

    // 回收
    private MyConsumer returnConsumer;

    // 刷新次数
    private Mat hisMat;

    // 提醒次数
    private AtomicInteger noticeTime = new AtomicInteger(0);

    public boolean checkIfLeader(){
        return Objects.equals(this.getSerialNo(), this.getLeaderSerialNo());
    }

    public void setAutoReturnTime(Long time){
        this.autoReturnTime = null;
    }
    public void setAutoReturnTime_ori(Long time){
        if(null == time){
            this.autoReturnTime = time;
        }
        this.autoReturnTime = time+50*60*1000;
    }

    @Getter
    @AllArgsConstructor
    public enum GuardStatusEnum {
        STAND_BY(1, "待命"),
        WATCHING_OUT(2, "警戒中"),
        RUN(3, "跑路"),
        BEFORE_STAND_BY(4, "伪待命"),
        ;
        private final int code;
        private final String text;

        public static GuardStatusEnum getByCode(Integer code){
            for (GuardStatusEnum value : values()) {
                if(Objects.equals(value.getCode(),code)){
                    return value;
                }
            }
            return STAND_BY;
        }
    }
}
