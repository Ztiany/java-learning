package juejin.netty.wechat.utils;

import java.util.UUID;

public class IDUtil {

    public static String createRandomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

}
