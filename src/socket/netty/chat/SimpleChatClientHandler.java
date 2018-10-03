package socket.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
		System.out.println(s);
	}

}
