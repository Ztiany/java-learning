package juejin.netty.wechat.common.protocol.command;

/**
 * 定义了所有的指令
 */
public final class Command {

    /**
     * 登录请求响应
     */
    public static final byte LOGIN_REQUEST = 1;

    /**
     * 登录请求
     */
    public static final byte LOGIN_RESPONSE = 2;

    /**
     * 消息发送
     */
    public static final byte MESSAGE_REQUEST = 3;

    /**
     * 消息响应
     */
    public static final byte MESSAGE_RESPONSE = 4;

    /**
     * 登出请求
     */
    public static final byte LOGOUT_REQUEST = 5;

    /**
     * 登出响应
     */
    public static final byte LOGOUT_RESPONSE = 6;

    /**
     * 建群请求
     */
    public static final byte CREATE_GROUP_REQUEST = 7;

    /**
     * 建群响应
     */
    public static final byte CREATE_GROUP_RESPONSE = 8;

    /**
     * 列出群成员请求
     */
    public static final byte LIST_GROUP_MEMBERS_REQUEST = 9;

    /**
     * 列出群成员响应
     */
    public static final byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    /**
     * 加入群请求
     */
    public static final byte JOIN_GROUP_REQUEST = 11;

    /**
     * 加入群响应
     */
    public static final byte JOIN_GROUP_RESPONSE = 12;

    /**
     * 退出群请求
     */
    public static final byte QUIT_GROUP_REQUEST = 13;

    /**
     * 退出群响应
     */
    public static final byte QUIT_GROUP_RESPONSE = 14;

    /**
     * 发送群消息请求
     */
    public static final byte GROUP_MESSAGE_REQUEST = 15;

    /**
     * 发送群消息响应
     */
    public static final byte GROUP_MESSAGE_RESPONSE = 16;

    /**
     * 心跳请求
     */
    public static final byte HEARTBEAT_REQUEST = 17;

    /**
     * 心跳响应
     */
    public static final byte HEARTBEAT_RESPONSE = 18;

}
