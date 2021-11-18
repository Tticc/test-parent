package com.tester.testerwebapp.controller.page.img;

import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2021-11-15 10:59:18
 */
public class ImgCommon {


    private static final ResourceLoader resourceLoader = new DefaultResourceLoader();

    // 配置文件实际位置。可以配置在本地文件，也可以配置到资源文件。
//    public static final String IMG_ROOT_PATH = "classpath:static" + File.separator + "img";
    public static final String IMG_ROOT_PATH = "file:C:\\Users\\Admin\\Desktop\\captureImg\\img\\";

    // 配置图片文件请求前缀。所有这个前缀，都会走特定位置。
    // 配置：com.tester.testerwebapp.controller.page.img.ImgWebConfig
    public static final String IMG_HTTP_URL_PREFIX = "/static/img";

    public static final Pattern isPic = Pattern.compile(".*?\\.(jpg|gif|jpeg)+$");

    public static final Pattern indexReg = Pattern.compile(".*?(\\d+).*?$");


    /**
     * 获取static/img目录下所有文件
     *
     * @Date 15:06 2021/11/15
     * @Author 温昌营
     **/
    public static List<File> getFiles() {
        return getFiles_sub(null, null, null);
    }

    /**
     * 获取static/img目录下某个文件夹下的所有文件
     *
     * @Date 15:06 2021/11/15
     * @Author 温昌营
     **/
    public static List<File> getFiles(Integer pathIndex) {
        assert null != pathIndex : "pathIndex should not null";
        return getFiles_sub(pathIndex, null, null);

    }

    /**
     * 比 getFiles(Integer pathIndex) 更下一层
     *
     * @Date 16:17 2021/11/15
     * @Author 温昌营
     **/
    public static List<File> getFiles(Integer pathIndex, Integer picIndex) {
        assert null != pathIndex : "pathIndex should not null";
        assert null != picIndex : "picIndex should not null";
        return getFiles_sub(pathIndex, picIndex, null);
    }

    /**
     * 比 getFiles(Integer pathIndex, Integer picIndex) 更下一层，获取指定图片
     *
     * @Date 16:17 2021/11/15
     * @Author 温昌营
     **/
    public static List<File> getFiles(Integer pathIndex, Integer picIndex, Integer imgIndex) {
        assert null != pathIndex : "pathIndex should not null";
        assert null != picIndex : "picIndex should not null";
        assert null != imgIndex : "imgIndex should not null";
        return getFiles_sub(pathIndex, picIndex, imgIndex);

    }

    private static List<File> getFiles_sub(Integer pathIndex, Integer picIndex, Integer imgIndex) {
        String path = ImgCommon.IMG_ROOT_PATH;
        List<File> files = getFiles(path);
        if (null != pathIndex) {
            Optional<File> first = files.stream().filter(e -> {
                int indexByName = getIndexByName(e.getName());
                return indexByName == pathIndex;
            }).findFirst();
            if (first.isPresent()) {
                File file = first.get();
                String name = file.getName();
                path = path + File.separator + name;
                files = getFiles(path);
            }
        }

        if (null != picIndex) {
            Optional<File> first = files.stream().filter(e -> {
                int indexByName = getIndexByName(e.getName());
                return indexByName == picIndex;
            }).findFirst();
            if (first.isPresent()) {
                File file = first.get();
                String name = file.getName();
                path = path + File.separator + name;
                files = getFiles(path);
            }
        }

        if (null != imgIndex) {
            Optional<File> first = files.stream().filter(e -> {
                int indexByName = getIndexByName(e.getName());
                return indexByName == imgIndex;
            }).findFirst();
            return first.map(Arrays::asList).orElseGet(ArrayList::new);
        }

        return files;
    }

    /**
     * 获取resources指定路径下某一层的文件
     *
     * @Date 13:49 2021/11/12
     * @Author 温昌营
     **/
    public static List<File> getFiles(String path) {
        List<File> res = null;
        try {
            Resource resource = resourceLoader.getResource(path);
            File file;
            File[] files;
            if (!(file = resource.getFile()).isDirectory()
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
     * 根据文件名包含的数字排序
     *
     * @Date 16:02 2021/11/15
     * @Author 温昌营
     **/
    public static List<File> sortFilesByIndex(List<File> files) {
        List<File> collect = files.stream()
                .sorted((a, b) -> {
                    Integer ta = getIndexByName(a.getName());
                    Integer tb = getIndexByName(b.getName());
                    return ta.compareTo(tb);
                })
                .collect(Collectors.toList());
        return collect;
    }


    /**
     * 文件路径转http地址路径
     *
     * @Date 15:59 2021/11/15
     * @Author 温昌营
     **/
    public static String fileUrl2HttpUrl(String fileUrl) {
        int i = IMG_ROOT_PATH.indexOf(":");
        String pathPrefix = IMG_ROOT_PATH.substring(i+1); // 如果没有，i是-1，从第0位开始读。如果第1位从第2位开始读，其他照常
        int i1 = fileUrl.indexOf(pathPrefix);
        String relativePath = fileUrl.substring(i1 + pathPrefix.length());
        String s = relativePath.replaceAll("\\\\", "/");
        s = s.startsWith("/") ? s : "/" + s;
        return IMG_HTTP_URL_PREFIX + s;
    }

    /**
     * 根据文件名获取index<br/>
     * 如：<br/>
     * path*12_important = 12 <br/>
     * 0101.jpg = 101 <br/>
     *
     * @Date 15:53 2021/11/15
     * @Author 温昌营
     **/
    public static int getIndexByName(String name) {
        Matcher matcher = indexReg.matcher(name);
        if (matcher.matches()) {
            String group1 = matcher.group(1);
            return Integer.parseInt(group1);
        }
        return 0;
    }


    /**
     * 公共response构建方法
     *
     * @Date 11:46 2021/11/12
     * @Author 温昌营
     **/
    public static void flush(HttpServletResponse response, String title, MyConsumer<PrintWriter> consumer) throws IOException, BusinessException {
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
