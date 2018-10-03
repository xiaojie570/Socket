package socket_netty4x;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @author lenovo
 * 处理服务端 channel
 * DiscardServerHandler 继承自 ChannelInboundHandlerAdapter，
 * 这个类实现了 ChannelInboundHandler接口，
 * ChannelInboundHandler 提供了许多事件处理的接口方法，然后你可以覆盖这些方法。
 * 现在仅仅只需要继承 ChannelInboundHandlerAdapter 类而不是你自己去实现接口方法
 * 
 *  ECHO 协议
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter{

	// 这里覆盖了channelRead()事件处理方法。每当从客户端接收到新的数据的时候，
	// 这个方法会在收到消息时被调用。
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		/*ByteBuf in = (ByteBuf) msg;
		while(in.isReadable()) {
			System.out.println((char)in.readByte());
			System.out.flush();
		}*/ //将数据打印到控制台 
		
		// ChannelHandlerContext 对象提供了许多操作，使你能够触发各种各样的I/O事件和操作。这里我们调用了
				// write()方法来逐字地把接收到的消息写入。
				// ctx.write(Object)方法不会使消息写入到通道上，他被缓冲在了内部，你需要调用ctx.flush方法来把缓冲区中数据强行输出。
				// 或者你可以用更简洁的ctx.writeAndFlush（msg）以达到同样的目的
		
		ctx.write(msg);
        ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 当出现异常就关闭连接
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
	
}
