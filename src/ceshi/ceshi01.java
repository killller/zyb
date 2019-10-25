package ceshi;

public class ceshi01 {
	public static void main(String[] args) {
		final StringBuffer s1 = new StringBuffer();
		final StringBuffer s2 = new StringBuffer();
		new Thread() {
			// 线程1
			public void run() {
				synchronized (s1) {
					s2.append("A");
					// point1
					synchronized (s2) {
						s2.append("B");
						System.out.println(s1);
						System.out.println(s2);
					}
				}
			}
		}.start();
		//nihao 
		new Thread() {
			// 线程2
			public void run() {
				synchronized (s2) {
					s2.append("C");
					synchronized (s1) {
						// point2
						s2.append("D");
						System.out.println(s2);
						System.out.println(s1);
					}
				}
			}
		}.start();

	}
}