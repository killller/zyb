package demo01;

public class Test04_Join {
	public static void main(String[] args) {
		Thread t = new Thread("zyb"){
			public void run(){
				for (int i = 0; i < 50; i++){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + ":" + i);
				}
			}
		};
		
		Thread t1 = new Thread("yc"){
			public void run(){
				for (int i = 0; i < 50; i++){
					if (i == 25){
						//执行join在此阻塞，等待t进程的运行结束
						try {
							t.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + ":" + i);
				}
			}
		};
		t.start();
		t1.start();
		
	}
}
