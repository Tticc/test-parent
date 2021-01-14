package com.tester.testercv.draw;

import com.tester.testercv.Img_GenerateImageTest;
import com.tester.testercv.draw.basic.NodeTypeEnum;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date
 */
public class DrawFlowTest {

    public static final String BASE_PATH = Img_GenerateImageTest.BASE_PATH;

    private static final int halfNodeHeight = 50;
    private static final int halfNodeWidth = 100;
    public static final int POINT_TYPE_START = NodeTypeEnum.START.getValue();
    public static final int POINT_TYPE_CHECK = NodeTypeEnum.EXCLUSIVE_CHECK.getValue();
    public static final int POINT_TYPE_TASK = NodeTypeEnum.USER_TASK.getValue();
    public static final int POINT_TYPE_END = NodeTypeEnum.END.getValue();


    public static final Scalar DEFAULT_LINE_POINT = new Scalar(255);
    public static final Scalar DEFAULT_TEXT_POINT = new Scalar(255);
    private static Mat mat;



    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        mat = new Mat(618, 2000, CvType.CV_8UC1, new Scalar(0));
    }

    @Test
    public void test_drawFromNode(){
        ImgNodeBuilder imgNodeBuilder = new ImgNodeBuilder();
        imgNodeBuilder.buildNodeTree();
        Map<Integer, List<NodeInImg>> levelNodeMap = imgNodeBuilder.getLevelNodeMap();
        NodeInImg startOfImgNodeInImg = imgNodeBuilder.getStartOfImgNodeInImg();

        int maxNodeLevel = levelNodeMap.size();
        int rows = maxNodeLevel*halfNodeHeight*3;
        int maxNodeNum = levelNodeMap.values().stream().flatMap(Collection::stream).max(Comparator.comparing(NodeInImg::getCol)).get().getCol();
        int cols = (maxNodeNum+1)*halfNodeWidth*3;

        startOfImgNodeInImg.setXPixel(halfNodeWidth)
                .setYPixel(halfNodeHeight);
        for (int i = 1; i < maxNodeLevel; i++) {
            List<NodeInImg> nodeInImgs = levelNodeMap.get(i);
            for (NodeInImg nodeInImg : nodeInImgs) {
                nodeInImg.setXPixel((nodeInImg.getCol()*3+1)*halfNodeWidth);
                nodeInImg.setYPixel((nodeInImg.getLevel()*3+1)*halfNodeHeight);
            }
        }
        System.out.println("\n\n\nlevelNodeMap:");
        System.out.println(levelNodeMap);

        Mat flowMat = new Mat(rows, cols, CvType.CV_8UC1, new Scalar(0));
        drawFromNodeTree(flowMat, startOfImgNodeInImg,null);
        showImg(flowMat,"flow at the very first");
//        writeImg("img flow",flowMat);
    }

    private void drawFromNodeTree(Mat mat, NodeInImg nodeInImg, NodeInImg previous){
        if(null == nodeInImg){
            return;
        }
        drawNode(mat, nodeInImg);
        drawLine(mat,previous, nodeInImg);
        drawFromNodeTree(mat, nodeInImg.getDown(), nodeInImg);
        drawFromNodeTree(mat, nodeInImg.getRight(), nodeInImg);
    }
    private void drawNode(Mat mat, NodeInImg nodeInImg){
        NodeTypeEnum byValue = NodeTypeEnum.getByValue(nodeInImg.getType());
        Point point = new Point(nodeInImg.getXPixel(), nodeInImg.getYPixel());
        switch (byValue){
            case USER_TASK:
                imgproc_rectangle(mat,point);
                break;
            case START:
                imgproc_ellipse(mat, point);
                break;
            case END:
            case EXCEPTION_END:
                imgproc_ellipse(mat, point);
                break;
            case EXCLUSIVE_CHECK:
                imgproc_diamond(mat,point);
                break;
            case ALL:
            default:
                break;
        }
        String text = nodeInImg.getText();
        Point textPoint = new Point(point.x - halfNodeWidth + 5, point.y);
        imgproc_text(mat,textPoint,text);
    }
    private void drawLine(Mat mat, NodeInImg start, NodeInImg end){
        if(null == start || end == null){
            return;
        }
        Point startPoint = new Point(start.getXPixel(),start.getYPixel());
        Point endPoint = new Point(end.getXPixel(),end.getYPixel());
        lineNode(mat,startPoint,endPoint);
    }



    /**
     * @param x
     * @return
     */
    private int getRightPointX(int x){
        return x+halfNodeWidth *3;
    }
    /**
     * @param y
     * @return
     */
    private int getDownPointY(int y){
        return y + halfNodeHeight *3;
    }

    private void negativeFeedback(List<NodeInImg> nodeInImgs){
        List<Integer> collect1 = nodeInImgs.stream().map(e -> e.getXPixel()).collect(Collectors.toList());
        System.out.println("xs is:"+collect1);
        int size;
        if(CollectionUtils.isEmpty(nodeInImgs)||(size = nodeInImgs.size()) <= 1){
            return;
        }

        // 从右往左执行
        NodeInImg preLevelMovePoint = null;
        for (int i = size-1; i > 0; i--) {
            for (int j = i-1; j >= 0; j--) {
                NodeInImg iNodeInImg = nodeInImgs.get(i);
                NodeInImg jNodeInImg = nodeInImgs.get(j);
                if(Math.abs(iNodeInImg.getXPixel() - jNodeInImg.getXPixel()) <= halfNodeWidth*2){
                    preLevelMovePoint = reSetPreviousPoint(nodeInImgs, iNodeInImg, jNodeInImg);
                }
            }
        }
        if(preLevelMovePoint != null){
            negativeFeedback(nodeInImgs);
        }
    }
    private NodeInImg reSetPreviousPoint(List<NodeInImg> nodeInImgs, NodeInImg iNodeInImg, NodeInImg jNodeInImg){
        NodeInImg res = null;
        NodeInImg iPre = iNodeInImg.getPrevious();
        NodeInImg jPre = jNodeInImg.getPrevious();
        if(iPre.getId() == jPre.getId()){
            // 这种情况不可能出现。
            System.err.println("异常！！！，距离过近的两个节点的直接父节点相同");
            return res;
        }
        if(iPre.getLevel() == jPre.getLevel()){
            // 父级相同，靠右的父级往右移动
            res = iPre.getXPixel() < jPre.getXPixel() ? jPre : iPre;
        } else {
            // 由level层级低的一方移动。移动x轴即可
            res = iPre.getLevel() > jPre.getLevel() ? jPre : iPre;
        }
        rebuildPointFromMovedNode(res);
        return res;
    }
    private void rebuildPointFromMovedNode(NodeInImg movedNodeInImg){
        if(null == movedNodeInImg){
            return;
        }
        movedNodeInImg.setXPixel(getRightPointX(movedNodeInImg.getXPixel()));
        if(movedNodeInImg.getDown() != null){
            movedNodeInImg.getDown().setXPixel(movedNodeInImg.getXPixel());
        }
        rebuildPointFromMovedNode(movedNodeInImg.getRight());
    }

    @Test
    public void test_draw(){
//        demoPrint();
//        readImg();
//        writeBlackImg();
//        topHat();
//        blackHat();
        Mat m = mat;
        Point startPoint0 = new Point((double)m.width()/2, halfNodeHeight);
        Point check10 = nextPoint(startPoint0,POINT_TYPE_START);
        addStartStep(m, startPoint0);
        addCheckStep(m,check10,"if approver = 13");
        lineNode(m,startPoint0,check10);


        Point check20 = nextPoint(check10,POINT_TYPE_CHECK,true);
        Point check21 = nextPoint(check10,POINT_TYPE_CHECK,false);
        addCheckStep(m,check20,"if type = 3");
        addCheckStep(m,check21,"if amount > 30000");
        lineNode(m,check10,check20);
        lineNode(m,check10,check21);


        showImg(m);
    }

    void addStartStep(Mat mat,Point point){
        imgproc_ellipse(mat,point);
        imgproc_text(mat,point,"start");
    }
    void addCheckStep(Mat mat,Point point,String text){
        imgproc_diamond(mat,point);
        imgproc_text(mat,point,text);
    }

    /**
     *
     * @param mat
     * @param start
     * @param end
     */
    void lineNode(Mat mat,Point start,Point end){
        Point lineStart;
        Point lineEnd;

        double xDis = Math.abs(start.x - end.x);
        double yDis = Math.abs(start.y - end.y);
        if(xDis <= yDis){
            lineStart = new Point(start.x, start.y + halfNodeHeight);
            lineEnd = new Point(end.x, end.y - halfNodeHeight);
        }else {
            lineStart = new Point(start.x+halfNodeWidth,start.y);
            lineEnd = new Point(end.x-halfNodeWidth,end.y);
        }
        imgproc_line(mat,lineStart,lineEnd);
    }

    /**
     * 根据上一个节点获取下一个节点
     * @param start 上一个节点
     * @param startPointType 上一个节点类型
     * @param left 下一个节点位置。左=true，右=false。当且仅当上一个节点为 POINT_TYPE_CHECK 时需要填充
     * @return org.opencv.core.Point
     * @Date 15:03 2020/11/27
     * @Author 温昌营
     **/
    private Point nextPoint(Point start,int startPointType, boolean ...left){
        double x;
        double y;
        if(startPointType != POINT_TYPE_CHECK){
            x = start.x;
            y = start.y + halfNodeHeight *3;
        }else {
            if(left[0]) {
                x = start.x;
                y = start.y + halfNodeHeight *3;
            }else {
                y = start.y;
                x = start.x + halfNodeWidth * 3;
            }
        }
        return new Point(x,y);
    }













    // ******************************************* basic **************************************************/
    private void imgproc_text(Mat mat, Point point, String text){
        Point tempPoint = new Point(point.x,point.y);
        Imgproc.putText(mat,text,tempPoint,Imgproc.FONT_HERSHEY_SIMPLEX,0.5, DEFAULT_TEXT_POINT);
    }

    private void imgproc_ellipse(Mat mat, Point point){
        Imgproc.ellipse(mat,point,new Size(halfNodeWidth, halfNodeHeight),360,0,360, DEFAULT_LINE_POINT);
    }

    private void imgproc_diamond(Mat mat, Point point){
        Point point1 = new Point(point.x,point.y- halfNodeHeight);
        Point point2 = new Point(point.x+ halfNodeWidth,point.y);
        Point point3 = new Point(point.x,point.y+ halfNodeHeight);
        Point point4 = new Point(point.x- halfNodeWidth,point.y);
        Imgproc.line(mat,point1,point2, DEFAULT_LINE_POINT);
        Imgproc.line(mat,point2,point3, DEFAULT_LINE_POINT);
        Imgproc.line(mat,point3,point4, DEFAULT_LINE_POINT);
        Imgproc.line(mat,point4,point1, DEFAULT_LINE_POINT);
    }

    private void imgproc_line(Mat mat, Point start,Point end){
        Imgproc.line(mat,start,end,DEFAULT_LINE_POINT);
    }

    private void imgproc_rectangle(Mat mat, Point point){
        Point start = new Point(point.x - halfNodeWidth,point.y - halfNodeHeight);
        Point end = new Point(point.x + halfNodeWidth,point.y + halfNodeHeight);
        Imgproc.rectangle(mat,start,end,DEFAULT_LINE_POINT);
    }





    private void showImg(Mat mat,String ...name){
        String picName = "picX";
        int defaultShowTime = 10;// second
        if (name.length>0) {
            picName = name[0];
        }
        HighGui.imshow(picName,mat);
        HighGui.waitKey();
//        try {
//            Thread.sleep(defaultShowTime*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
    private void writeImg(String name,Mat mat){
        Imgcodecs.imwrite(BASE_PATH+"writeImg_"+name+".png",mat);
    }
}
