package com.tester.testercommon.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 生成二维码示例
 * 二维码+描述文字
 */
@Slf4j
public class QRCodeUtil {
    //设置默认参数，可以根据需要进行修改

    private static final int QRCOLOR = 0xFF000000; // 默认是黑色

    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    private static final int WIDTH = 230; // 二维码宽

    private static final int HEIGHT = 230; // 二维码高


    public static void main(String[] args) {
        QRCodeUser qrCodeUser = new QRCodeUser();
        qrCodeUser.setUserName("张三");
        qrCodeUser.setUrl("www.baidu.com");
        qrCodeUser.setDataType("肺");
        qrCodeUser.setOrderNo("SHKH60213GKJ");
        qrCodeUser.setDept("研发部小激动较发达");
        qrCodeUser.setOrderDate("2022-12-12");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("C:\\Users\\wenc\\Desktop\\img\\" + qrCodeUser.getOrderNo() + ".png");  //保存路径输出流，将图片输出到指定路径
            Font fontChinese = new Font("新宋体", Font.BOLD, 25);
            BufferedImage image = QRCodeUtil.createQr(qrCodeUser, fontChinese);

            boolean crateQRCode = ImageIO.write(image, "png", fileOutputStream);

        } catch (WriterException | IOException e) {
            log.error("二维码写入IO流异常", e);
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException ioe) {
                log.error("二维码关流异常", ioe);
            }
        }
    }

    /**
     * 用于设置QR二维码参数
     * com.google.zxing.EncodeHintType：编码提示类型,枚举类型
     * EncodeHintType.CHARACTER_SET：设置字符编码类型
     * EncodeHintType.ERROR_CORRECTION：设置误差校正
     * ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
     * 不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
     * EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
     */
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
            put(EncodeHintType.MARGIN, 0);
        }

    };


    /**
     * 生成二维码和附带字体参数
     */
    private static BufferedImage createQr(QRCodeUser qrCodeUser, Font font) throws WriterException {
        //设置二维码旁边的文字信息
        String userName = "患者姓名: " + qrCodeUser.getUserName();
        String dataType = "数据类型: " + qrCodeUser.getDataType();
        String orderNo = "数据号: " + qrCodeUser.getOrderNo();
        String dept = "单位: " + qrCodeUser.getDept();
        String orderDate = "订单日期: " + qrCodeUser.getOrderDate();
        String qrurl = qrCodeUser.getUrl();    //这里以百度为例

        /**
         * MultiFormatWriter:多格式写入，这是一个工厂类，里面重载了两个 encode 方法，用于写入条形码或二维码
         *      encode(String contents,BarcodeFormat format,int width, int height,Map<EncodeHintType,?> hints)
         *      contents:条形码/二维码内容
         *      format：编码类型，如 条形码，二维码 等
         *      width：码的宽度
         *      height：码的高度
         *      hints：码内容的编码类型
         * BarcodeFormat：枚举该程序包已知的条形码格式，即创建何种码，如 1 维的条形码，2 维的二维码 等
         * BitMatrix：位(比特)矩阵或叫2D矩阵，也就是需要的二维码
         */
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        /**参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
         * BitMatrix 的 get(int x, int y) 获取比特矩阵内容，指定位置有值，则返回true，将其设置为前景色，否则设置为背景色
         * BufferedImage 的 setRGB(int x, int y, int rgb) 方法设置图像像素
         *      x：像素位置的横坐标，即列
         *      y：像素位置的纵坐标，即行
         *      rgb：像素的值，采用 16 进制,如 0xFFFFFF 白色
         */
        BitMatrix bm = multiFormatWriter.encode(qrurl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        //创建一个图片缓冲区存放二维码图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
            }
        }

        // ------------------------------------------自定义文本描述-------------------------------------------------
        if (!StringUtils.isEmpty(qrCodeUser.getUserName())) {
            //在内存创建图片缓冲区  这里设置画板的宽高和类型
            BufferedImage outImage = new BufferedImage(600, 320, BufferedImage.TYPE_4BYTE_ABGR);

            //创建画布
            Graphics2D outg = outImage.createGraphics();

            // 画文字到新的面板
            outg.setColor(Color.BLACK);
            // 字体、字型、字号
            outg.setFont(font);
            //获取字体高度
            int frontHeight = (int) (outg.getFontMetrics().getHeight() * 2);

            //drawString(文字信息、x轴、y轴)方法根据参数设置文字的坐标轴 ，根据需要来进行调整
            int toTop = 50;
            outg.drawString(userName, 10, toTop);
            outg.drawString(dataType, 10, toTop + frontHeight);
            outg.drawString(orderNo, 10, toTop + frontHeight * 2);
            outg.drawString(dept, 10, toTop + frontHeight * 3);
            outg.drawString(orderDate, 10, toTop + frontHeight * 4);

            //获取字体宽度
            Integer maxWidth = getMax(outg.getFontMetrics().stringWidth(userName),
                    outg.getFontMetrics().stringWidth(dataType),
                    outg.getFontMetrics().stringWidth(orderNo),
                    outg.getFontMetrics().stringWidth(dept),
                    outg.getFontMetrics().stringWidth(orderDate)
            );
            // 在画布上画上二维码  X轴Y轴，宽度高度
            outg.drawImage(image, maxWidth + 20, toTop, image.getWidth(), image.getHeight(), null);


            outg.dispose();
            outImage.flush();
            image = outImage;
        }
        image.flush();
        return image;
    }

    @Data
    public static class QRCodeUser {
        String url;
        String userName;
        String dataType;
        String orderNo;
        String dept;
        String orderDate;
    }

    private static Integer getMax(Integer... val) {
        int max = -1;
        for (Integer integer : val) {
            max = integer > max ? integer : max;
        }
        return max;
    }

}