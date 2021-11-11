package com.tester.testercv;

import com.tester.testercommon.util.http.HttpsClient;
import com.tester.testercommon.util.pool.DefaultPool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * @Author 温昌营
 * @Date 2021-11-10 09:40:14
 */
public class ImgDownload {


    private static BlockingQueue<DownloadTask> queue = new LinkedBlockingQueue<>();

    private static Thread monitorThread;

    private static Proxy proxy = HttpsClient.getProxy("127.0.0.1", 10809);

    /**
     * main方法启动
     *
     * @Date 18:04 2021/11/10
     * @Author 温昌营
     **/
    public static void main(String[] args) throws Exception {
        ImgDownload imgDownload = new ImgDownload();
        imgDownload.start(DefaultPool::exec);
        imgDownload.pushTask(1,
                199,
                "http://www.xxx.xx.com",
                "C:\\Users\\Admin\\Desktop\\captureImg\\img\\");

    }

    /**
     * 启动守护线程
     *
     * @Date 18:04 2021/11/10
     * @Author 温昌营
     **/
    public void start(Consumer<Runnable> consumer) {
        // 启动一个线程，避免main退出后，下面的守护线程因为处于sleep状态而未能拉取任务开启新线程，进而只剩下守护线程，jvm退出
        consumer.accept(() -> {
        });
        MonitorTask monitorTask = new MonitorTask(consumer);
        Thread thread = new Thread(monitorTask, "my-monitor-deamon");
        monitorThread = thread;
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 追加任务
     *
     * @Date 18:04 2021/11/10
     * @Author 温昌营
     **/
    public void pushTask(int startIndex, int endIndex, String prefixUrl, String prefixFilePath) {
        DownloadTask downloadTask = new DownloadTask()
                .setStartIndex(startIndex)
                .setEndIndex(endIndex)
                .setCurrentIndex(startIndex)
                .setPrefixUrl(prefixUrl)
//                .setPostfixUrl(oriTask.getPostfixUrl())
                .setPrefixFilePath(prefixFilePath);
        putEle(downloadTask);
    }


    @Data
    @Accessors(chain = true)
    public static class MonitorTask implements Runnable {
        private Consumer<Runnable> consumer;
        public static final int DEFAULT = 0;
        public static final int STARTING = 1;
        public static final int STOPPED = 9;
        private int state = DEFAULT;

        public MonitorTask(Consumer<Runnable> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            this.state = STARTING;
            while (this.state != STOPPED) {
                DownloadTask poll = queue.poll();
                if (null == poll) {
                    LockSupport.park();
//                    try {
//                        Thread.sleep(1000L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    consumer.accept(poll);
                }
            }
        }
    }


    @Data
    @Accessors(chain = true)
    public static class DownloadTask implements Runnable {
        private int startIndex;
        private int endIndex;
        private int currentIndex;
        private String prefixUrl;
        private String postfixUrl = ".jpg";
        private String prefixFilePath = "";

        public static DownloadTask copyFrom(DownloadTask oriTask, int currentIndex) {
            return new DownloadTask()
                    .setStartIndex(oriTask.getStartIndex())
                    .setEndIndex(oriTask.getEndIndex())
                    .setCurrentIndex(currentIndex)
                    .setPrefixUrl(oriTask.getPrefixUrl())
                    .setPostfixUrl(oriTask.getPostfixUrl())
                    .setPrefixFilePath(oriTask.getPrefixFilePath());
        }

        @Override
        public void run() {
            try {
                while (this.currentIndex <= this.endIndex) {
                    String url = getUrl(this.prefixUrl, this.currentIndex, this.postfixUrl);
                    File outFile = new File(getFileName(this.prefixFilePath, this.currentIndex, ".jpg"));
                    HttpsClient.requestForFile(url, HttpsClient.GET_METHOD, null, null, outFile, proxy);
                    ++this.currentIndex;
                }
            } catch (Exception e) {
                putEle(this);
            }
        }
    }

    public static void putEle(DownloadTask task) {
        boolean offer = queue.offer(task);
        if (offer) {
            LockSupport.unpark(monitorThread);
        }
    }

    private static String getUrl(String prefixUrl, int currentIndex, String postfixUrl){
         String mid = ""+currentIndex;
//        String mid = currentIndex+"/pexels-photo-"+currentIndex;
        return prefixUrl + mid + postfixUrl;
    }

    private static String getFileName(String prefixFilePath, int currentIndex, String postfixFilePath) {
        String mid;
        if (currentIndex < 10) {
            mid = "000" + currentIndex;
        } else if (currentIndex < 100) {
            mid = "00" + currentIndex;
        } else if (currentIndex < 1000) {
            mid = "0" + currentIndex;
        } else {
            mid = "" + currentIndex;
        }
        return prefixFilePath + mid + postfixFilePath;
    }
}



