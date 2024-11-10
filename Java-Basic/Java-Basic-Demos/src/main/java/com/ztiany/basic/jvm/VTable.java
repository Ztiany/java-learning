package com.ztiany.basic.jvm;

import java.io.IOException;

abstract class A {

    public void printMe() {
        System.out.println("I love vim");
    }

    public abstract void sayHello();
}

class B extends A {

    @Override
    public void sayHello() {
        System.out.println("hello, i am child B");
    }

}

public class VTable {

    public static void main(String[] args) throws IOException {
        A obj = new B();
        System.in.read();
        System.out.println(obj);
    }

}
