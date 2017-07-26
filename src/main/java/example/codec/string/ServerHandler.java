package example.codec.string;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

	// 接收到新数据时
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// 接收信息
		System.out.println(ctx.channel().remoteAddress() + " : " + msg);

		// 返回客户端消息, \n 是必须的, 客户端接受时也使用 DelimiterBasedFrameDecoder 判断结束位置
		ctx.writeAndFlush("Received your message !\n");
	}
	
	// 客户端连接时触发
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " connected !");
		ctx.writeAndFlush( "Welcome!\n");
	}

}
