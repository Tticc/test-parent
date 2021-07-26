package com.tester.testercv.routeFinder;

import com.tester.testercv.commonImg.CommonImgHelper;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 温昌营
 * @Date 2021-7-13 13:43:57
 */
public class RouteFinderTest {
    private static int maxLen = 9;
    private static int[][] matrix = new int[maxLen][maxLen];
    private static List<String> routes = new ArrayList<>();
    private static List<Point> targetChain = new ArrayList<>();
    AtomicInteger stepCount = new AtomicInteger(1);
    private static Point startPoint;
    private static Point endPoint;


    // VMOption = -Djava.library.path=E:\opencv\opencv\build\java\x64
    @Test
    public void test_printMatrix() {
        Mat mat = initMat();
        CommonImgHelper.showImg(mat);
    }

    // VMOption = -Djava.library.path=E:\opencv\opencv\build\java\x64
    @Test
    public void test_getRoute() {
        getRoute();
    }

    private void getRoute() {
        int count = 0;
        for (int i = 0; i < maxLen; i++) {
            if(startPoint.y != i){
                continue;
            }
            for (int j = 0; j < maxLen; j++) {
                if(startPoint.x != j){
                    continue;
                }
                Mat mat = initMat();
                String str = "[" + i + "," + j + "];";
                if (matrix[i][j] == 1) {
                    matrix[i][j] = 3;
                    findNext(mat, matrix, str, i, j, addPoint(null, i, j));
                    matrix[i][j] = 1;
                }
                if (routes.size() > 0) {
                    saveMatAndClean(mat, ++count);
                    // 取消注释后则打印所有的可行路径
                    return;
                }
            }
        }
    }

    private void findNext(Mat mat, int[][] matrix, String str, int preI, int preJ, List<Point> oriChain) {
//        saveMatStep(mat,oriChain);
        if (checkRouteDone(matrix)) {
            if(preI != endPoint.y || preJ != endPoint.x){
                return;
            }
            saveResult(mat, oriChain);
            routes.add(str);
            targetChain = oriChain;
            return;
        }
        for (int i = 0; i < maxLen; i++) {
            for (int j = 0; j < maxLen; j++) {
                if (matrix[i][j] == 1 && checkIfNeighbor(preI, preJ, i, j)) {
                    matrix[i][j] = 3;
                    findNext(mat, matrix, str + "[" + i + "," + j + "];", i, j, addPoint(oriChain, i, j));
                    matrix[i][j] = 1;
                }
            }
        }
    }

    private boolean checkRouteDone(int[][] matrix) {
        for (int i = 0; i < maxLen; i++) {
            for (int j = 0; j < maxLen; j++) {
                if (matrix[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfNeighbor(int preI, int preJ, int nextI, int nextJ) {
        if (preI == nextI && Math.abs(preJ - nextJ) == 1) {
            return true;
        }
        if (preJ == nextJ && Math.abs(preI - nextI) == 1) {
            return true;
        }
        return false;
    }


    private void saveResult(Mat mat, List<Point> oriChain) {
        String picName = "";
        int step = stepCount.getAndIncrement();
        if (step < 10) {
            picName = "0000" + step;
        } else if (step < 100) {
            picName = "000" + step;
        } else if (step < 1000) {
            picName = "00" + step;
        } else if (step < 10000) {
            picName = "0" + step;
        } else {
            picName = "" + step;
        }
        Mat clone = mat.clone();
        linkPoint(clone, oriChain);
        CommonImgHelper.writeImg(picName, clone, "resultFolder");
    }

    private void saveMatStep(Mat mat, List<Point> oriChain) {
        String picName = "";
        int step = stepCount.getAndIncrement();
        if (step < 10) {
            picName = "0000" + step;
        } else if (step < 100) {
            picName = "000" + step;
        } else if (step < 1000) {
            picName = "00" + step;
        } else if (step < 10000) {
            picName = "0" + step;
        } else {
            picName = "" + step;
        }
        Mat clone = mat.clone();
        linkPoint(clone, oriChain);
        CommonImgHelper.writeImg(picName, clone, "stepFolder");
    }


    private void saveMatAndClean(Mat mat, int count) {
        linkPoint(mat, targetChain);
        System.out.println(routes);
        System.out.println(targetChain);
        CommonImgHelper.writeImg("test" + count, mat);
        routes = new ArrayList<>();
        targetChain = new ArrayList<>();
    }

    private void linkPoint(Mat mat, List<Point> chain) {
        if (CollectionUtils.isEmpty(chain)) {
            return;
        }
        Point point = chain.stream().reduce((a, b) -> {
            CommonImgHelper.drawLine(mat, (int) a.x, (int) a.y, (int) b.x, (int) b.y);
            return b;
        }).get();
    }

    private List<Point> addPoint(List<Point> oriChain, int x, int y) {
        if (CollectionUtils.isEmpty(oriChain)) {
            oriChain = new ArrayList<>();
        }
        List<Point> newChain = new ArrayList<>();
        newChain.addAll(oriChain);
        Point point = CommonImgHelper.getPoint(y * 100 + 50, x * 100 + 50);
        newChain.add(point);
        return newChain;
    }


    private Mat initMat() {
        Mat mat = CommonImgHelper.getMat(maxLen * 100, maxLen * 100);
        initPic(mat);
        return mat;
    }

    private void initPic(Mat mat) {
        initDrawLine(mat);
        initDrawPoint(mat);
    }

    private void initDrawLine(Mat mat) {
        int[] leftTop = new int[]{0, 0};
        int[] rightTop = new int[]{0, maxLen * 100};
        int[] leftBottom = new int[]{maxLen * 100, 0};
        int[] rightBottom = new int[]{maxLen * 100, maxLen * 100};

        CommonImgHelper.drawLine(mat, leftTop[0], leftTop[1], rightTop[0], rightTop[1]);
        CommonImgHelper.drawLine(mat, leftTop[0], leftTop[1], leftBottom[0], leftBottom[1]);
        CommonImgHelper.drawLine(mat, leftBottom[0], leftBottom[1], rightBottom[0], rightBottom[1]);
        CommonImgHelper.drawLine(mat, rightTop[0], rightTop[1], rightBottom[0], rightBottom[1]);

        for (int i = 0; i < maxLen; i++) {
            CommonImgHelper.drawLine(mat, leftTop[0] + i * 100, leftTop[1], rightTop[0] + i * 100, rightTop[1]);
            CommonImgHelper.drawLine(mat, leftTop[0], leftTop[1] + i * 100, leftBottom[0], leftBottom[1] + i * 100);
        }
    }

    private void initDrawPoint(Mat mat) {
        for (int i = 0; i < maxLen; i++) {
            for (int j = 0; j < maxLen; j++) {
                if (matrix[j][i] == 1) {
                    Point point = CommonImgHelper.getPoint(i * 100 + 50, j * 100 + 50);
                    CommonImgHelper.drawCircle(mat, point, 2);
                }
            }
        }
    }


    static {
        for (int i = 0; i < maxLen; i++) {
            for (int j = 0; j < maxLen; j++) {
                matrix[i][j] = 0;
            }
        }
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        matrix[0][3] = 1;
        matrix[0][4] = 1;
        matrix[0][5] = 1;
        matrix[0][6] = 1;
        matrix[0][7] = 1;
        matrix[0][8] = 1;

        matrix[1][0] = 1;
        matrix[1][1] = 0;
        matrix[1][2] = 1;
        matrix[1][3] = 1;
        matrix[1][4] = 1;
        matrix[1][5] = 0;
        matrix[1][6] = 0;
        matrix[1][7] = 0;
        matrix[1][8] = 1;

        matrix[2][0] = 1;
        matrix[2][1] = 1;
        matrix[2][2] = 1;
        matrix[2][3] = 0;
        matrix[2][4] = 1;
        matrix[2][5] = 1;
        matrix[2][6] = 1;
        matrix[2][7] = 1;
        matrix[2][8] = 1;

        matrix[3][0] = 0;
        matrix[3][1] = 0;
        matrix[3][2] = 1;
        matrix[3][3] = 0;
        matrix[3][4] = 1;
        matrix[3][5] = 1;
        matrix[3][6] = 1;
        matrix[3][7] = 0;
        matrix[3][8] = 0;

        matrix[4][0] = 0;
        matrix[4][1] = 0;
        matrix[4][2] = 1;
        matrix[4][3] = 0;
        matrix[4][4] = 1;
        matrix[4][5] = 0;
        matrix[4][6] = 1;
        matrix[4][7] = 0;
        matrix[4][8] = 0;

        matrix[5][0] = 0;
        matrix[5][1] = 0;
        matrix[5][2] = 1;
        matrix[5][3] = 1;
        matrix[5][4] = 1;
        matrix[5][5] = 0;
        matrix[5][6] = 1;
        matrix[5][7] = 0;
        matrix[5][8] = 0;

        matrix[6][0] = 1;
        matrix[6][1] = 1;
        matrix[6][2] = 1;
        matrix[6][3] = 1;
        matrix[6][4] = 1;
        matrix[6][5] = 0;
        matrix[6][6] = 1;
        matrix[6][7] = 0;
        matrix[6][8] = 0;

        matrix[7][0] = 1;
        matrix[7][1] = 1;
        matrix[7][2] = 1;
        matrix[7][3] = 1;
        matrix[7][4] = 1;
        matrix[7][5] = 1;
        matrix[7][6] = 1;
        matrix[7][7] = 0;
        matrix[7][8] = 0;

        matrix[8][0] = 0;
        matrix[8][1] = 0;
        matrix[8][2] = 0;
        matrix[8][3] = 1;
        matrix[8][4] = 0;
        matrix[8][5] = 0;
        matrix[8][6] = 0;
        matrix[8][7] = 0;
        matrix[8][8] = 0;

//        startPoint = CommonImgHelper.getPoint(y * 100 + 50, x * 100 + 50);
        startPoint = CommonImgHelper.getPoint(3, 0);
        endPoint = CommonImgHelper.getPoint(3, 8);

    }

}
