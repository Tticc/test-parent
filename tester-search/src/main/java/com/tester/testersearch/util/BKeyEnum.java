package com.tester.testersearch.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BKeyEnum {
    BTCUSDT("BTCUSDT", "BTCUSDT"),
    ETHUSDT("ETHUSDT", "ETHUSDT"),
    ;
    private final String code;
    private final String text;

    public static BKeyEnum getByCode(String code) {
        for (BKeyEnum value : values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return BTCUSDT;
    }
}