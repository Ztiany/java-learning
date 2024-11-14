package com.ztiany.basic.jvm;

import java.util.LinkedList;
import java.util.List;

/**
 * 通过时间间隔验证 StopTheWorld
 * <p>
 * VM参数： -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC -XX:-UseParNewGC
 */
public class StopWorldTest {

    /* 不停地往 list 中填充数据，触发GC。*/
    public static class FillListThread extends Thread {

        List<byte[]> list = new LinkedList<>();

        @Override
        public void run() {
            try {
                while (true) {
                    if (list.size() * 512 / 1024 / 1024 >= 990) {
                        list.clear();
                        System.out.println("list is clear");
                    }
                    byte[] bl;
                    for (int i = 0; i < 100; i++) {
                        bl = new byte[512];
                        list.add(bl);
                    }
                    Thread.sleep(1);
                }
            } catch (Exception e) {
            }
        }
    }

    /* 每 100ms 定时打印 */
    public static class TimerThread extends Thread {

        public final static long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - startTime;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(100); //0.1s
                }
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        // 填充对象线程和打印线程同时启动
        FillListThread fillListThread = new FillListThread(); //造成 GC，造成 STW
        TimerThread timerThread = new TimerThread(); //时间打印线程
        fillListThread.start();
        timerThread.start();
    }

}