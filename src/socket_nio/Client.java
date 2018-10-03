package socket_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
	// ��Ҫһ��Selector
	
	public static void main(String[] args) {
		// �������ӵĵ�ַ
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);
		
		// ��������ͨ��
		SocketChannel sc = null;
		
		//����������
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		try {
			//��ͨ��
			sc = SocketChannel.open();
			// �������ӣ�������һ��TCP�ģ����Ǽ򵥵�һ��channel������
			sc.connect(address);
			
			while(true) {
				/*int count = sc.read(buf);
				System.out.println(count);
				if(count != -1) {
					buf.flip();
					byte[] res = new byte[buf.remaining()];
					buf.get(res);
					String body = new String(res).trim();
					System.out.println("���յ�����server�����ݣ�" + body);
				}*/
				// ����һ���ֽ����飬Ȼ��ʹ��ϵͳ¼�빦��
				byte[] bytes = new byte[1024];
				System.in.read(bytes);
				
				// �����ݷŵ���������
				buf.put(bytes);
				// �Ի��������и�λ
				buf.flip();
				// д������
				sc.write(buf);
				// ��ջ���������
				buf.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(sc != null) {
				try {
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
