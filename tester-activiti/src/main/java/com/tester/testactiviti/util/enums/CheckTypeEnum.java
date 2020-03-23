package com.tester.testactiviti.util.enums;

import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
public enum CheckTypeEnum {
    LG(1,"大于"),
    LE(2,"小于"),
    EQ(3,"等于"),
            ;
    private final int value;
    private final String text;
    CheckTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
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
