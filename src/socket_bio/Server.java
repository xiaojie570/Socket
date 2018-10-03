package socket_bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	final static int PORT = 9876;
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			// 1. 开启服务器 (建立服务器连接,设定客户连接请求队列的长度)
			server = new ServerSocket(PORT);
			System.out.println("server start......");
			
			// 2. 进程阻塞，服务器监听
			while(true) {
				Socket socket = server.accept();
			
				// 3. 新建一个线程执行客户端的任务，具体的执行过程通过调用ServerHandler类来实现
				new Thread(new ServerHandler(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(server != null) {
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
