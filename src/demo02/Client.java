package demo02;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket server  = new Socket("172.20.80.54", 8888);
		Scanner scanner = new Scanner(System.in);
		System.out.println("成功连接服务器");
		InetAddress addr = server.getInetAddress();
		System.out.println("客户端的主机地址:" + addr.getHostAddress());
		System.out.println("客户端的IP地址" + Arrays.toString(addr.getAddress()));
		
		InputStream in = server.getInputStream();
		OutputStream out = server.getOutputStream();
		
		Thread t1 = new Thread(){
			public void run(){
				boolean running = true;
				while(running){
					System.out.print("我说:");
					String msg = scanner.nextLine();
					try {
						out.write(msg.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();
		
		Thread t2 = new Thread(){
			public void run(){
				boolean running = true;
				while(running){
					try {
						byte[] buf = new byte[1024];
						int count = in.read(buf);
						System.out.println();
						System.out.println("服务器说：" + new String(buf, 0, count));
						System.out.print("我说:");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t2.start();
		
		t1.join();
		t2.join();
		
		scanner.close();
		server.close();
		
	}
}
