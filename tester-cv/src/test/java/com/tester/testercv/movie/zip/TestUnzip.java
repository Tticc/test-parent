package com.tester.testercv.movie.zip;

import de.idyl.winzipaes.AesZipFileDecrypter;
import de.idyl.winzipaes.impl.AESDecrypter;
import de.idyl.winzipaes.impl.AESDecrypterBC;
import de.idyl.winzipaes.impl.ExtZipEntry;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

public class TestUnzip {

    @Test
    public void test_unzip(){
        String inFile = "C:\\Users\\wenc\\Desktop\\zip\\test\\1.zip";
        String outDir = "C:\\Users\\wenc\\Desktop\\zip\\test\\lang";
        String passwd = "1234";
        unzip(inFile,outDir,passwd);
    }

    public static void unzip(String inFile, String outDir, String passwd) {
        File outDirectory = new File(outDir);
        if (!outDirectory.exists()) {
            outDirectory.mkdir();
        }
        AESDecrypter decrypter = new AESDecrypterBC();
        AesZipFileDecrypter zipDecrypter = null;
        try {
            zipDecrypter = new AesZipFileDecrypter(new File(inFile), decrypter);
            AesZipFileDecrypter.charset = "utf-8";
            /**
             * 得到ZIP文件中所有Entry,但此处好像与JDK里不同,目录不视为Entry
             * 需要创建文件夹,entry.isDirectory()方法同样不适用,不知道是不是自己使用错误
             * 处理文件夹问题处理可能不太好
             */
            List<ExtZipEntry> entryList = zipDecrypter.getEntryList();
            for(ExtZipEntry entry : entryList) {
                String eName = entry.getName();
                String dir = eName.substring(0, eName.lastIndexOf(File.separator) + 1);
                File extractDir = new File(outDir, dir);
                if (!extractDir.exists()) {
                    extractDir.mkdirs();
                }
                /**
                 * 抽出文件
                 */
                File extractFile = new File(outDir + File.separator + eName);
                zipDecrypter.extractEntry(entry, extractFile, passwd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                zipDecrypter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
