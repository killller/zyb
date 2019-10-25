package demo05;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Tomcat {
	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.startup();
	}
	
	//启动命令
	public void startup()throws Exception{
		ServerSocket server = new ServerSocket(8080);
		boolean running = true;
		while (running){
			Socket socket = server.accept();
			new Thread(){
				public void run(){
					new Processer().Process(socket);
				}
			}.start();
		}
		server.close();
	}
	
	//关闭服务器
	public void shutdown(){
		
	}
}
