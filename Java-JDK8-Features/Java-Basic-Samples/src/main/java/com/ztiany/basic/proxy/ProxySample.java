package com.ztiany.basic.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 动态代理。
 *
 * @author Ztiany
 * Date : 2017-02-18 21:49
 * Email: ztiany3@gmail.com
 */
public class ProxySample {

    public static void main(String... args) {

        Class clazzProxy = Proxy.getProxyClass(Collection.class.getClassLoader(), Collection.class);
        System.out.println(clazzProxy.getName());

        for (Constructor constructor : clazzProxy.getConstructors()) {
            System.out.println(constructor.getName());
        }

        for (Method method : clazzProxy.getMethods()) {
            System.out.println(method.getName());
        }

        System.out.println("================================");

        List<String> list = new ArrayList<>();

        List proxy = (List) getProxy(list, new Advice() {
            @Override
            public void beforeMethod(Method method) {
                System.out.println("call before " + method.getName());
            }

            @Override
            public void afterMethod(Method method) {
                System.out.println("call after  " + method.getName());
            }
        });

        proxy.add(1);
        proxy.clear();

        System.out.println(proxy.hashCode());
        System.out.println(list.size());
        System.out.println(proxy.getClass());
    }

    private static Object getProxy(final Object target, final Advice advice) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, arguments) -> {
                    advice.beforeMethod(method);

                    // 无返回值的方法，在代理里返回对象不会崩溃。
                    if(method.getName().equals("clear")) {
                        return "AAA";
                    }

                    Object retVal = method.invoke(target, arguments);
                    advice.afterMethod(method);
                    return retVal;
                });
    }

    interface Advice {

        /**
         * 方法一般接收三个参数  target method args。
         */
        void beforeMethod(Method method);

        void afterMethod(Method method);

    }

}
