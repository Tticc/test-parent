package com.tester.testercv.draw;

import com.tester.testercv.draw.basic.NodeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 节点
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
public class Node {
    /**
     *
     * 节点id
     */
    private Long id;
    /**
     * 层级。start节点 = 0。<br/>
     *  其他：down = level + 1; right = level;
     **/
    private int level;
    /**
     * 列。从0开始。<br/>
     *  其他：down = col; right = col + 1;
     **/
    private int col;
    /**
     * 节点类型
     **/
    private int type;
    /**
     * 下方节点。非 end节点 有down
     **/
    private Node down;
    /**
     * 右边节点。仅 check节点 有right
     **/
    private Node right;
    /**
     * 上一个节点。非 start节点有previous
     **/
    private Node previous;
    /**
     * 节点文本
     **/
    private String text;
    /**
     * x轴位置
     **/
    private int xPixel;
    /**
     * y轴位置
     **/
    private int yPixel;

    @Override
    public String toString() {
        return "{level:" + level +
                ", col:" + col +
                ", type:" + NodeTypeEnum.getByValue(type).getText() +
                ", text:" + text +
                ", xPixel:" + xPixel +
                ", yPixel:" + yPixel +
                "}";
    }
}
