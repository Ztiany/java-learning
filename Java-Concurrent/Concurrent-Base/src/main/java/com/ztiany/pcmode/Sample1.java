package com.ztiany.pcmode;

/**
 * 消费者生产者模型。
 *
 * @author Ztiany
 */
public class Sample1 {

    private static int sProduct = 0;

    private static int sCustomerCount = 0;
    private static int sProducerCount = 0;

    private static final Object LOCK = new Object();

    public static void main(String... args) {
        new Thread(new Customer()).start();
        new Thread(new Producer()).start();
    }

    private static class Customer implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                synchronized (LOCK) {
                    while (sProduct <= 0) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sProduct--;
                    sCustomerCount--;
                    System.out.println("总共消费产品：" + sCustomerCount);
                    LOCK.notifyAll();
                }
            }
        }

    }

    private static class Producer implements Runnable {

        @Override
        public void run() {
            for (; ; ) {
                synchronized (LOCK) {
                    while (sProduct >= 100) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sProduct++;
                    sProducerCount++;
                    LOCK.notifyAll();
                }
                System.out.println("总共生产产品：" + sProducerCount);
            }
        }
    }

}