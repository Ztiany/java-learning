package com.ztiany.basic.jvm;

/**
 * 运行该程序后，使用 HSDB 查看 JVM 内存布局：
 *
 * <pre>
 *      JVM参数：-Xms30m -Xmx30m -XX:+UseConcMarkSweepGC -XX:-UseCompressedOops -Xss1m
 *
 *      说明：
 *          XX:-UseCompressedOops  禁用对象指针压缩（影响查看内存布局）
 *
 * </pre>
 */
public class JVMObject {

    public final static String MAN_TYPE = "man"; // 常量

    public static String WOMAN_TYPE = "woman";  // 静态变量

    public static void main(String[] args) throws Exception {//栈帧
        Teacher T1 = new Teacher();//堆中   T1 是局部变量
        T1.setName("Mark");
        T1.setSexType(MAN_TYPE);
        T1.setAge(36);
        for (int i = 0; i < 15; i++) {//进行15次垃圾回收
            System.gc();//垃圾回收
        }
        Teacher T2 = new Teacher();
        T2.setName("King");
        T2.setSexType(MAN_TYPE);
        T2.setAge(18);
        Thread.sleep(Integer.MAX_VALUE);//线程休眠很久很久
    }

}

class Teacher {

    String name;
    String sexType;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexType() {
        return sexType;
    }

    public void setSexType(String sexType) {
        this.sexType = sexType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

