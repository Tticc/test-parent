package com.tester.testerwebapp.controller.page.img;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

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


    public static final String CHECK_PWD = "edcxx";

    public static final String USER_TYPE = "user_type";

    public static final String INNER = "inner"; // 内部
    public static final String OUTER = "outer"; // 外部
    public static final String URL_TAG = "url"; // url tag

    public static final String HOME_URL = "/img/home"; // 主界面请求地址

    @Autowired
    private RouteManager routeManager;
    @Autowired
    private ImageManager imageManager;

    /**
     * home
     *
     * @Date 11:47 2021/11/12
     * @Author 温昌营
     **/
    @GetMapping("/home")
    public String getHome(HttpServletRequest req, HttpSession session) {
        if (INNER.equals(session.getAttribute(USER_TYPE))) {
            req.setAttribute("initContent", listHome());
        }
        return "img/home";
    }

    /**
     * test
     *
     * @Date 11:47 2021/11/12
     * @Author 温昌营
     **/
    @GetMapping("/test")
    public String getTest(HttpServletRequest req, HttpSession session) {
        return "img/test";
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
        jsonObject.put("content", listHome());
        jsonObject.put(URL_TAG, session.getAttribute(URL_TAG));
        return jsonObject.toString();
    }


    /**
     * 构建home内容
     *
     * @Date 11:11 2021/11/15
     * @Author 温昌营
     **/
    private String listHome() {
        return routeManager.buildHomeContent();
    }


    /**
     * 列出指定path的所有pic文件夹
     *
     * @Date 9:42 2021/11/12
     * @Author 温昌营
     **/
    @RequestMapping(value = "/path/{index}", method = RequestMethod.GET)
    public void listPath(HttpServletRequest request, HttpServletResponse response, @PathVariable int index) throws IOException, BusinessException {
        String s = routeManager.listPath(request, response, index);
        ImgCommon.flush(response, "path" + index, (out) -> {
            out.println(s);
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
        StringBuilder sb = new StringBuilder();
        int size = imageManager.listPic(sb, pathIndex, picIndex);
        String title = "pic" + picIndex + " - " + size;
        ImgCommon.flush(response, title, (out) -> {
            out.println(sb.toString());
        });
    }

    /**
     * 单图片
     * @Date 2021-11-21 16:07:31
     * @Author 温昌营
     */
    @RequestMapping(value = "/path/{pathIndex}/pic/{picIndex}/img/{imgIndex}", method = RequestMethod.GET)
    public void getImg(HttpServletResponse response, @PathVariable int pathIndex, @PathVariable int picIndex, @PathVariable int imgIndex) throws IOException, BusinessException {
        StringBuilder sb = new StringBuilder();
        String title = imageManager.getImg(sb, pathIndex, picIndex, imgIndex);
        ImgCommon.flush(response, title, (out) -> {
            out.println(sb.toString());
        });
    }

    /**
     * 图片位置
     * @Date 2021-11-21 16:07:36
     * @Author 温昌营
     */
    @RequestMapping(value = "/path/{pathIndex}/pic/{picIndex}/img/index/{imgIndex}", method = RequestMethod.GET)
    public void getImgIndex(HttpServletResponse response, @PathVariable int pathIndex, @PathVariable int picIndex, @PathVariable int imgIndex) throws IOException, BusinessException {
        StringBuilder sb = new StringBuilder();
        String title = imageManager.getImgIndex(sb, pathIndex, picIndex, imgIndex);
        ImgCommon.flush(response, title, (out) -> {
            out.println(sb.toString());
        });
    }

    /**
     * scrollGet
     *
     * @Date 2021-11-21 16:07:42
     * @Author 温昌营
     **/
    @ResponseBody
    @PostMapping("/scrollGet")
    public String scrollGet(@RequestParam("currUrl") String currUrl, @RequestParam("isUp") Boolean isUp) {
        int i1 = currUrl.indexOf(ImgCommon.IMG_HTTP_URL_PREFIX);
        String replace = currUrl.substring(i1 + ImgCommon.IMG_HTTP_URL_PREFIX.length());
        if(replace.startsWith("/")){
            replace = replace.substring(1);
        }
        String[] split = replace.split("/");
        int pathIndex = ImgCommon.getIndexByName(split[0]);
        int picIndex = ImgCommon.getIndexByName(split[1]);
        int imgIndex = ImgCommon.getIndexByName(split[2]);

        List<File> files = ImgCommon.getFiles(pathIndex, picIndex);
        List<File> sorted = ImgCommon.sortFilesByIndex(files);
        File preFile = null;
        File nextFile = null;
        for (int i = 0; i < sorted.size(); i++) {
            if(imgIndex == ImgCommon.getIndexByName(sorted.get(i).getName())){
                preFile = i - 1 >= 0 ? sorted.get(i-1) : null;
                nextFile = i + 1 >= sorted.size() ? null : sorted.get(i + 1);
            }
        }
        File targetFile = isUp ? preFile : nextFile;
        String s = "";
        if(targetFile != null){
            s = ImgCommon.fileUrl2HttpUrl(targetFile.getPath());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newOneSrc",s);
        return jsonObject.toString();
    }


}
