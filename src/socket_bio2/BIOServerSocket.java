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
		//1. ����serverSocket
		ServerSocket serverSocket = new ServerSocket();
		//2. ���ö˿ں�
		serverSocket.bind(new InetSocketAddress(9876));
		//3. �����̳߳�
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		while(true) {
			System.out.println("�����˿�Ϊ9876�ķ�����");
			//4. �������ȴ��ͻ��˵�����
			final Socket socket = serverSocket.accept();
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						// ��ӡ��ǰ�̵߳�ID��
						System.out.println("��ǰ�̵߳�ID�ţ�" + Thread.currentThread().getId());
						//a. ��ÿͻ��˵���ͼ request
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						// �洢��ȡ������
						StringBuilder sb = new StringBuilder();
						String line = null;
						// ��ȡ����
						while((line = br.readLine()) != null) {
							// �����ݴ洢��StringBuilder��
							sb.append(line);
						}
						System.out.println("�������յ������ݣ�" + sb.toString());
						//b. ���ؿͻ������� ��ȡ�����
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						pw.write("���յ����ݣ�" + sb.toString() + "�������Ѿ����յ�����");
						// �����ݷ���,flush��ʾǿ�ƵĽ����������ݷ���ȥ������Ҫ�ȵ���������
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
