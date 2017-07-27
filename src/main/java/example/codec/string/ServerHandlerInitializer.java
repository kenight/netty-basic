package example.codec.string;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

	// ChannelPipeline 的执行顺序, 举例说明(官方文档)
	// p.addLast("1", new InboundHandlerA());
	// p.addLast("2", new InboundHandlerB());
	// p.addLast("3", new OutboundHandlerA());
	// p.addLast("4", new OutboundHandlerB());
	// p.addLast("5", new InboundOutboundHandlerX());
	// 如果是接收，执行顺序：1,2,5 (实现 ChannelInboundHandler 的处理类）
	// 如果是流出，执行顺序：5,4,3 (实现 ChannelOutboundHandler 的处理类)
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 以("\n")为结尾分割的解码器
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		// 字符解码器
		ch.pipeline().addLast(new StringDecoder());
		// 字符编码器
		ch.pipeline().addLast(new StringEncoder());
		// 自定义
		ch.pipeline().addLast(new ServerHandler());
	}

}
