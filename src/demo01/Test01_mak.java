package demo01;

public class Test01_mak {
	
	public static void main(String[] args) throws InterruptedException {
		Thread mainThread = Thread.currentThread();
		mainThread.setName("zyb");
		System.out.println("线程ID(唯一)：" + mainThread.getId());
		System.out.println("线程名称：" + mainThread.getName());
		System.out.println("线程优先级(10最高，1最低，5默认)" + mainThread.getPriority());
		Thread.sleep(2000);//休眠	单位		毫秒
		System.out.println("线程是否是守护线程(精灵线程)：" + mainThread.isDaemon());
		System.out.println("线程是否处于活动状态：" + mainThread.isAlive());
		System.out.println("线程是否处于中断状态：" + mainThread.isInterrupted());
		
		/**
		 * 创建线程
		 * 方法一：继承Thread类
		 * 方法二：实现Runnable接口
		 */
		
		MyThread1 mt1 = new MyThread1();
		
		//在启动线程前设置好优先级
		mt1.setPriority(1);
		
		//运行此案城必须执行 start 方法
		mt1.start(); //第一个线程
		
		MyThread2 mt2 = new MyThread2();
		
		Thread t = new Thread(mt2, "多线程");
		
		t.start();
		
	}
	
}

class MyThread1 extends Thread{

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++)
			System.out.println(Thread.currentThread().getName() + ":" + i);
	}
}


class MyThread2 implements Runnable{
	@Override
	public void run() {
		for (int i = 0; i < 1000; i++)
			System.out.println(Thread.currentThread().getName() + ":" + i);
	}
}


class A{
	
}
