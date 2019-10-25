package demo01;

public class Test05_wait {
	public static void main(String[] args) throws InterruptedException {
		MyRunnable mr = new MyRunnable();
		
		for (int i = 0; i < 100; i++)
			new Thread(mr, "线程" + i).start();
		
		Thread.sleep(1100);
		//同步代码块
		synchronized (mr){
			//通知49线程继续运行，注意：之前执行的是mr的wait，所以这里也是执行mr的notify
			mr.notify();
		}
		
		System.out.println(mr.count);
		/*
		Thread t1 = new Thread(mr,"zyb");
		Thread t2 = new Thread(mr,"yc");
		Thread t3 = new Thread(mr,"lydia");
		Thread t4 = new Thread(mr,"kahuoqw");
		Thread t5 = new Thread(mr,"q");
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		*/
	}
}

class MyRunnable implements Runnable{
	int count=100;
	@Override
	/**
	 * synchronized 表示该代码只能被同时被一个线程所执行
	 */
	//同步方法
	public synchronized void run() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if ("线程49".equals(Thread.currentThread().getName())){
			System.out.println("===========线程49===============在此等待");
			try {
				wait();//是object方法
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(Thread.currentThread().getName() + " : " + count--);
	}
	
}

