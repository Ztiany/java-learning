package com.ztiany.basic.string;

/**
 * @author Ztiany
 */
public class StringCache {

    public static void main(String... args) {
        String s = new String("1");
        s.intern();
        String s2 = "1";
        System.out.println(s == s2);//false


        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);//true

        String s5 = new String("33");
        s5.intern();
        String s6 = "33";
        System.out.println(s5 == s6);//false
    }

}