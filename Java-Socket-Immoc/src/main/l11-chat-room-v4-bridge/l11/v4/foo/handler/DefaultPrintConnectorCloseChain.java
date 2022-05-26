package l11.v4.foo.handler;

import l11.v4.clink.core.Connector;

/**
 * 默认连接关闭打印链
 */
class DefaultPrintConnectorCloseChain extends ConnectorCloseChain {

    @Override
    protected boolean consume(ConnectorHandler handler, Connector connector) {
        System.out.println(handler.getClientInfo() + ":Exit!!, Key:" + handler.getKey().toString());
        return false;
    }

}
