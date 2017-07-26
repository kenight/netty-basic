package example.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// 很简单的一个例子，没有客户端，用 telnet 当客户端连接服务器
// 命令 telnet localhost 10086
// 任意输入服务器控制台中即可响应
public class Server {

	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void startup() throws Exception {
		// NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器
		// 实现服务端应用需要2个 NioEventLoopGroup
		// 第一个经常被叫做‘boss’，用来接收进来的连接
		// 第二个经常被叫做‘worker’，用来处理已经被接收的连接
		// 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// ServerBootstrap 是一个启动 NIO 服务的辅助启动类(配置参数等)
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			// 使用 NioServerSocketChannel 类创建 channel 实例
			b.channel(NioServerSocketChannel.class);
			// 这里的事件处理类 childHandler 经常会被用来处理一个最近的已经接收的 Channel
			// ChannelInitializer 是一个特殊的处理类，他的目的是帮助使用者配置一个新的 Channel
			// 如增加一些处理类比如 EchoChannelHandler 来配置一个新的 Channel
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ServerHandler());
				}
			});
			// 配置 Channel 参数
			// option() 是提供给NioServerSocketChannel 用来接收进来的连接。
			// childOption() 是提供给由父管道 ServerChannel 接收到的连接，在这个例子中也是
			// NioServerSocketChannel
			b.option(ChannelOption.SO_BACKLOG, 128);
			b.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			// 一直等待，直到socket被关闭，在这个例子中，这不会发生
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new Server(10086).startup();
	}

}
