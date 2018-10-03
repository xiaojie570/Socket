package socket_bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServerSocket {
	public static void main(String[] args) throws IOException {
		//1. 创建serverSocket
		ServerSocket serverSocket = new ServerSocket();
		//2. 设置端口号
		serverSocket.bind(new InetSocketAddress(9876));
		//3. 创建线程池
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		while(true) {
			System.out.println("监听端口为9876的服务器");
			//4. 阻塞，等待客户端的连接
			final Socket socket = serverSocket.accept();
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						// 打印当前线程的ID号
						System.out.println("当前线程的ID号：" + Thread.currentThread().getId());
						//a. 获得客户端的意图 request
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						// 存储读取的数据
						StringBuilder sb = new StringBuilder();
						String line = null;
						// 读取数据
						while((line = br.readLine()) != null) {
							// 将数据存储在StringBuilder中
							sb.append(line);
						}
						System.out.println("服务器收到的数据：" + sb.toString());
						//b. 返回客户端数据 获取输出流
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						pw.write("接收的数据：" + sb.toString() + "服务器已经接收到数据");
						// 将数据发送,flush表示强制的将缓冲区数据发出去，不需要等到缓冲区满
						pw.flush();
						br.close();
						pw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally{
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }       
				} 
			});
		}
	}
}
