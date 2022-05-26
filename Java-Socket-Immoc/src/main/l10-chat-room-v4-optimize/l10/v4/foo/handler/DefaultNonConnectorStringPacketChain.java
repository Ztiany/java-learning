package l10.v4.foo.handler;

import l10.v4.clink.box.StringReceivePacket;

/**
 * 默认String接收节点，不做任何事情
 */
class DefaultNonConnectorStringPacketChain extends ConnectorStringPacketChain {

    @Override
    protected boolean consume(ConnectorHandler handler, StringReceivePacket packet) {
        return false;
    }

}
