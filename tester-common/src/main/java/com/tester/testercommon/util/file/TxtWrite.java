package com.tester.testercommon.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author 温昌营
 * @Date
 */
@Slf4j
public class TxtWrite {
    public static String createTxtFile(String absFilePath, String fileName) throws IOException {
        String absFileName = absFilePath + File.separator + fileName;
        File contentFile = new File(absFileName);
        File checkDir= new File(absFilePath);
        if (!checkDir.exists()) {
            checkDir.mkdir();//创建文件夹
        }
        if (!contentFile.exists()) {
            contentFile.createNewFile();
        }
        return absFileName;
    }

    public static boolean writeTxtFile(String absFilePath,String content) throws Exception {
        if(StringUtils.isEmpty(content)){
            return true;
        }
        if(StringUtils.isEmpty(absFilePath)){
            log.error("writeTxtFile - 异常。写入文件路径为空！");
            return false;
        }
        boolean flag = false;
//        FileInputStream fis = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            File file = new File(absFilePath);
            // 将文件读入输入流
//            fis = new FileInputStream(file);
//            isr = new InputStreamReader(fis);
//            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            buf.append(content);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
//            if (br != null) {
//                br.close();
//            }
//            if (isr != null) {
//                isr.close();
//            }
//            if (fis != null) {
//                fis.close();
//            }
        }
        return flag;
    }
}
