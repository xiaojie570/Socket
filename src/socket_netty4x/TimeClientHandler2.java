package socket_netty4x;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 因为是基于流的传输，所以在传输过程中会使得传输的数据被拆分开，如何保证传输的数据不被拆分到不同的数据段内呢？
 * 最简单的方案是构造一个内部的可积累的缓冲，直到4个字节全部接收到了内部缓冲。
 * 
 * 
 * @author lenovo
 *
 */
public class TimeClientHandler2 extends ChannelInboundHandlerAdapter {

	private ByteBuf buf;
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		//(1) ChannelHandler有两个生命周期的监听方法，handlerAdded和handlerRemoved。
		// 你可以完成任意初始化任务只要他不会被阻塞很长的时间。
		buf = ctx.alloc().buffer(4); 
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		buf.release(); //(1)
		buf = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ByteBuf m = (ByteBuf) msg;
		buf.writeBytes(m); //(2) 首先，所有接受的数据都应该被积累在buf变量中
		m.release();
		
		//(3) 然后处理器必须检测buf变量是否有足够的数据，在这个例子中是4个字节，然后处理实际的业务逻辑。
		//    否则，Netty会重复调用channelRead（），当有更多数据到达直到4个字节的数据被积累。
		if(buf.readableBytes() >= 4) { 
			long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
			System.out.println(new Date(currentTimeMillis));
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
	
}
