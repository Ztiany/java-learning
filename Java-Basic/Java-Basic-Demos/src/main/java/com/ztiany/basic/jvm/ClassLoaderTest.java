package com.ztiany.basic.jvm;

public class ClassLoaderTest {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader());
    }

}