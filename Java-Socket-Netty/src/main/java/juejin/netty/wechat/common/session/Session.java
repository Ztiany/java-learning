package juejin.netty.wechat.common.session;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示一个会话
 */
@Data
@NoArgsConstructor
public class Session {

    /**
     * 用户唯一性标识
     */
    private String userId;

    private String userName;

    public Session(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userId + ":" + userName;
    }

}