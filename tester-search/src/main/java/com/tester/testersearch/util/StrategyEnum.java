package com.tester.testersearch.util;

import com.tester.testersearch.service.binc.strategy.TradeParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public enum StrategyEnum {
    _1000010("_1000010", newTradeParam("_1000010",e -> e.setStep(35).setSlTimes(new BigDecimal("0.005")))),
    _1000020("_1000020", newTradeParam("_1000020",e -> e.setStep(35).setSlTimes(new BigDecimal("0.0075")))),
    _1000030("_1000030", newTradeParam("_1000030",e -> e.setStep(35).setSlTimes(new BigDecimal("0.01")))),
    _1000040("_1000040", newTradeParam("_1000040",e -> e.setStep(35).setSlTimes(new BigDecimal("0.015")))),

    _1000050("_1000050", newTradeParam("_1000050",e -> e.setStep(65).setSlTimes(new BigDecimal("0.005")))),
    _1000060("_1000060", newTradeParam("_1000060",e -> e.setStep(65).setSlTimes(new BigDecimal("0.0075")))),
    _1000070("_1000070", newTradeParam("_1000070",e -> e.setStep(65).setSlTimes(new BigDecimal("0.01")))),
    _1000080("_1000080", newTradeParam("_1000080",e -> e.setStep(65).setSlTimes(new BigDecimal("0.015")))),

    _1000090("_1000090", newTradeParam("_1000090",e -> e.setStep(95).setSlTimes(new BigDecimal("0.005")))),
    _1000100("_1000100", newTradeParam("_1000100",e -> e.setStep(95).setSlTimes(new BigDecimal("0.0075")))),
    _1000110("_1000110", newTradeParam("_1000110",e -> e.setStep(95).setSlTimes(new BigDecimal("0.01")))),
    _1000120("_1000120", newTradeParam("_1000120",e -> e.setStep(95).setSlTimes(new BigDecimal("0.015")))),

    _1000130("_1000130", newTradeParam("_1000130",e -> e.setStep(135).setSlTimes(new BigDecimal("0.005")))),
    _1000140("_1000140", newTradeParam("_1000140",e -> e.setStep(135).setSlTimes(new BigDecimal("0.0075")))),
    _1000150("_1000150", newTradeParam("_1000150",e -> e.setStep(135).setSlTimes(new BigDecimal("0.01")))),
    _1000160("_1000160", newTradeParam("_1000160",e -> e.setStep(135).setSlTimes(new BigDecimal("0.015")))),

    ;
    private final String code;
    private final TradeParam param;

    public static StrategyEnum getByCode(String code) {
        for (StrategyEnum value : values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return _1000010;
    }

    private static TradeParam newTradeParam(String code, Consumer<TradeParam> consumer) {
        TradeParam tradeParam = new TradeParam()
                .setStrategyCode(code)
                .setBarEnum(BarEnum._30m)
                .setStep(35)
                .setSkipAfterHuge(10).setKeepSkipAfterHuge(10)
                .setSlTimes(new BigDecimal("0.005"))
                .setTpTimes(new BigDecimal("0.07"))
                .setSkipTimes(new BigDecimal("0.012"))
                .setSkipByProfits(1)
                .setExcludeLast(true)
                .setSkipFakeMa(true)

                .setReverseSlTimes(new BigDecimal("0.005"))
                .setReverseTpTimes(new BigDecimal("0.01"))
                .setReverseSkipNum(0)
                .setReverseTakeNum(0);
        consumer.accept(tradeParam);
        return tradeParam;
    }
}