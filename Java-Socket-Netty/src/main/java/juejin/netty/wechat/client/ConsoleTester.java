package juejin.netty.wechat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleTester {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static Thread sThread;

    public static void main(String[] args) {
        //testScanner();
        //testBufferReaderReadLine();
        testBufferReaderRead();
        endThread();
    }

    private static void testScanner() {
        sThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            do {
                System.out.println(scanner.nextLine());
                System.out.println(sThread.isInterrupted());
            } while (!sThread.isInterrupted());
        });

        sThread.start();
    }

    private static void testBufferReaderRead() {
        sThread = new Thread(() -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            do {
                try {
                    if (bufferedReader.ready()) {
                        System.out.println("read...");
                        System.out.println(bufferedReader.read());
                    } else {
                        System.out.println("ready return false");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!sThread.isInterrupted());
        });

        sThread.start();
    }

    private static void testBufferReaderReadLine() {
        sThread = new Thread(() -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            do {
                try {
                    System.out.println(bufferedReader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(sThread.isInterrupted());
            } while (!sThread.isInterrupted());
        });

        sThread.start();
    }

    private static void endThread() {
        executor.schedule(() -> {
            sThread.interrupt();
            executor.shutdown();
        }, 10, TimeUnit.SECONDS);
    }

}
