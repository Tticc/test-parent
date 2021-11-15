package com.tester.testerwebapp.controller.page.img;

import com.tester.testercommon.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author 温昌营
 * @Date 2021-11-15 10:57:58
 */
@Service
public class RouteManager {


    /**
     * 构建home主体内容
     *
     * @Date 14:43 2021/11/12
     * @Author 温昌营
     **/
    public String buildHomeContent() {
        List<File> files = ImgCommon.getFiles();
        List<File> collect = ImgCommon.sortFilesByIndex(files);
        StringBuilder stringBuilder = new StringBuilder();
        appendRouteStyle(stringBuilder, (sb) -> {
            for (File file : collect) {
                sb.append("<a href=\"/img/path/")
                        .append(ImgCommon.getIndexByName(file.getName()))
                        .append("\">")
                        .append(file.getName())
                        .append("</a>")
                        .append("<br/><br/>");
            }
        });
        return stringBuilder.toString();
    }

    /**
     * 列出指定path的文件夹列表
     *
     * @Date 11:11 2021/11/15
     * @Author 温昌营
     **/
    public String listPath(HttpServletRequest request, HttpServletResponse response, int index) throws IOException, BusinessException {
        List<File> files = ImgCommon.getFiles(index);
        List<File> collect = ImgCommon.sortFilesByIndex(files);
        StringBuilder stringBuilder = new StringBuilder();
        appendRouteStyle(stringBuilder, sb -> {
            for (File item : collect) {
                int picIndex = ImgCommon.getIndexByName(item.getName());
                sb.append("<a href=\"/img/path/" + index + "/pic/" + picIndex + "\"> " + item.getName() + " </a>")
                        .append("<br/><br/>");
            }
        });
        return stringBuilder.toString();
    }


    private void appendRouteStyle(StringBuilder sb, Consumer<StringBuilder> consumer) {
        sb.append("<style>");
        sb.append(".routeContent {");
        sb.append("padding-left: 5%;");
        sb.append("padding-top: 1%;");
        sb.append("padding-bottom: 1%;");
        sb.append("font-size:80px");
        sb.append("}");
        sb.append("</style>");
        sb.append("<div class=\"routeContent\">");
        consumer.accept(sb);
        sb.append("</div>");
    }
}
