package com.ztiany.current.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用重入锁进行通讯，主线程执行100次，然后子线程执行10次
 */
public class ConditionCommunication {

    public static void main(String[] args) {

        final Business business = new Business();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 1; i <= 50; i++) {
                            business.sub(i);
                        }

                    }
                }
        ).start();

        for (int i = 1; i <= 50; i++) {
            business.main(i);
        }
    }

    static class Business {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        private boolean bShouldSub = true;

        public void sub(int i) {
            lock.lock();
            try {
                while (!bShouldSub) {
                    try {
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println("sub thread sequence of " + j + ",loop of " + i);
                }
                bShouldSub = false;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public void main(int i) {
            lock.lock();
            try {
                while (bShouldSub) {
                    try {
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 100; j++) {
                    System.out.println("main thread sequence of " + j + ",loop of " + i);
                }
                bShouldSub = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

    }
}
