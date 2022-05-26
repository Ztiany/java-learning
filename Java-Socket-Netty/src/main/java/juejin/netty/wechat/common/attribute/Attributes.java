package juejin.netty.wechat.common.attribute;

import io.netty.util.AttributeKey;
import juejin.netty.wechat.common.session.Session;

public class Attributes {

    public static final AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
