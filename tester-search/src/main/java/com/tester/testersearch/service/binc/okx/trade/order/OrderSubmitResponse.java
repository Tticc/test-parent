package com.tester.testersearch.service.binc.okx.trade.order;

import com.tester.testersearch.service.binc.okx.trade.BaseTradeResponse;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class OrderSubmitResponse extends BaseTradeResponse {
    //REST网关接收请求时的时间戳，Unix时间戳的微秒数格式，如 1597026383085123 返回的时间是请求验证后的时间。
    private Long inTime;
    //REST网关发送响应时的时间戳，Unix时间戳的微秒数格式，如 1597026383085123
    private Long outTime;
    // 包含结果的对象数组
    private List<OrderData> data;
    @Data
    public static class OrderData {
        //客户自定义订单ID
        private String clOrdId;
        //订单ID
        private String ordId;
        //事件执行结果的code，0代表成功
        private String sCode;
        //事件执行失败或成功时的msg
        private String sMsg;
        //订单标签
        private String tag;
        //系统完成订单请求处理的时间戳，Unix时间戳的毫秒数格式，如 1597026383085
        private Long ts;
    }
}
