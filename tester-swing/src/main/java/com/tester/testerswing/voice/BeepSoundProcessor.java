package com.tester.testerswing.voice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 提示音处理器
 *
 * @Date 9:56 2022/8/2
 * @Author 温昌营
 **/
public class BeepSoundProcessor {

    private static boolean started = true;

    private static BlockingQueue<BeepSoundTaskDTO> tasks = new LinkedBlockingQueue<>(1);

    private static Thread t = new Thread(() -> {
        try {
            BeepSoundTaskDTO tempTask;
            while (started) {
                if (null != (tempTask = tasks.poll(5, TimeUnit.SECONDS))) {
                    Jacobtest.textToSpeech(tempTask.getText(), tempTask.getVolume(), tempTask.getSpeed());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    public static BeepSoundTaskDTO generateTask(String text, int volume, int speed) {
        return new BeepSoundTaskDTO().setText(text).setVolume(volume).setSpeed(speed);
    }

    public static boolean putTask(BeepSoundTaskDTO task) {
        boolean offer = tasks.offer(task);
        if (!offer) {
            System.out.println("队列已满");
        }
        return offer;
    }

    public static boolean start() {
        started = true;
        t.start();
        return started;
    }

    public static boolean stop() {
        started = false;
        return started;
    }
}
