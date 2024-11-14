package com.ztiany.basic.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author Aleksey Shipilev
 */
public class JOL_Demo {

    /*
     * This sample showcases the basic field layout.
     *
     * You can see a few notable things here:
     *   a) how much the object header consumes;
     *   b) how fields are laid out;
     *   c) how the external alignment beefs up the object size
     */
    public static void main(String[] args) {
        System.out.println("-------------------------------------------------------");
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(A.class).toPrintable());
        System.out.println("-------------------------------------------------------");
    }

    public static class A {
        boolean f;
    }

}