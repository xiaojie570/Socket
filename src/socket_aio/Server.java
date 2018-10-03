package socket_aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	// 线程池
	// 该线程池负责两个任务：处理IO事件和触发CompletionHandler回调接口。
	private ExecutorService executorService;
	// 线程组
	private AsynchronousChannelGroup threadGroup;
	// 服务器通信
	public AsynchronousServerSocketChannel assc;
	public Server(int port) throws InterruptedException {
		super();
		
		try {
			// 创建一个缓存池(ExecutorService)
			executorService = Executors.newCachedThreadPool();
			// 创建线程组(AsynchronousChannelGroup)
			threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService,1);
			// 创建服务器通道(AynchronousServerSocketChannel)
			assc = AsynchronousServerSocketChannel.open(threadGroup);
			// 进行绑定
			assc.bind(new InetSocketAddress(port));
			
			System.out.println("server start , port : " + port);
			
			// 调用accept（）方法来接收来自客户端的连接。
			// 由于异步IO实际的IO操作是交给操作系统来做的，用户进程只负责通知操作系统进行IO和接收操作系统IO完成的通知。
			// 所以异步的ServerChannel调用accept()方法后，当前线程不会阻塞，程序也不知道accept()方法什么时候能够接收到客户端请求
			// 并且操作系统完成网络IO，
			assc.accept(this, new ServerCompletionHandler());
			// 一直阻塞，不让服务器停止
			Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(9876);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
