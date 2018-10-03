package socket_buff;

import java.nio.IntBuffer;

public class TestBuff {
	public static void main(String[] args) {
		// 1. ��������
		
		// ����ָ�����ȵĻ�����
		IntBuffer buff = IntBuffer.allocate(10);
		buff.put(13); // positionλ�ã�0->1
		buff.put(21); // positionλ�ã�1->2
		buff.put(34); // positionλ�ã�2->3
		
		// ��λ�ø�λΪ0��Ҳ����positionλ�ã�3->0
		buff.flip();
		System.out.println("ʹ��flip��λ��" + buff);
		System.out.println("����Ϊ�� " + buff.capacity());
		System.out.println("����Ϊ�� " + buff.limit());
		
		System.out.println("��ȡ�±�Ϊ1��Ԫ�أ�" + buff.get(1));
		System.out.println("get(index)������positionλ�ò��䣺" + buff);
		buff.put(1, 22);
		System.out.println("put(index, change)������positionλ�ò��䣺" + buff);
		
		for(int i = 0; i < buff.limit(); i++) {
			// ����get������ʹ������λ�ã�position��������һλ
			System.out.println(buff.get() + "\t");
		}
		
		System.out.println("buff�������֮��" + buff);
		
		//2. wrap������ʹ��
		// wrap���������һ�����飺һ�������÷������ȳ�ʼ���������ĳ��ȣ���Ϊû�����壬��󻹻ᱻwrap�����������鸲�ǵ�
		// ����wrap�����޸Ļ����������ʱ�����鱾��Ҳ����ŷ����仯��
		int [] arr = new int[]{1,2,5};
		IntBuffer buff1 = IntBuffer.wrap(arr);
		System.out.println(buff1);
		
		IntBuffer buff2 = IntBuffer.wrap(arr, 0, 2);
		// ����ʹ�ñ�ʾ����Ϊ����arr�ĳ��ȣ����ǿɲ�����Ԫ��ֻ��ʵ�ʽ��뻺������Ԫ�س���
		System.out.println(buff2);
		
		//3. ��������
		IntBuffer buff3 = IntBuffer.allocate(10);
		int[] arr1 = new int[]{1,2,3};
		buff3.put(arr1);
		System.out.println(buff3);
		// һ�ָ��Ʒ���
		IntBuffer buff4 = buff3.duplicate();
		System.out.println(buff4);
		
		//����buff3��λ������
		buff3.position(0);
		buff3.flip();
		System.out.println(buff3);
		
		System.out.println("�ɶ�����Ϊ��" + buff3.remaining());
	}
}
