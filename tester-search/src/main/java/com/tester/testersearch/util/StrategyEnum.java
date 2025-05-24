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

    _1000170("_1000170", newTradeParam("_1000170",e -> e.setStep(35).setSlTimes(new BigDecimal("0.005")).setMaShort(7).setMaLong(21))),
    _1000180("_1000180", newTradeParam("_1000180",e -> e.setStep(35).setSlTimes(new BigDecimal("0.0075")).setMaShort(7).setMaLong(21))),
    _1000190("_1000190", newTradeParam("_1000190",e -> e.setStep(35).setSlTimes(new BigDecimal("0.01")).setMaShort(7).setMaLong(21))),
    _1000200("_1000200", newTradeParam("_1000200",e -> e.setStep(35).setSlTimes(new BigDecimal("0.015")).setMaShort(7).setMaLong(21))),

    _1000210("_1000210", newTradeParam("_1000210",e -> e.setStep(65).setSlTimes(new BigDecimal("0.005")).setMaShort(7).setMaLong(21))),
    _1000220("_1000220", newTradeParam("_1000220",e -> e.setStep(65).setSlTimes(new BigDecimal("0.0075")).setMaShort(7).setMaLong(21))),
    _1000230("_1000230", newTradeParam("_1000230",e -> e.setStep(65).setSlTimes(new BigDecimal("0.01")).setMaShort(7).setMaLong(21))),
    _1000240("_1000240", newTradeParam("_1000240",e -> e.setStep(65).setSlTimes(new BigDecimal("0.015")).setMaShort(7).setMaLong(21))),

    _1000250("_1000250", newTradeParam("_1000250",e -> e.setStep(95).setSlTimes(new BigDecimal("0.005")).setMaShort(7).setMaLong(21))),
    _1000260("_1000260", newTradeParam("_1000260",e -> e.setStep(95).setSlTimes(new BigDecimal("0.0075")).setMaShort(7).setMaLong(21))),
    _1000270("_1000270", newTradeParam("_1000270",e -> e.setStep(95).setSlTimes(new BigDecimal("0.01")).setMaShort(7).setMaLong(21))),
    _1000280("_1000280", newTradeParam("_1000280",e -> e.setStep(95).setSlTimes(new BigDecimal("0.015")).setMaShort(7).setMaLong(21))),

    _1000290("_1000290", newTradeParam("_1000290",e -> e.setStep(135).setSlTimes(new BigDecimal("0.005")).setMaShort(7).setMaLong(21))),
    _1000300("_1000300", newTradeParam("_1000300",e -> e.setStep(135).setSlTimes(new BigDecimal("0.0075")).setMaShort(7).setMaLong(21))),
    _1000310("_1000310", newTradeParam("_1000310",e -> e.setStep(135).setSlTimes(new BigDecimal("0.01")).setMaShort(7).setMaLong(21))),
    _1000320("_1000320", newTradeParam("_1000320",e -> e.setStep(135).setSlTimes(new BigDecimal("0.015")).setMaShort(7).setMaLong(21))),

    _1000330("_1000330", newTradeParam("_1000330",e -> e.setStep(1).setSlTimes(new BigDecimal("0.01")).setMaShort(7).setMaLong(21))),

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