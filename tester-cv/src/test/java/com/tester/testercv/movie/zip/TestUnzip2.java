//package com.tester.testercv.movie.zip;
//
//import net.lingala.zip4j.core.ZipFile;
//import net.lingala.zip4j.exception.ZipException;
//import org.junit.Test;
//import org.springframework.util.StopWatch;
//
//
//public class TestUnzip2 {
//
//    @Test
//    public void test_unzip() throws ZipException {
//        String inFile = "C:\\Users\\wenc\\Desktop\\zip\\test\\lang.zip";
//        String outDir = "C:\\Users\\wenc\\Desktop\\zip\\test\\lang";
//        String passwd = "1234";
//        String[] strings = passwordSource();
//        int len = strings.length;
//        StopWatch stopWatch = new StopWatch();
//        int num = 0;
//        stopWatch.start();
//        ZipFile zipFile = new ZipFile(inFile);
//        for (int i = 0; i < len; i++) {
//            for (int j = 0; j < len; j++) {
//                for (int k = 0; k < len; k++) {
//                    for (int l = 0; l <len; l++) {
//                        for (int m = 0; m < len; m++) {
////                            for (int n = 0; n < len; n++) {
////                                for (int o = 0; o < len; o++) {
////                                    passwd = strings[i]+strings[j]+strings[k]+strings[l]+strings[m]+strings[n]+strings[o];
//                                    passwd = strings[i]+strings[j]+strings[k]+strings[l]+strings[m];
//                                    boolean unzip = unzip_new(zipFile, outDir, passwd);
//                                    ++num;
//                                    if(unzip){
//                                        stopWatch.stop();
//                                        long totalTimeMillis = stopWatch.getTotalTimeMillis();
//                                        System.out.println("password is: "+passwd+"。total mill: "+totalTimeMillis);
//                                        System.out.println("num is:"+num);
//                                        return;
////                                    }
////                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private static boolean unzip(String inFile,String outDir,String passwd) {
//        try {
//            ZipFile zipFile = new ZipFile(inFile);
//            // 如果解压需要密码
//            if(zipFile.isEncrypted()) {
//                zipFile.setPassword(passwd);
//            }
//            zipFile.extractAll(outDir);
//            return true;
//        } catch (ZipException e) {
////            System.out.println(passwd+"----"+e.getMessage());
//        }
//        return false;
//    }
//
//    private static boolean unzip_new(ZipFile zipFile,String outDir,String passwd) {
//        try {
//            zipFile.setPassword(passwd);
//            zipFile.extractAll(outDir);
//            return true;
//        } catch (ZipException e) {
////            System.out.println(passwd+"----"+e.getMessage());
//        }
//        return false;
//    }
//
//    public static String[] passwordSource(){
//        String str = "A";
////        String str = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
////        String str = "A C D E F G H I M N O Q R S T U W X Y Z";
////        str += " a b c d e f g h i j k l m n o p q r s t u v w x y z";
//        str += " 0 1 2 3 4 5 6 7 8 9";
//        str += " ! @ # %";
//        return str.split(" ");
//    }
//
//}
