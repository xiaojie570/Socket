package socket_buff;

import java.nio.IntBuffer;

public class TestBuff {
	public static void main(String[] args) {
		// 1. 基本操作
		
		// 创建指定长度的缓冲区
		IntBuffer buff = IntBuffer.allocate(10);
		buff.put(13); // position位置：0->1
		buff.put(21); // position位置：1->2
		buff.put(34); // position位置：2->3
		
		// 把位置复位为0，也就是position位置：3->0
		buff.flip();
		System.out.println("使用flip复位：" + buff);
		System.out.println("容量为： " + buff.capacity());
		System.out.println("限制为： " + buff.limit());
		
		System.out.println("获取下标为1的元素：" + buff.get(1));
		System.out.println("get(index)方法：position位置不变：" + buff);
		buff.put(1, 22);
		System.out.println("put(index, change)方法，position位置不变：" + buff);
		
		for(int i = 0; i < buff.limit(); i++) {
			// 调用get方法会使缓冲区位置（position）向后递增一位
			System.out.println(buff.get() + "\t");
		}
		
		System.out.println("buff对象遍历之后：" + buff);
		
		//2. wrap方法的使用
		// wrap方法会包裹一个数组：一般这种用法不会先初始化缓存对象的长度，因为没有意义，最后还会被wrap所包裹的数组覆盖掉
		// 并且wrap方法修改缓冲区对象的时候，数组本身也会跟着发生变化。
		int [] arr = new int[]{1,2,5};
		IntBuffer buff1 = IntBuffer.wrap(arr);
		System.out.println(buff1);
		
		IntBuffer buff2 = IntBuffer.wrap(arr, 0, 2);
		// 这样使用表示容量为数组arr的长度，但是可操作的元素只有实际进入缓存区的元素长度
		System.out.println(buff2);
		
		//3. 其他方法
		IntBuffer buff3 = IntBuffer.allocate(10);
		int[] arr1 = new int[]{1,2,3};
		buff3.put(arr1);
		System.out.println(buff3);
		// 一种复制方法
		IntBuffer buff4 = buff3.duplicate();
		System.out.println(buff4);
		
		//设置buff3的位置属性
		buff3.position(0);
		buff3.flip();
		System.out.println(buff3);
		
		System.out.println("可读数据为：" + buff3.remaining());
	}
}
