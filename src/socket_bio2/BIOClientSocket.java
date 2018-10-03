package socket_bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BIOClientSocket {
	public static void main(String[] args) {
		// 使用线程模拟用户并发访问、
		for(int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					try {
						// 1. 创建socket
						Socket socket = new Socket();
					
						// 2. 连接服务器
						socket.connect(new InetSocketAddress("127.0.0.1",9876));
						
						//a. 发送数据到服务器
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						pw.write("这是客户端数据........");
						// 将数据发送
						pw.flush();
						// 告知服务器已经到了流的结尾
						socket.shutdownOutput();
						
						//b. 获得服务器的回应
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						// 存储读取的数据
						StringBuilder sb = new StringBuilder();
						String line = null;
						// 读取数据
						while((line = br.readLine()) != null) {
							// 将数据保存在StringBuilder中
							sb.append(line);
						}
						// 打印服务器回应数据
						
						System.out.println(sb.toString());
						
						// 关闭流
						
						br.close();
						pw.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();;
		}
	}
}
