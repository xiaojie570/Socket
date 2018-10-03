package socket_bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BIOClientSocket {
	public static void main(String[] args) {
		// ʹ���߳�ģ���û��������ʡ�
		for(int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					try {
						// 1. ����socket
						Socket socket = new Socket();
					
						// 2. ���ӷ�����
						socket.connect(new InetSocketAddress("127.0.0.1",9876));
						
						//a. �������ݵ�������
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						pw.write("���ǿͻ�������........");
						// �����ݷ���
						pw.flush();
						// ��֪�������Ѿ��������Ľ�β
						socket.shutdownOutput();
						
						//b. ��÷������Ļ�Ӧ
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						// �洢��ȡ������
						StringBuilder sb = new StringBuilder();
						String line = null;
						// ��ȡ����
						while((line = br.readLine()) != null) {
							// �����ݱ�����StringBuilder��
							sb.append(line);
						}
						// ��ӡ��������Ӧ����
						
						System.out.println(sb.toString());
						
						// �ر���
						
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
