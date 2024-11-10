package com.ztiany.basic.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceGCTest {

    private static class BigObject {
        private byte[] data = new byte[1024 * 1024];
    }

    public static void main(String[] args) throws InterruptedException {
        weakReference();
        weakReferenceWithQueue();
        strongAndWeakReferenceWithQueue();
    }

    private static void strongAndWeakReferenceWithQueue() throws InterruptedException {
        ReferenceQueue<BigObject> referenceQueue = new ReferenceQueue<>();
        BigObject bigObject = new BigObject();
        WeakReference<BigObject> weakReference = new WeakReference<>(bigObject, referenceQueue);
        System.out.println("Before GC, weakReference get: " + weakReference.get());
        System.out.println("Before GC, referenceQueue poll: " + referenceQueue.poll());

        System.gc();
        Thread.sleep(1000);

        System.out.println("After GC, weakReference get: " + weakReference.get());
        System.out.println("After GC, referenceQueue poll: " + referenceQueue.poll());
    }

    private static void weakReferenceWithQueue() throws InterruptedException {
        ReferenceQueue<BigObject> referenceQueue = new ReferenceQueue<>();
        WeakReference<BigObject> weakReference = new WeakReference<>(new BigObject(), referenceQueue);
        System.out.println("Before GC, weakReference get: " + weakReference.get());
        System.out.println("Before GC, referenceQueue poll: " + referenceQueue.poll());

        System.gc();
        Thread.sleep(1000);

        System.out.println("After GC, weakReference get: " + weakReference.get());
        System.out.println("After GC, referenceQueue poll: " + referenceQueue.poll());
    }

    private static void weakReference() throws InterruptedException {
        WeakReference<BigObject> weakReference = new WeakReference<>(new BigObject());
        System.out.println("Before GC: " + weakReference.get());

        System.gc();
        Thread.sleep(1000);

        System.out.println("After GC: " + weakReference.get());
    }

}