package socekt_netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		
		ChannelFuture cf = b.connect("127.0.0.1", 9876).sync();
		
		cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello server!!!".getBytes()));
		Thread.sleep(1000);
		cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello server!!!".getBytes()));
		Thread.sleep(1000);
		cf.channel().writeAndFlush(Unpooled.copiedBuffer("hello server!!!".getBytes()));
		Thread.sleep(1000);
		
		cf.channel().closeFuture().sync();
		group.shutdownGracefully();
	}
}
