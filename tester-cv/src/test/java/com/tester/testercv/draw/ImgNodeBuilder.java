package com.tester.testercv.draw;

import com.tester.testercv.draw.basic.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Data
public class ImgNodeBuilder {


    private static LinkedNode start;// 来自已有代码。
    private static List<FormFieldConditionDO> conditions;// 来自已有代码。


    private Map<Integer,List<NodeInImg>> levelNodeMap = new HashMap<>();
    private NodeInImg startOfImgNodeInImg = null;

    public static void main(String[] args){
        ImgNodeBuilder imgNodeBuilder = new ImgNodeBuilder();
        imgNodeBuilder.buildNodeTree();
    }

    public void buildNodeTree(){
        buildNodeTreeSub(startOfImgNodeInImg,start);
    }

    // 完成当前节点，并设置下一节点
    private void buildNodeTreeSub(NodeInImg preImgNodeInImg, LinkedNode preDataNode) {
        List<LinkedNode> allNexts = preDataNode.getAllNexts();
        if (CollectionUtils.isEmpty(allNexts)) {
            // 下一层节点为空，返回
            return;
        }
        if (null == preImgNodeInImg) {
            // 起始点
            preImgNodeInImg = startOfImgNodeInImg = new NodeInImg()
                    .setId(preDataNode.getNode().getId())
                    .setLevel(0)
                    .setCol(0)
                    .setPrevious(null)
                    .setType(NodeTypeEnum.START.getValue())
                    .setText("start")
                    .setDown(null)
                    .setRight(null);
            setNodeField(preDataNode, preImgNodeInImg);
            addToMapAndResetCol(0, preImgNodeInImg,0);
        }
        NodeInImg tempDown = null;
        NodeInImg tempRight = null;
        int level;
        int col;
        LinkedNode tempLinkedNode;
        if (allNexts.size() > 1) {
            //
            FormFieldConditionDO condition = getConditionByNodeId(preDataNode.getNode().getId());
            LinkedNode linkedNodeDown = Objects.equals(allNexts.get(0).getNode().getId(),condition.getTrueNextId()) ? allNexts.get(0) : allNexts.get(1);
            LinkedNode linkedNodeRight = !Objects.equals(allNexts.get(0).getNode().getId(),condition.getTrueNextId()) ? allNexts.get(0) : allNexts.get(1);;

                if (linkedNodeDown != null) {
                    tempLinkedNode = linkedNodeDown;
                    // true 节点往下走
                    level = preImgNodeInImg.getLevel() + 1;
                    col = preImgNodeInImg.getCol();
                    tempDown = new NodeInImg()
                            .setId(tempLinkedNode.getNode().getId())
                            .setLevel(level)
                            .setCol(col)
                            .setPrevious(preImgNodeInImg);
                    setNodeField(tempLinkedNode, tempDown);
                    addToMapAndResetCol(level, tempDown,col);
                    buildNodeTreeSub(tempDown, tempLinkedNode);
                }
                 if(linkedNodeRight != null){
                    tempLinkedNode = linkedNodeRight;
                    // false 节点往右走
                    level = preImgNodeInImg.getLevel();
                    col = preImgNodeInImg.getCol()+1;
                    tempRight = new NodeInImg()
                            .setId(tempLinkedNode.getNode().getId())
                            .setLevel(level)
                            .setCol(col)
                            .setPrevious(preImgNodeInImg);
                    setNodeField(tempLinkedNode, tempRight);
                    addToMapAndResetCol(level, tempRight,col);
                    buildNodeTreeSub(tempRight, tempLinkedNode);
                }
        } else {
            tempLinkedNode = allNexts.get(0);
            level = preImgNodeInImg.getLevel() + 1;
            col = preImgNodeInImg.getCol();
            tempDown = new NodeInImg()
                    .setId(tempLinkedNode.getNode().getId())
                    .setLevel(level)
                    .setCol(col)
                    .setPrevious(preImgNodeInImg);
            setNodeField(tempLinkedNode, tempDown);
            addToMapAndResetCol(level, tempDown,col);
            buildNodeTreeSub(tempDown, tempLinkedNode);
        }
        preImgNodeInImg.setRight(tempRight);
        preImgNodeInImg.setDown(tempDown);
    }
    private void addToMapAndResetCol(int level, NodeInImg nodeInImg, int col){
        List<NodeInImg> nodeInImgs = levelNodeMap.get(level);
        if(CollectionUtils.isEmpty(nodeInImgs)){
            nodeInImgs = new ArrayList<>();
            levelNodeMap.put(level, nodeInImgs);
            nodeInImgs.add(nodeInImg);
            return;
        }
        NodeInImg nodeInImg1 = nodeInImgs.stream().max(Comparator.comparing(NodeInImg::getCol)).get();
        if(nodeInImg1.getCol() >= col){
            nodeInImg.setCol(nodeInImg1.getCol() + 1);
            NodeInImg tempNodeInImg = nodeInImg.getPrevious();
            while(tempNodeInImg != null && tempNodeInImg.getCol() == col){
                tempNodeInImg.setCol(nodeInImg.getCol());
                tempNodeInImg = tempNodeInImg.getPrevious();
            }
        }
        nodeInImgs.add(nodeInImg);
    }

    private void setNodeField(LinkedNode linkedNode, NodeInImg nodeInImg){
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
        nodeInImg.setDown(null)
                .setRight(null)
                .setType(type)
                .setText(text);
    }

    private FormFieldConditionDO getConditionByNodeId(Long id){
        for (FormFieldConditionDO condition : conditions) {
            if(Objects.equals(condition.getNodeId(),id)){
                return condition;
            }
        }
        System.err.println("something went wrong, should not going into here.");
        return new FormFieldConditionDO();
    }


    @Data
    @Accessors(chain = true)
    public static class LinkedNode{
        LinkedNode(){}
        public LinkedNode(NodeModelDO node){
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
//        start = getFromNew();
        start = getFromDB();
        conditions = BasicTool.listConditions();
    }






    private static LinkedNode getFromDB(){
        LinkedNode linkNode = BasicTool.getLinkNode();
        return linkNode;
    }

    @Deprecated
    private static LinkedNode getFromNew(){
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
        return new LinkedNode()
                .setNode(new NodeModelDO().setId(1L))
                .setNodeKey("start of all")
                .setAllNexts(startNext)
                .setNext(null)
                .setType(NodeTypeEnum.START.getValue());
    }
}
