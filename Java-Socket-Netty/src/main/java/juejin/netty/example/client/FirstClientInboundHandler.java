package juejin.netty.example.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstClientInboundHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当 Channel 被注册到它的 EventLoop 时调用。EventLoop 是一个事
     * 件循环，负责处理该 Channel 的所有事件。<br/>
     * 可以在此方法中执行一些初始化逻辑。例如建立某些资源。
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 当 Channel 从它的 EventLoop 中注销时调用，表示该 Channel 不再
     * 与事件循环关联。<br/>
     * 可以释放一些在 channelRegistered 中分配的资源，或记录日志。
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * 当 Channel 激活并准备好处理数据时调用。通常意味着通道已经成功
     * 连接到远程对端。<br/>
     * 可以在此方法中启动一些初始化通信，如发送一条连接确认消息，或
     * 做一些状态的初始化工作。
     */
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端写出数据");
        // 1. 获取数据
        ByteBuf buffer = getByteBuf(ctx);
        // 2. 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    /**
     * 当 Channel 变为不活跃状态时调用。可能意味着连接被关闭，通道的
     * 生命周期结束。<br/>
     * 常用于清理资源、关闭连接或记录日志。这是进行资源回收的最佳位置。
     */
    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /* Netty 里面数据是以 ByteBuf 为单位的， 所有需要写出的数据都必须塞到一个 ByteBuf，数据的写出是如此，数据的读取亦是如此。 */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();
        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好，我是客户端!".getBytes(StandardCharsets.UTF_8);
        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }

    /**
     * 当从对端读取到消息时调用。msg 参数是接收到的数据。<br/>
     * 在此方法中处理入站消息。可以将消息解码、处理，并决定是否将其
     * 传递到下一个 ChannelHandler。
     */
    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    /**
     * 在 channelRead 完成并且所有消息都被消费后调用。<br/>
     * 可以在这里决定是否进行进一步的读取操作。例如，如果 AUTO_READ
     * 选项为关闭状态，可以在此方法中调用 ctx.read() 手动触发新的读操作。
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    /**
     * 当触发用户自定义事件时调用。自定义事件通常用于在 ChannelPipeline 中传递某种特定的信号或通知。<br/>
     * 用于处理特定的自定义事件。例如，可以在此处理心跳包、超时事件等。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 当 Channel 的可写状态发生变化时调用。可以通过 Channel.isWritable()
     * 检查当前的可写状态。<br/>
     * 用于控制写入操作的流量。当通道变为不可写时，可以暂时停止发送
     * 数据；当通道恢复为可写状态时，可以恢复发送。
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 当出现异常时调用。用于处理异常，例如记录日志或关闭连接。可以
     * 选择在处理异常后调用 ctx.close() 以关闭连接。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}