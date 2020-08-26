package com.tester.testercommon.enums;

/**
 * @Author 温昌营
 * @Date
 */
public enum YesNoEnum {
    YES(1, "是"),
    NO(0, "否");

    private final int value;
    private final String text;

    private YesNoEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }
}