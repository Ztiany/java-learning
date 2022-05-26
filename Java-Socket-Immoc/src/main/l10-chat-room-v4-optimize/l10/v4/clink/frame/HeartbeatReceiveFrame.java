package l10.v4.clink.frame;

import l10.v4.clink.core.IoArgs;

/**
 * 心跳接收帧
 */
public class HeartbeatReceiveFrame extends AbsReceiveFrame {

    static final HeartbeatReceiveFrame INSTANCE = new HeartbeatReceiveFrame();

    private HeartbeatReceiveFrame() {
        super(HeartbeatSendFrame.HEARTBEAT_DATA);
    }

    /*心跳没有任何数据*/
    @Override
    protected int consumeBody(IoArgs args) {
        return 0;
    }

}