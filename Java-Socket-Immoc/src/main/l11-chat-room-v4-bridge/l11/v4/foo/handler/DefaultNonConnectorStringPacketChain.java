package l11.v4.foo.handler;

import l11.v4.clink.box.StringReceivePacket;

/**
 * 默认 String 接收节点，不做任何事情。
 */
class DefaultNonConnectorStringPacketChain extends ConnectorStringPacketChain {

    @Override
    protected boolean consume(ConnectorHandler handler, StringReceivePacket packet) {
        return false;
    }

}
