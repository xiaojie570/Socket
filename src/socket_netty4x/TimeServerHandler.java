package socket_netty4x;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 这个例子与之前的例子不同的是在不接受任何请求时他会发送一个含有32位的整数的消息，
 * 并且一旦消息发送就会立即关闭练级。在这个例子中，会学到如何构建和发送一个消息，然后在完成时关闭连接
 * 
 * (1) channelActive() 方法将会在连接被建立并且准备进行通信时被调用。因此让我们在这个方法里完成一个代表当前时间的32位整数消息的构建工作
 * (2) 为了发送一个新的消息，我们需要分配一个包含这个消息的新的缓冲。因为我们需要写入一个32位的整数，因此我们需要一个至少有4个字节的 ByteBuf。
 * 		通过 ChannelHandlerContext.alloc() 得到一个当前的ByteBufAllocator，然后分配一个新的缓冲。
 * (3) 和往常一样我们需要编写一个构建好的消息。但是等一等，flip 在哪？难道我们使用 NIO 发送消息时不是调用 java.nio.ByteBuffer.flip() 吗？
 * 		ByteBuf 之所以没有这个方法因为有两个指针，一个对应读操作一个对应写操作。当你向 ByteBuf 里写入数据的时候写指针的索引就会增加，同时读指针的索引没有变化。
 * 		读指针索引和写指针索引分别代表了消息的开始和结束。(与NIO不同的：Netty对于不同的操作类型有不同的指针)
 * (4) 当一个写请求已经完成是如何通知到我们？这个只需要简单地在返回的 ChannelFuture 上增加一个ChannelFutureListener。
 * 		这里我们构建了一个匿名的 ChannelFutureListener 类用来在操作完成时关闭 Channel
 * @author lenovo
 *
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { //(1)
		super.channelActive(ctx); 
		final ByteBuf time = ctx.alloc().buffer(4);  //(2)
		time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L)); 
		
		// 另外一个点需要注意的是 ChannelHandlerContext.write() (和 writeAndFlush() )方法会返回一个 ChannelFuture 对象，一个 ChannelFuture 代表了一个还没有发生的 I/O 操作。
		// 这意味着任何一个请求操作都不会马上被执行，因为在 Netty 里所有的操作都是异步的。
		final ChannelFuture f = ctx.writeAndFlush(time); //(3)
		f.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				assert f == future;
				ctx.close();
			}
			
		});//(4)
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
	
}
