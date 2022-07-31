package com.tester.testerswing.voice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BeepSoundProcessor {

    private static boolean started = true;

    private static BlockingQueue<BeepSoundTask> tasks = new LinkedBlockingQueue<>(2);

    private static Thread t = new Thread(() -> {
        try {
            BeepSoundTask tempTask;
            while (started) {
                if (null != (tempTask = tasks.poll(5, TimeUnit.SECONDS))) {
                    Jacobtest.textToSpeech(tempTask.getText(), tempTask.getVolume(), tempTask.getSpeed());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    public static BeepSoundTask generateTask(String text, int volume, int speed) {
        return new BeepSoundTask().setText(text).setVolume(volume).setSpeed(speed);
    }

    public static boolean putTask(BeepSoundTask task) {
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
