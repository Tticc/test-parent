package com.tester.testercv.draw.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
@Getter
@AllArgsConstructor
public enum CheckTypeEnum {
    GT(1,"大于",">"),
    LT(2,"小于","<"),
    EQ(3,"等于","="),
            ;
    private final int value;
    private final String text;
    private final String symbol;
    public static CheckTypeEnum getByValue(Integer value) {
        for (CheckTypeEnum item : values()) {
            if (Objects.equals(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }
    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
