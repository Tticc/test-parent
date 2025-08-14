package com.tester.testersearch.service.binc.okx.trade;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString(callSuper=true)
@Data
public class TickerTradeResponse extends BaseTradeResponse{
    private List<TickerInfo> data;

    @Data
    @Accessors(chain = true)
    public static class TickerInfo{
        //产品类型
        private String instType;
        //产品ID
        private String instId;
        //最新成交价
        private String last;
        //最新成交的数量，0 代表没有成交量
        private String lastSz;
        //24小时开盘价
        private String open24h;
        //24小时最低价
        private String low24h;
        //24小时最高价
        private String high24h;
        //24小时成交量，以币为单位
        private String volCcy24h;
        //24小时成交量，以张为单位
        private String vol24h;
        //UTC+0 时开盘价
        private String sodUtc0;
        //UTC+8 时开盘价
        private String sodUtc8;

        //卖一价对应的数量
        private String askSz;
        //卖一价
        private String askPx;
        //买一价对应的数量
        private String bidSz;
        //买一价
        private String bidPx;

        private Long ts;
    }
}
