package socket_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 服务端channel也要注册到多路复用器上
 * @author lenovo
 *
 */

public class Server implements Runnable{

	//1. 建立一个多路复用器（管理所有的通道）
	private Selector selector;
	//2. 建立缓冲区
	private ByteBuffer readBuf = ByteBuffer.allocate(1024);
	private ByteBuffer writeBuf = ByteBuffer.allocate(1024);
	
	// 在Server的构造函数中进行了一系列的操作
	public Server(int port) {
		try {
			//1. 打开多路复用器
			this.selector = Selector.open();
			//2. 打开服务器通道， open是一个静态方法
			ServerSocketChannel ssc = ServerSocketChannel.open();
			//3. 设置服务器通道为非阻塞模式
			ssc.configureBlocking(false);
			//4. 绑定地址
			ssc.bind(new InetSocketAddress(port));
			//5. 把服务器通道注册到多路复用器上，并且监听阻塞事件。这个channel一直在等着
			ssc.register(this.selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("Server start, port : " + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Override
	// 循环Client端注册到多路复用器的channel，看看这些channel的标志位都是什么状态
	public void run() {
		while(true) {
			try {
				//1. 必须要让多路复用器开始监听。表示注册在多路复用器上的通道都在被监听.
				// select方法阻塞到至少有一个通道在注册的事件上就绪了。
				this.selector.select();
				//2. 返回多路复用器已经选择的结果集（每次循环都要遍历注册到selector上的所有key）
				Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
				//3. 进行遍历
				while(keys.hasNext()) {
					//4. 获取一个选择的元素
					SelectionKey key = keys.next();
					//5. 直接从容器中移除就可以了，因为这个操作已经在下面被处理完了，
					//   如果之后channel中又有数据了，就需要重新注册
					keys.remove();
					//6. 是否是有效的，比如Client与服务器端断掉了就不是有效的
					if(key.isValid()) {
						//7. 如果为阻塞状态
						if(key.isAcceptable())
							this.accept(key);
						
						//8. 如果为可读状态
						if(key.isReadable())
							this.read(key);
						
						//9. 写数据
						if(key.isWritable()){
							this.write(key);
						}
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void write(SelectionKey key) throws IOException {
		/*SocketChannel sc = (SocketChannel) key.channel();
		this.writeBuf.clear();
		byte[] bytes = new byte[1024];
		System.in.read(bytes);
		this.writeBuf.put(bytes);
		writeBuf.flip();
		sc.write(writeBuf);
		sc.register(this.selector, SelectionKey.OP_READ);*/
	}
	private void read(SelectionKey key) {
		try {
			//1. 清空缓冲区旧的数据
			this.readBuf.clear();
			//2. 获取之前注册的socket通道对象
			SocketChannel sc = (SocketChannel) key.channel();
			//3. 读取数据
			int count = sc.read(this.readBuf);
			//4. 如果没有数据
			if(count == -1) {
				key.channel().close();
				key.cancel();
				return;
			}
			//5. 有数据则进行读取，读取之前需要进行复位方法（把position和limit进行复位
			this.readBuf.flip();
			//6. 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
			byte[] bytes = new byte[this.readBuf.remaining()];
			//7. 接收缓冲区数据，将readBuf中的数据写到bytes中
			this.readBuf.get(bytes);
			//8. 打印结果
			String body = new String(bytes).trim();
			System.out.println("Server : " + body);
			
			//9. 可以写回给客户端数据
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// 通过key即可以拿到对应可以的channel  如果是阻塞的情况下，就执行accept方法
	private void accept(SelectionKey key) {
		try {
			//1. 获取服务通道
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			
			//2. 执行阻塞方法。（等待客户端的通道是否注册上了）
			SocketChannel sc = ssc.accept();
			
			//3. 设置阻塞模式
			sc.configureBlocking(false);
			
			//4. 注册到多路复用器上，并设置读取标志
			sc.register(this.selector, SelectionKey.OP_READ);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new Thread(new Server(8765)).start();
	}
}
