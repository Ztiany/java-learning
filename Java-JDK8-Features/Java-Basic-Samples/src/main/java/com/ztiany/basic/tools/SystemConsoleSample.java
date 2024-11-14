package com.ztiany.basic.tools;


public class SystemConsoleSample {

    public static void main(String... args) {
        char[] chars = System.console().readPassword();
        System.out.println(new String(chars));
    }

}
