package socket_netty4x;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * (1) NioEventLoopGroup: 用来处理I/O操作的多线程事件循环器，Netty提供了许多不同的EventLoopGroup的实现用来处理不同的传输。
 * 			在这个例子中，我们实现了一个服务端的应用，因此会有2个NioEventLoopGroup。第一个经常被叫做“boss”，用来接收进来的连接。
 * 			第二个经常被叫做“worker"，用来处理已经被接收的连接，一旦"boss"接收到连接，就会把连接信息注册到“worker”上。
 * 			如何知道多少个线程已经被使用，如何映射到已经创建的Channel上都需要依赖于EventLoopGroup的实现，并且可以通过构造函数来配置他们的关系。
 * (2) ServerBootstrap 是一个启动NIO服务的辅助启动类。你可以在这个服务中直接使用channel，但是这会是一个复杂的处理过程，通常不需要这样做
 * (3) 这里指定使用NioServerSocketChannel类来举例说明一个新的channel如何接收进来的连接
 * (4) 这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。
 * 		ChannelInitializer是一个特殊的处理类，他的目的是帮助使用者配置一个新的channel。也许你想通过增加一些处理类比如DiscardServerHandler来配置一个新的channel
 * 		或者其对应的ChannelPipeline来实现你的网络程序。		
 * @author lenovo
 *
 */
/**
 * @author lenovo
 *
 */
public class TimeServer implements Runnable{
	private int port;

	public TimeServer(int port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup(); // (1)
		try {
			ServerBootstrap bootStrap = new ServerBootstrap(); // (2)
			bootStrap.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)              // (3)
			.childHandler(new ChannelInitializer<SocketChannel>(){ // (4)
	
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast(new TimeServerHandler());
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)  // (5) 可以设置这里指定的Channel实现的配置参数。我们正在写一个TCP/IP服务端，因此我们被允许设置socket的参数选项比如keepAlive
			.childOption(ChannelOption.SO_KEEPALIVE,true);  // (6)
			
			// 绑定端口，开始接收进来的连接
			ChannelFuture f = bootStrap.bind(port).sync();  // (7)
			
			// 等待服务器，socket关闭
			// 在这个例子中，这里不会发生，但是你可以优雅地关闭你的服务器
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 9876;
		
		new Thread(new TimeServer(port)).start();
	}
	
}
