package me.ztiany.asm.core.clazz.tools;

class ClassWithLambda {

    public static void main(String[] args) {
        Runnable r = () -> System.out.println("Hello World");
        r.run();
    }

}

class ClassWithCheckedLambda {

    public static void main(String[] args) {
        Runnable r = () -> {
            if (!Utils.shouldCall("Hello World")) {
                return;
            }
            System.out.println("Hello World");
        };
        r.run();
    }

}

class ViewClick {

    public void onClick(View view) {
        System.out.println("ViewClickSample.onClick");
    }

}

class ViewClickChecked {

    public void onClick(View view) {
        if (!ThrottleUtils.shouldCall(view)) {
            return;
        }
        System.out.println("ViewClickSample.onClick");
    }

}

class ViewClickWith1PChecked {

    public void onClick(Object p1, View view) {
        if (!ThrottleUtils.shouldCall(view)) {
            return;
        }
        System.out.println(p1);
        System.out.println("ViewClickSample.onClick");
    }

}

class ViewClickWith2PChecked {

    public void onClick(Object p1, Object p2, View view) {
        if (!ThrottleUtils.shouldCall(view)) {
            return;
        }
        System.out.println(p1);
        System.out.println(p2);
        System.out.println("ViewClickSample.onClick");
    }

}

class View {

}

class ThrottleUtils {

    public static boolean shouldCall(View view) {
        return true;
    }

}

class Utils {
    public static boolean shouldCall(String message) {
        return true;
    }
}