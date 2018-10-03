package socekt_netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	/**
	 * 对于ChannelOption.SO_BACKLOG的解释：
	 * 服务器端TCP内核模块维护有2个队列，我们称之为A和B
	 * 客户端向服务器端connect的时候，会发送带有SYN标志的包（第一次握手）
	 * 服务器接收到客户端发来的SYN的时候，向客户端发送SYN_ACK确认（第二次握手）
	 * 此时TCP内核模块把客户端连接加入到A队列中，然后服务器收到客户端发来的ACK时（第三次握手）
	 * TCP内核模块把客户端连接从A队列移到B队列，连接完成，应用程序的accept会返回
	 * 也就是说accept从B队列中取出完成三次握手的连接。
	 * A队列和B队列的长度之和是backlog。当A和B队列的长度之和大于backlog时，新连接将会被TCP内核拒绝。
	 * 所以，如果backlog过小，可能会出现accept速度跟不上，A和B队列满了，导致新的客户端无法连接。
	 * 要注意的是：backlog对程序支持的连接数并无影响，backlog影响的只是还没有被accept取出的连接
	 * @throws InterruptedException 
	 */
	
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup pGroup = new NioEventLoopGroup(); // 一个是用于处理服务器端接收客户端连接的
		EventLoopGroup cGroup = new NioEventLoopGroup(); // 一个是进行网络通信的（网络读写的）
		ServerBootstrap b = new ServerBootstrap();	     // 创建辅助工具类，用于服务器通道的一系列配置
		b.group(pGroup, cGroup)                          // 绑定两个线程组
		.channel(NioServerSocketChannel.class)          // 指定NIO模式
		.option(ChannelOption.SO_BACKLOG, 1024)        // 设置TCP缓冲池
		/*.option(ChannelOption.SO_SNDBUF, 3*1024)		 // 设置发送缓冲大小
		.option(ChannelOption.SO_RCVBUF, 32*1024)       // 这是接收缓冲大小
		.option(ChannelOption.SO_KEEPALIVE, true)      // 保持连续
*/		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				// 获取这个管道，加一个一个ServerHandler。业务逻辑都是放在ServerHandler中的
				sc.pipeline().addLast(new ServerHandler());  // 3. 在这里配置具体数据接收方法的处理
			}
		});
		
		ChannelFuture cf1 = b.bind(9876).sync();            // 4. 进行绑定
		
		cf1.channel().closeFuture().sync();               // 5. 等待关闭，是一个优雅关闭
		
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
	}
}
