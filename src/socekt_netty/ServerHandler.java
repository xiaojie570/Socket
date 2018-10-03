package socekt_netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;  //　注意ByteBuf是nio中的
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		
		String body = new String(req,"utf-8");
		System.out.println("Server : " + msg);
		
		// 服务器端给客户端的响应
		String response = "hi fujie, i am server cuidapanger";
		ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes())); // 写并且冲刷
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	
}
