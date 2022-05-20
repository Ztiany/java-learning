package lagou.netty.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseSampleEncoder extends MessageToByteEncoder<ResponseSample> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseSample msg, ByteBuf out) throws Exception {
        if (msg != null) {
            out.writeBytes(msg.getCode().getBytes());
            out.writeBytes(msg.getData().getBytes());
            out.writeLong(msg.getTimestamp());
        }
    }

}


class ResponseSample {

    private final String code;
    private final String data;
    private final long timestamp;

    public ResponseSample(String code, String data, long timestamp) {
        this.code = code;
        this.data = data;
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

}