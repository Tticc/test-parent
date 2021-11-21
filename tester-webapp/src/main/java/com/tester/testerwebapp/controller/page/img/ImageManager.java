package com.tester.testerwebapp.controller.page.img;

import com.tester.testercommon.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2021-11-15 10:57:58
 */
@Service
public class ImageManager {

    /**
     * 图片列表img标签拼接
     *
     * @Date 16:06 2021/11/15
     * @Author 温昌营
     **/
    public int listPic(StringBuilder sb, int pathIndex, int picIndex) throws IOException, BusinessException {
        List<File> files = ImgCommon.getFiles(pathIndex, picIndex);
        List<File> sorted = ImgCommon.sortFilesByIndex(files);
        for (File item : sorted) {
            String s = getPicHttpUrl(item.getPath());
            String picName = item.getName();
            if (ImgCommon.isPic.matcher(picName).matches()) {
                // 使用定位模式：   xxx/img/index/xxxx
                // 使用单图片模式： xxx/img/xxxx
                sb.append("<a href=\"/img/path/" + pathIndex + "/pic/" + picIndex + "/img/index/" + ImgCommon.getIndexByName(picName) + "\">")
                        .append("<img src=\"")
                        .append(s)
                        .append("\" width=\"100%\"/>");
            } else {
                sb.append("<video width = \"840\" height = \"540\" controls autoplay>");
                sb.append("<source src=\"")
                        .append(s)
                        .append("\" type=\"video/mp4\">");
                sb.append("</video>");
                sb.append("<br/><br/><h1>"+picName+"</h1>");
            }
        }
        appendPicTail(sb, pathIndex, picIndex);
        return sorted.size();
    }

    /**
     * 定位指定图片
     *
     * @Date 16:06 2021/11/15
     * @Author 温昌营
     **/
    public String getImgIndex(StringBuilder sb, int pathIndex, int picIndex, int imgIndex) {
        List<File> files = ImgCommon.getFiles(pathIndex, picIndex);
        List<File> sorted = ImgCommon.sortFilesByIndex(files);
        File file = null;
        for (int i = 0; i < sorted.size(); i++) {
            if(imgIndex == ImgCommon.getIndexByName(sorted.get(i).getName())){
                file = sorted.get(i);
            }
        }
        String path = file.getPath();
        String s = getPicHttpUrl(path);
        String style = "    <style>\n" +
                "        .shadow{\n" +
                "            width:50%;\n" +
                "            position:absolute;\n" +
                "            top:0;\n" +
                "            z-index:998;\n" +
                "            display:block;\n" +
                "        }\n" +
                "        .shadow-left{\n" +
                "            left:0;\n" +
                "        }\n" +
                "        .shadow-right{\n" +
                "            right:0;\n" +
                "        }\n" +
                "        body {\n" +
                "            display: block;\n" +
                "            margin: 0px;\n" +
                "        }\n" +
                "    </style>";
        String href = "/img/path/" + pathIndex + "/pic/" + picIndex + "/img/index/" + ImgCommon.getIndexByName(file.getName());

        String div = "<div class=\"divp\" id=\"divp\">\n" +
                "    <div id=\"divImg\">\n" +
                "    <a href = '"+href+"'>\n" +
                "        <img width = \"100%\" src=\"" + s + "\">\n" +
                "    </a>\n" +
                "    </div>\n" +
                "</div>";
        String script = "<script type=\"text/javascript\">\n" +
                "    window.onscroll = throttle(Date.now(), function () {\n" +
                "        var scrollTop = $(this).scrollTop(); //滚动条距离顶部的高度\n" +
                "        var scrollHeight = $(document).height(); //当前页面的总高度\n" +
                "        var clientHeight = $(this).height(); //当前可视的页面高度\n" +
                "        var bodyHeight1 = document.body.scrollHeight; //可见区域高度\n" +
                "        var clientHeight1 = window.screen.height; //屏幕分辨率的高\n" +
                "        var scrollTop1 = document.body.scrollTop; //网页被卷去的高\n" +
                "        if (scrollTop1 < 100) {\n" +
                "            getData(true,bodyHeight1);\n" +
                "        }else if (bodyHeight1 - scrollTop1 <=  2000) { //距离顶部+当前高度 >=文档总高度 即代表滑动到底部\n" +
                "            getData(false,bodyHeight1);\n" +
                "        }\n" +
                "    }, 400);\n" +
                "    function getData(isUp,scrollHeight) {\n" +
                "        let divImg = document.getElementById(\"divImg\");\n" +
                "        let eleArr = divImg.children;\n" +
                "        let firstImg = eleArr[0];\n" +
                "        let first = firstImg.children[0];\n" +

                "        let last = eleArr[eleArr.length-1];\n" +
                "        last = last.children[0];\n" +
                "        let currUrl = isUp ? first.src : last.src;\n" +
                "        let innerIsUp = isUp;\n" +
                "        let innerScrollHeight = scrollHeight;\n" +
                "        $.ajax({\n" +
                "            type: \"POST\",\n" +
                "            dataType: \"json\",\n" +
                "            url: \"/img/scrollGet\" ,\n" +
                "            data: {'currUrl':currUrl, 'isUp':innerIsUp},\n" +
                "            success: function (data) {\n" +
                "                if(data.newOneSrc == ''){\n" +
                "                    return;\n" +
                "                }\n" +
                "                // 追加子节点\n" +

                "                let img = document.createElement(\"img\");\n" +
                "                img.setAttribute('src', data.newOneSrc);\n" +
                "                img.setAttribute('width', '100%');\n" +

                "                let url = document.createElement(\"a\");\n" +
                "                url.setAttribute('href', data.newOneHref);\n" +
                "                url.appendChild(img);\n" +

                "                if(innerIsUp){\n" +
                "                    divImg.insertBefore(url,firstImg);\n" +
                "                }else {\n" +
                "                    divImg.appendChild(url);\n" +
                "                }\n" +
                "                // 重设高度\n" +
                "                img.onload = function () {\n" +
                "                    let divp = document.getElementById(\"divp\");\n" +
                "                    divp.style.height = innerScrollHeight+img.clientHeight+'px';\n" +
                "                }\n" +
                "            },\n" +
                "            error : function(err) {\n" +
                "                alert(err.responseJSON === undefined ? \"unknown error\" : err.responseJSON.message);\n" +
                "            }\n" +
                "        });\n" +
                "    };\n" +
                "\n" +
                "\n" +

                "    function throttle(startTime, func,delay){     //延缓滚动加载次数  防止抖动\n" +
                "        var timer = null;\n" +
                "        return function(){\n" +
                "            var curTime = Date.now();\n" +
                "            var remaining = delay - (curTime - startTime);\n" +
                "            var context = this;\n" +
                "            var args = arguments;\n" +
                "            clearTimeout(timer);\n" +
                "            if(remaining <= 0){\n" +
                "                func.apply(context,args);\n" +
                "                startTime = Date.now();\n" +
                "            }else{\n" +
                "                timer = setTimeout(func,remaining);\n" +
                "            }\n" +
                "        }\n" +
                "    };"+
                "</script>";
        sb.append(style).append(div).append(script);
        return imgIndex + " of " + sorted.size();
    }

    /**
     * 获取指定图片
     *
     * @Date 16:06 2021/11/15
     * @Author 温昌营
     **/
    public String getImg(StringBuilder sb, int pathIndex, int picIndex, int imgIndex) {
        List<File> files = ImgCommon.getFiles(pathIndex, picIndex);
        List<File> sorted = ImgCommon.sortFilesByIndex(files);
        int preIndex = -1;
        int nextIndex = -1;
        File file = null;
        for (int i = 0; i < sorted.size(); i++) {
            if(imgIndex == ImgCommon.getIndexByName(sorted.get(i).getName())){
                file = sorted.get(i);
                preIndex = i - 1 >= 0 ? ImgCommon.getIndexByName(sorted.get(i-1).getName()) : -1;
                nextIndex = i + 1 > sorted.size() ? -1 : ImgCommon.getIndexByName(sorted.get(i + 1).getName());
            }
        }
        String path = file.getPath();
        String s = getPicHttpUrl(path);
        String style = "    <style>\n" +
                "        .shadow{\n" +
                "            width:50%;\n" +
                "            position:absolute;\n" +
                "            top:0;\n" +
                "            z-index:998;\n" +
                "            display:block;\n" +
                "        }\n" +
                "        .shadow-left{\n" +
                "            left:0;\n" +
                "        }\n" +
                "        .shadow-right{\n" +
                "            right:0;\n" +
                "        }\n" +
                "        body {\n" +
                "            display: block;\n" +
                "            margin: 0px;\n" +
                "        }\n" +
                "    </style>";
        String div = "<div class=\"divp\" id=\"divp\">\n" +
                "    <div id=\"divImg\">\n" +
                "        <img width = \"100%\" id=\"timg\" src=\"" + s + "\">\n" +
                "    </div>\n" +
                "    <div id=\"divl\" class=\"shadow shadow-left\"></div>\n" +
                "    <div id=\"divr\" class=\"shadow shadow-right\"></div>" +
                "</div>";

        String script = "<script type=\"text/javascript\">\n" +
                "    window.onload = function () {\n" +
                "        var divl = document.getElementById(\"divl\");\n" +
                "        var divr = document.getElementById(\"divr\");\n" +
                "\n" +
                "        divl.onclick = function () {\n" +
                "            location.href = ('/img/path/" + pathIndex + "/pic/" + picIndex + "/img/" + preIndex + "');\n" +
                "        };\n" +
                "        divr.onclick = function () {\n" +
                "            location.href = ('/img/path/" + pathIndex + "/pic/" + picIndex + "/img/" + nextIndex + "');\n" +
                "        };\n" +
                "    };\n" +
                "    document.getElementById(\"timg\").onload = function () {\n" +
                "        var h = document.getElementById('timg').clientHeight;\n" +
                "        var divL = document.getElementById(\"divl\");\n" +
                "        var divR = document.getElementById(\"divr\");\n" +
                "        divL.style.height = parseInt(h)+\"px\";\n" +
                "        divR.style.height = parseInt(h)+\"px\";\n" +
                "    }" +
                "</script>";
        sb.append(style).append(div).append(script);
        return imgIndex + " of " + sorted.size();
    }

    /**
     * 追加尾部链接
     *
     * @Date 11:46 2021/11/12
     * @Author 温昌营
     **/
    private void appendPicTail(StringBuilder sb, int pathIndex, int picIndex) {
        List<File> files = ImgCommon.getFiles(pathIndex);
        List<File> sorted = ImgCommon.sortFilesByIndex(files);
        List<Integer> collect = sorted.stream()
                .map(File::getName)
                .map(ImgCommon::getIndexByName)
                .collect(Collectors.toList());
        int pre = 0;
        int next = 0;
        for (int i = 0; i < collect.size(); i++) {
            if (collect.get(i) == picIndex) {
                pre = i - 1 < 0 ? -1 : i - 1;
                next = i + 1 > collect.size() - 1 ? -1 : i + 1;
            }
        }
        sb.append("<style>");
        sb.append(".tail {");
        sb.append("padding-left: 5%;");
        sb.append("padding-top: 1%;");
        sb.append("padding-bottom: 1%;");
        sb.append("}");
        sb.append("</style>");
        sb.append("<div class=\"tail\">");
        if (pre != -1) {
            sb.append("<h1><a href=\"/img/path/" + pathIndex + "/pic/" + collect.get(pre) + "\">previous</a> </h1>");
        }
        sb.append("<br/><br/>");
        if (next != -1) {
            sb.append("<h1><a href=\"/img/path/" + pathIndex + "/pic/" + collect.get(next) + "\">next</a> </h1>");
        }
        sb.append("<br/><br/>");
        sb.append("<h1><a href=\"" + ImgController.HOME_URL + "\">HOME</a> </h1>");
        sb.append("</div>");
    }

    /**
     * 将文件路径转为http的img标签路径
     *
     * @Date 16:07 2021/11/15
     * @Author 温昌营
     **/
    private String getPicHttpUrl(String filePath) {
        return ImgCommon.fileUrl2HttpUrl(filePath);
    }
}
