package com.ztiany.basic.jvm;

/**
 * 逃逸分析-栈上分配
 *
 * <pre>
 *     分别在启动和禁用逃逸分析的情况下运行该程序，逃逸分析默认启动，通过 -XX:-DoEscapeAnalysis 可以禁用。
 * </pre>
 *
 */
public class EscapeAnalysisSample {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 50000000; i++) {//5千万的对象，为什么不会垃圾回收
            allocate();
        }
        System.out.println((System.currentTimeMillis() - start) + " ms");
        Thread.sleep(600000);
    }

    static void allocate() {//满足逃逸分析（不会逃逸出方法）
        MyObject myObject = new MyObject(2020, 2020.6);
    }

    static class MyObject {
        int a;
        double b;

        MyObject(int a, double b) {
            this.a = a;
            this.b = b;
        }
    }

}