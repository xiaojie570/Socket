package socket_bio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ServerHandler implements Runnable {
	private Socket socket;
	public ServerHandler(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		PrintWriter out =null;
		try {
			// 从socket中获取信息流
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			// 向socket中写信息流
			out = new PrintWriter(this.socket.getOutputStream(),true);
			String body = null;
			while(true) {
				// 读取socket中的信息
				body = in.readLine();
				// 如果socket中没有信息，则跳出
				if(body == null) break;
				System.out.println("Server: " + body);
				// 返回给客户端信息
				Scanner sc = new Scanner(System.in);
				
				out.println("服务器端回送响应的数据: " + sc.nextLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
