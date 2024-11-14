package com.ztiany.basic.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * @author Ztiany
 */
public class PhantomReferenceTest {

    public static void main(String... args) {
        Object counter = new Object();
        //引用队列
        ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
        //创建一个关联引用队列的PhantomReference
        PhantomReference<Object> p = new PhantomReference<>(counter, refQueue);
        counter = null;
        //在对象被 GC 的同时，会把该对象的包装类（即 PhantomReference）放入到 ReferenceQueue 里面。
        System.gc();
        try {
            // Remove 是一个阻塞方法，可以指定 timeout，或者选择一直阻塞
            Reference<?> ref = refQueue.remove(1000L);
            if (ref != null) {
                // do something
                System.out.println("haha");
            }
        } catch (InterruptedException e) {
            // Handle it
        }
    }

}