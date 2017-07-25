package example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoChannelHandler extends ChannelInboundHandlerAdapter {

	// 事件处理方法。每当从客户端收到新的数据时，这个方法会在收到消息时被调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.write(msg);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		// 当出现异常就关闭连接
		ctx.close();
	}
}
