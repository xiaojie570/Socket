package socket_buff;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test_Channel_Buffer {
	public static void main(String[] args) throws IOException {
		// 创建一个文件,文件名字为：demo
		File demo=new File("demo");
        if(!demo.exists()){
             demo.mkdir();
        }
        // 在文件名为demo的文件中，创建一个文件名为testchannel的文本
        File file=new File(demo,"testchannel");
        if(!file.exists()){
             file.createNewFile();
        }

        // RandomAccessFile Java提供的对文件内容的访问，既可以读文件也可以写文件，可以访问文件的任意位置适用于由大小已知的记录组成的文件
        // 内部包含一个文件指针，打开文件时指针在开头pointer=0;读写操作时指针会往后移动
		RandomAccessFile aFile = new RandomAccessFile(file, "rw");
		
		// 获得这个文件的channel通道
		FileChannel inChannel = aFile.getChannel();

		// 创建一个能够包含17个字节到缓冲区
		ByteBuffer buf = ByteBuffer.allocate(17);

		// 从channel通道中读取数据到buf缓冲区中
		int bytesRead = inChannel.read(buf);
		while (bytesRead != -1) {

			System.out.println("Read " + bytesRead);
			// 将Buffer从写模式切换到读模式。调用flip(）方法会将position设回0，并将limit设置成之前的position
			buf.flip();
	
			while(buf.hasRemaining()){
				System.out.print((char) buf.get());
			}
	
			buf.clear();
			bytesRead = inChannel.read(buf);
			
		}
		aFile.close();
	}
}
