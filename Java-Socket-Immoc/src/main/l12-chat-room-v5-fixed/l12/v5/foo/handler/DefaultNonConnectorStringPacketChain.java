package l12.v5.foo.handler;

import l12.v5.clink.box.StringReceivePacket;

/**
 * 默认 String 接收节点，不做任何事情。
 */
class DefaultNonConnectorStringPacketChain extends ConnectorStringPacketChain {

    @Override
    protected boolean consume(ConnectorHandler handler, StringReceivePacket packet) {
        return false;
    }

}
