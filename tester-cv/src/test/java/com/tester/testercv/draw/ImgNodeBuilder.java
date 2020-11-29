package com.tester.testercv.draw;

import com.tester.testercv.draw.basic.CheckTypeEnum;
import com.tester.testercv.draw.basic.FormFieldConditionDO;
import com.tester.testercv.draw.basic.NodeModelDO;
import com.tester.testercv.draw.basic.NodeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Data
public class ImgNodeBuilder {


    private static LinkedNode start;// 来自已有代码。


    private Map<Integer,List<Node>> levelNodeMap = new HashMap<>();
    private Node startOfImgNode = null;

    public static void main(String[] args){
        ImgNodeBuilder imgNodeBuilder = new ImgNodeBuilder();
        imgNodeBuilder.buildNodeTree();
    }

    public void buildNodeTree(){
        buildNodeTreeSub(startOfImgNode,start);
        System.out.println(levelNodeMap);
    }

    // 完成当前节点，并设置下一节点
    private void buildNodeTreeSub(Node preImgNode, LinkedNode preDataNode) {
        List<LinkedNode> allNexts = preDataNode.getAllNexts();
        if (CollectionUtils.isEmpty(allNexts)) {
            // 下一层节点为空，返回
            return;
        }
        if (null == preImgNode) {
            // 起始点
            preImgNode = startOfImgNode = new Node()
                    .setId(preDataNode.getNode().getId())
                    .setLevel(0)
                    .setPrevious(null)
                    .setType(NodeTypeEnum.START.getValue())
                    .setText("start")
                    .setDown(null)
                    .setRight(null);
            setNodeField(preDataNode,preImgNode);
            addToMap(0, preImgNode);
        }
        Node tempDown = null;
        Node tempRight = null;
        int level;
        LinkedNode tempLinkedNode;
        if (allNexts.size() > 1) {
            //
            FormFieldConditionDO condition = getConditionByNodeId(preDataNode.getNode().getId());
            for (LinkedNode nextDataNode : allNexts) {
                tempLinkedNode = nextDataNode;
                Long id = tempLinkedNode.getNode().getId();
                if (id == condition.getTrueNextId()) {
                    // true 节点往下走
                    level = preImgNode.getLevel() + 1;
                    tempDown = new Node()
                            .setId(id)
                            .setLevel(level)
                            .setPrevious(preImgNode);
                    setNodeField(tempLinkedNode, tempDown);
                    addToMap(level, tempDown);
                    buildNodeTreeSub(tempDown, tempLinkedNode);
                } else {
                    // false 节点往右走
                    level = preImgNode.getLevel();
                    tempRight = new Node()
                            .setId(id)
                            .setLevel(level)
                            .setPrevious(preImgNode);
                    setNodeField(tempLinkedNode, tempRight);
                    addToMap(level, tempRight);
                    buildNodeTreeSub(tempRight, tempLinkedNode);
                }
            }

        } else {
            tempLinkedNode = allNexts.get(0);
            level = preImgNode.getLevel() + 1;
            tempDown = new Node()
                    .setId(tempLinkedNode.getNode().getId())
                    .setLevel(level)
                    .setPrevious(preImgNode);
            setNodeField(tempLinkedNode, tempDown);
            addToMap(level, tempDown);
            buildNodeTreeSub(tempDown, tempLinkedNode);
        }
        preImgNode.setRight(tempRight);
        preImgNode.setDown(tempDown);
    }
    private void addToMap(int level, Node node){
        List<Node> nodes = levelNodeMap.get(level);
        if(nodes == null){
            nodes = new ArrayList<>();
            levelNodeMap.put(level,nodes);
        }
        nodes.add(node);
    }

    private void setNodeField(LinkedNode linkedNode, Node node){
        Integer type = linkedNode.getType();
        String text = linkedNode.getNodeKey();
        NodeTypeEnum byValue = NodeTypeEnum.getByValue(type);
        switch (byValue){
            case START:
                break;
            case EXCLUSIVE_CHECK:
                // get condition here
                FormFieldConditionDO condition = getConditionByNodeId(linkedNode.getNode().getId());;
                Integer checkType = condition.getCheckType();
                String checkValue = condition.getCheckValue();
                text+= CheckTypeEnum.getByValue(checkType).getSymbol()+checkValue;
                break;
            case USER_TASK:
                break;
            case EXCEPTION_END:
            case END:
                break;
            case ALL:
            default:
                break;
        }
        node.setDown(null)
                .setRight(null)
                .setType(type)
                .setText(text);
    }

    private FormFieldConditionDO getConditionByNodeId(Long id){
        if(id == 2L){
            return new FormFieldConditionDO()
                    .setCheckType(CheckTypeEnum.LT.getValue())
                    .setCheckValue("100000")
                    .setTrueNextId(3L)
                    .setFalseNextId(4L);
        }
        System.err.println("something went wrong, should not going into here.");
        return new FormFieldConditionDO();
    }


    @Data
    @Accessors(chain = true)
    static class LinkedNode{
        LinkedNode(){}
        LinkedNode(NodeModelDO node){
            this.node = node;
            this.nodeKey = node.getNodeKey();
            this.type = node.getNodeType();
            this.allNexts = new ArrayList<>();
        }
        NodeModelDO node;
        String nodeKey;
        Integer type;
        /** 实际上的下一个节点*/
        LinkedNode next;
        /** 所有可能的下一节点*/
        List<LinkedNode> allNexts;
    }


    static {
        LinkedNode task1 = new LinkedNode()
                .setNode(new NodeModelDO().setId(3L))
                .setNodeKey("cv do that")
                .setAllNexts(null)
                .setNext(null)
                .setType(NodeTypeEnum.USER_TASK.getValue());

        LinkedNode task2 = new LinkedNode()
                .setNode(new NodeModelDO().setId(4L))
                .setNodeKey("wenc do that")
                .setAllNexts(null)
                .setNext(null)
                .setType(NodeTypeEnum.USER_TASK.getValue());


        List<LinkedNode> checkAllNext = Arrays.asList(task1,task2);
        LinkedNode check1 = new LinkedNode()
                .setNode(new NodeModelDO().setId(2L))
                .setNodeKey("check if big")
                .setAllNexts(checkAllNext)
                .setNext(null)
                .setType(NodeTypeEnum.EXCLUSIVE_CHECK.getValue());


        List<LinkedNode> startNext = Arrays.asList(check1);
        start = new LinkedNode()
                .setNode(new NodeModelDO().setId(1L))
                .setNodeKey("start of all")
                .setAllNexts(startNext)
                .setNext(null)
                .setType(NodeTypeEnum.START.getValue());
    }
}
