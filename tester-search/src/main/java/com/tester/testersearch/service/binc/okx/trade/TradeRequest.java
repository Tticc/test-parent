package com.tester.testersearch.service.binc.okx.trade;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TradeRequest {
    @ApiModelProperty(name = "instId", value = "币id。如BTC-USDT-SWAP", example = "BTC-USDT-SWAP", required = true)
    private String instId;
    @ApiModelProperty(name = "tdMode", value = "交易模式.cross", required = true)
    private String tdMode;
//    @ApiModelProperty(name = "ccy", value = "保证金货币", required = false)
//    private String ccy;
//    @ApiModelProperty(name = "clOrdId", value = "客户端分配的客户端订单 ID", required = false)
//    private String clOrdId;
//    @ApiModelProperty(name = "tag", value = "订单标签", required = false)
//    private String tag;
    @ApiModelProperty(name = "side", value = "订单方。buy sell", required = true)
    private String side;
    // 有条件的
    @ApiModelProperty(name = "posSide", value = "位置侧。仅适用于 FUTURES/SWAP。", example = "long or short", required = true)
    private String posSide;
    @ApiModelProperty(name = "ordType", value = "订单类型", example = "post_only", required = true)
    private String ordType;
    @ApiModelProperty(name = "sz", value = "买入或卖出数量", example = "0.01", required = true)
    private String sz;
    @ApiModelProperty(name = "px", value = "订单价格", example = "95000", required = true)
    private String px;
}
