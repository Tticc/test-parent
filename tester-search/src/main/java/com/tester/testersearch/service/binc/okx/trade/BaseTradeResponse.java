package com.tester.testersearch.service.binc.okx.trade;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class BaseTradeResponse {
    /**
     * 结果代码，0表示成功
     */
    private String code;
    /**
     * 错误信息，代码为0时，该字段为空
     */
    private String msg;

    public boolean checkIfSuccess(){
        return Objects.equals("0",this.getCode());
    }
}
