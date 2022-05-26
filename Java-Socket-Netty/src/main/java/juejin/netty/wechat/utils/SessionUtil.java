package juejin.netty.wechat.utils;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import juejin.netty.wechat.common.attribute.Attributes;
import juejin.netty.wechat.common.protocol.request.LoginRequestPacket;
import juejin.netty.wechat.common.session.Session;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {

    private static final Map<String, Channel> loginUsers = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> groups = new ConcurrentHashMap<>();

    /**
     * 设置该 channel 对应的的用户已经登录
     */
    public static void bindSession(Session session, Channel channel) {
        loginUsers.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 移除该 channel 对应的用户的登录状态
     */
    public static void unbindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            loginUsers.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
            System.out.println(session + " 退出登录!");
        }
    }

    public static boolean hasLogin(Channel channel) {
        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    @Nullable
    public static Channel getChannel(Session session) {
        return loginUsers.get(session.getUserId());
    }

    @Nullable
    public static Channel getChannel(String userId) {
        return loginUsers.get(userId);
    }

    public static void bindChannelGroup(String groupId, ChannelGroup group) {
        groups.put(groupId, group);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groups.get(groupId);
    }

    /**
     * 验证登录请求
     */
    public static boolean verifyLoginRequest(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}