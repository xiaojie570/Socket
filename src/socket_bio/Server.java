package socket_bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	final static int PORT = 9876;
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			// 1. ���������� (��������������,�趨�ͻ�����������еĳ���)
			server = new ServerSocket(PORT);
			System.out.println("server start......");
			
			// 2. ��������������������
			while(true) {
				Socket socket = server.accept();
			
				// 3. �½�һ���߳�ִ�пͻ��˵����񣬾����ִ�й���ͨ������ServerHandler����ʵ��
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
