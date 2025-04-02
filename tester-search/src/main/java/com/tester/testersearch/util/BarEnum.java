package com.tester.testersearch.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BarEnum {
    _1s("1s", "1秒钟",1),
    _1m("1m", "1分钟",60),
    _3m("3m", "3分钟",60*3),
    _5m("5m", "5分钟",60*5),
    _15m("15m", "15分钟",60*15),
    _19m("19m", "19分钟",60*19),
    _30m("30m", "30分钟",60*30),
    _1H("1H", "1小时",60*60),
    _2H("2H", "2小时",60*60*2),
    _4H("4H", "4小时",60*60*4),
    _1d("1d", "1天",60*60*24),
    ;
    private final String code;
    private final String text;
    private final int interval;

    public static BarEnum getByCode(String code) {
        for (BarEnum value : values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        return _1s;
    }
}