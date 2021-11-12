package com.tester.testerwebapp.controller.page.img;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 查看 templates/img/home.html
 *
 * @Author 温昌营
 * @Date 2021-11-11 18:27:57
 */
@Controller
@Slf4j
@RequestMapping("/img")
public class ImgController {

    Pattern isPic = Pattern.compile(".*?\\.(jpg|gif|jpeg)+$");

    private static final String IMG_ROOT_PATH = "static/img";


    private static final String CHECK_PWD = "edcxx";

    public static final String USER_TYPE = "user_type";

    public static final String INNER = "inner"; // 内部
    public static final String OUTER = "outer"; // 外部
    public static final String URL_TAG = "url"; // url tag
    public static final String HOME_URL = "/img/home"; // 主界面请求地址


    /**
     * home
     *
     * @Date 11:47 2021/11/12
     * @Author 温昌营
     **/
    @GetMapping("/home")
    public String getHome(HttpServletRequest req, HttpSession session) {
        if (INNER.equals(session.getAttribute(USER_TYPE))) {
            req.setAttribute("initContent", buildHomeContent());
        }
        return "img/home";
    }

    /**
     * 用户验证
     *
     * @Date 14:43 2021/11/12
     * @Author 温昌营
     **/
    @ResponseBody
    @RequestMapping(value = "/credential", method = RequestMethod.POST)
    public String credential(String pwd, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 200);
        if (!CHECK_PWD.equals(pwd)) {
            session.setAttribute(USER_TYPE, OUTER);
            System.out.println("incorrect first time");
            jsonObject.put("errmsg", "pwd err");
            jsonObject.put("status", 403);
            return jsonObject.toString();
        }
        if (session.getAttribute(USER_TYPE) != null && !INNER.equals(session.getAttribute(USER_TYPE))) {
            System.out.println("correct but not first time");
            jsonObject.put("errmsg", "pwd err");
            jsonObject.put("status", 403);
            return jsonObject.toString();
        }
        session.setAttribute(USER_TYPE, INNER);
        jsonObject.put("content", buildHomeContent());
        jsonObject.put(URL_TAG, session.getAttribute(URL_TAG));
        return jsonObject.toString();
    }


    /**
     * 列出指定path的所有pic文件夹
     *
     * @Date 9:42 2021/11/12
     * @Author 温昌营
     **/
    @RequestMapping(value = "/path/{index}", method = RequestMethod.GET)
    public void listPath(HttpServletRequest request, HttpServletResponse response, @PathVariable int index) throws IOException, BusinessException {
        String path = IMG_ROOT_PATH + "/path" + index;
        List<File> files = getFiles(path);
        StringBuilder sb = new StringBuilder();
        for (File item : files) {
            String picIndex = item.getName().replaceAll("[a-z]|[A-Z]", "");
            sb.append("<a href=\"/img/path/")
                    .append(index)
                    .append("/pic/")
                    .append(picIndex)
                    .append("\"> <h1>pic")
                    .append(picIndex)
                    .append("</h1> </a>")
                    .append("<br/>");
        }
        flush(response, "path" + index, (out) -> {
            out.println("<style>");
            out.println("body{text-align:center;padding-top: 1%;padding-bottom: 1%;} ");
            out.println(".tail {");
            out.println("padding-left: 5%;");
            out.println("padding-top: 1%;");
            out.println("padding-bottom: 1%;");
            out.println("}");
            out.println("</style>");
            out.println("<div class=\"tail\">");
            out.println(sb.toString());
            out.println("</div>");
        });
    }

    /**
     * 展示指定path，指定pic包含的所有图片
     *
     * @Date 9:42 2021/11/12
     * @Author 温昌营
     **/
    @RequestMapping(value = "/path/{pathIndex}/pic/{picIndex}", method = RequestMethod.GET)
    public void listPic(HttpServletResponse response, @PathVariable int pathIndex, @PathVariable int picIndex) throws IOException, BusinessException {
        String picPath = IMG_ROOT_PATH + "/path" + pathIndex + "/pic" + picIndex;
        List<File> files = getFiles(picPath);
        List<File> sorted = files.stream().sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (File item : sorted) {
            String picName = item.getName();
            if(isPic.matcher(picName).matches()){
                sb.append("<img src=\"/static/img/path")
                        .append(pathIndex)
                        .append("/pic")
                        .append(picIndex)
                        .append("/")
                        .append(picName)
                    .append("\" width=\"100%\"/>");
//                        .append("\"/>");
            }else {
                sb.append("<video width = \"840\" height = \"540\" controls autoplay>");
                sb.append("<source src=\"/static/img/path")
                        .append(pathIndex)
                        .append("/pic")
                        .append(picIndex)
                        .append("/")
                        .append(picName)
                        .append("\" type=\"video/mp4\">");
                sb.append("</video>");
            }
        }
        String title = "pic" + picIndex;
        title += " - " + sorted.size();
        flush(response, title, (out) -> {
            out.println("<style>");
            out.println(".tail {");
            out.println("padding-left: 5%;");
            out.println("padding-top: 1%;");
            out.println("padding-bottom: 1%;");
            out.println("}");
            out.println("</style>");
            out.println(sb.toString());
            putPicTail(out, pathIndex, picIndex);
        });
    }


    /**
     * 构建home主体内容
     *
     * @Date 14:43 2021/11/12
     * @Author 温昌营
     **/
    private String buildHomeContent() {
        List<File> files = getFiles(IMG_ROOT_PATH);
        List<File> collect = files.stream()
                .sorted((a, b) -> {
                    Integer ta = Integer.valueOf(a.getName().replaceAll("[a-z]|[A-Z]", ""));
                    Integer tb = Integer.valueOf(b.getName().replaceAll("[a-z]|[A-Z]", ""));
                    return ta.compareTo(tb);
                })
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        //<a href="/img/path/1"><h1>path1</h1></a>

        sb.append("<style>");
        sb.append(".tail {");
        sb.append("padding-left: 5%;");
        sb.append("padding-top: 1%;");
        sb.append("padding-bottom: 1%;");
        sb.append("}");
        sb.append("</style>");
        sb.append("<div class=\"tail\">");
        for (File file : collect) {
            sb.append("<h1><a href=\"/img/path/")
                    .append(file.getName().replaceAll("[a-z]|[A-Z]", ""))
                    .append("\">")
                    .append(file.getName())
                    .append("</a></h1>")
                    .append("<br/>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    /**
     * 追加尾部链接
     *
     * @Date 11:46 2021/11/12
     * @Author 温昌营
     **/
    private void putPicTail(PrintWriter out, int pathIndex, int picIndex) {
        String path = IMG_ROOT_PATH + "/path" + pathIndex;
        List<File> files = getFiles(path);
        List<Integer> collect = files.stream()
                .map(e -> e.getName())
                .map(e -> e.replaceAll("[a-z]|[A-Z]", ""))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
        int pre = 0;
        int next = 0;
        for (int i = 0; i < collect.size(); i++) {
            if (collect.get(i) == picIndex) {
                pre = i - 1 < 0 ? -1 : i - 1;
                next = i + 1 > collect.size() - 1 ? -1 : collect.size() - 1;
            }
        }
        out.println("<div class=\"tail\">");
        if (pre != -1) {
            out.println("<h1><a href=\"/img/path/" + pathIndex + "/pic/" + collect.get(pre) + "\">previous</a> </h1>");
        }
        out.println("<br/><br/>");
        if (next != -1) {
            out.println("<h1><a href=\"/img/path/" + pathIndex + "/pic/" + collect.get(next) + "\">next</a> </h1>");
        }
        out.println("</div>");
    }

    /**
     * 获取resources指定路径下某一层的文件
     *
     * @Date 13:49 2021/11/12
     * @Author 温昌营
     **/
    private List<File> getFiles(String path) {
        List<File> res = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            File file;
            File[] files;
            if (!(file = classPathResource.getFile()).isDirectory()
                    || (files = file.listFiles()) == null) {
                return res;
            }
            res = Arrays.asList(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 公共response构建方法
     *
     * @Date 11:46 2021/11/12
     * @Author 温昌营
     **/
    private void flush(HttpServletResponse response, String title, MyConsumer<PrintWriter> consumer) throws IOException, BusinessException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");

        out.println("<meta http-equiv=\"pragma\" content=\"no-cache\">");
        out.println("<meta http-equiv=\"cache-control\" content=\"no-cache\">");
        out.println("<meta http-equiv=\"expires\" content=\"0\">");
        out.println("<meta http-equiv=\"keywords\" content=\"keyword1,keyword2,keyword3\">");
        out.println("<meta http-equiv=\"description\" content=\"This is my page\">");
        out.println("</head>");
        out.println("<body>");
        consumer.accept(out);
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }


}
