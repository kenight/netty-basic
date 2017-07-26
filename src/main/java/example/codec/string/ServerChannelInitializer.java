package example.codec.string;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

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
