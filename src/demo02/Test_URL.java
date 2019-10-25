package demo02;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Test_URL {
	public static void main(String[] args) throws IOException {
		//URl	统一资源定位器
		//格式
		// HTTP:	http://192.168.1.54:8080/Food/index.jsp
		
		//构建URl
		URL url = new URL("http://www.hyycinfo.com");
		//资源文件名
		System.out.println("url.getFile() = " + url.getFile());
		//主机地址
		System.out.println("url.getHost() = " + url.getHost());
		//协议
		System.out.println("url.getProtocol() = " + url.getProtocol());
		//端口
		System.out.println("url.getPort() = " + url.getPort());
		//查询参数
		System.out.println("url.getQuery() = "+ url.getQuery());
		
		//连接资源目标
		URLConnection conn = url.openConnection();
		//获取连接的输入流
		InputStream in = conn.getInputStream();
		//定义一个字符流 读取字节流
		InputStreamReader isr = new InputStreamReader(in, "UTF-8");
		
		//定义字节数组，作为缓冲
		//byte[] buffer = new byte[1024];
		char [] buffer = new char[1024];
		//定义读取的字节数
		int count;
		while( (count = isr.read(buffer)) > 0 ){
			String string = new String(buffer, 0, count);
			System.out.println(string);
		}
	}
}
