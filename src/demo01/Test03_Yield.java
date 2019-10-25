package demo01;

public class Test03_Yield {
	public static void main(String[] args) {
		new Thread("zyb"){
			public void run(){
				long start = System.currentTimeMillis();
				for (int i = 0; i < 100000; i++)
					Math.random();
				long time = System.currentTimeMillis() - start;
				System.out.println(Thread.currentThread().getName() + ":" + time);
			}
		}.start();
		
		new Thread("多线程"){
			public void run(){
				long start = System.currentTimeMillis();
				for (int i = 0; i < 100000; i++){
					Math.random();
					//执行让渡
					Thread.yield();
				}
				long time = System.currentTimeMillis() - start;
				System.out.println(Thread.currentThread().getName() + ":" + time);
			}
		}.start();
	}
}
	