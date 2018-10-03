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
			// ��socket�л�ȡ��Ϣ��
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			// ��socket��д��Ϣ��
			out = new PrintWriter(this.socket.getOutputStream(),true);
			String body = null;
			while(true) {
				// ��ȡsocket�е���Ϣ
				body = in.readLine();
				// ���socket��û����Ϣ��������
				if(body == null) break;
				System.out.println("Server: " + body);
				// ���ظ��ͻ�����Ϣ
				Scanner sc = new Scanner(System.in);
				
				out.println("�������˻�����Ӧ������: " + sc.nextLine());
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
