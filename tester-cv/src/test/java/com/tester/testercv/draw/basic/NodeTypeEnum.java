package com.tester.testercv.draw.basic;

import java.util.Objects;

/**
 *
 * @Date 16:16 2020/4/1
 * @Author 温昌营
 **/
public enum NodeTypeEnum {
    ALL(0,"all"),
    START(1,"开始节点"),
    END(2,"结束节点"),
    EXCEPTION_END(21,"条件异常结束节点"),
    USER_TASK(3,"任务节点"),
    EXCLUSIVE_CHECK(4,"排他判断节点"),

    ;

    private final int value;
    private final String text;
    NodeTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
    public static NodeTypeEnum getByValue(Integer value) {
        for (NodeTypeEnum item : values()) {
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
