package socket_aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class Client implements Runnable{
	private AsynchronousSocketChannel asc;

	public Client() throws IOException {
		super();
		asc = AsynchronousSocketChannel.open();
	}
	
	public void connect() {
		asc.connect(new InetSocketAddress("127.0.0.1",9876));
	}
	
	public void write(String request) {
		try {
			asc.write(ByteBuffer.wrap(request.getBytes())).get();
			read();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void read() {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			asc.read(buf).get();
			buf.flip();
			byte[] respByte = new byte[buf.remaining()];
			buf.get(respByte);
			System.out.println(new String(respByte,"utf-8").trim());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Client c1 = new Client();
		c1.connect();
		
		Client c2 = new Client();
		c2.connect();
		
		Client c3 = new Client();
		c3.connect();
		
		new Thread(c1,"c1").start();
		new Thread(c2,"c2").start();
		new Thread(c3,"c3").start();
		
		Thread.sleep(1000);
		
		c1.write("c1 aaaa");
		c2.write("c2 bbbb");
		c3.write("c3 cccc");
	}
}
