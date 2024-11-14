package com.ztiany.basic.exception;

public class UncaughtExceptionHandlerTest {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("thread running");
            throw new IllegalStateException("Dead");
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
