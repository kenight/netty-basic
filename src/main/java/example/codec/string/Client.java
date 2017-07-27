package example.codec.string;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	private String host;
	private int port;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.handler(new ClientHandlerInitializer());

			// 连接服务器
			Channel ch = b.connect(host, port).sync().channel();

			// 接受系统输入
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			// 连续输入
			while (true) {
				String line = in.readLine();
				if (line == null) // 如果是空行, 则跳过本次循环, 不输出
					continue;
				ch.writeAndFlush(line + "\r\n");
			}
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Client("localhost", 10086).connect();
	}

}
