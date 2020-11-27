package com.tester.testercv.draw;

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
     * 层级。start节点 = 0。<br/>
     *  其他：down = level + 1; right = level;
     **/
    private int level;
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
}
