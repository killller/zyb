package demo04;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Processer {
	public void Process(Socket socket){
		InputStream in;
		OutputStream out;
		
		try {
			in = socket.getInputStream();
			out  = socket.getOutputStream();
			//读取请求报文内容
			byte[] buf = new byte[1024];
			int count;
			count = in.read(buf);
			String content = new String(buf, 0, count);
			System.out.println(content);
			//解析请求报文（暂未实现）
			HttpServletRequest request = ParseRequest(content);
			/**
			 *	/index.jsp
			 *	D:/JavaWeb/movies/WebContent/index.jsp
			 * 	
			 * 	
			 * 
			 */
			
			String suffix = request.getRequestURL().substring(request.getRequestURL().lastIndexOf(".") + 1);
			
			String contentType;
			switch(suffix){
			case "js":
				contentType = "application/x-javascript"; break;
			case "css":
				contentType = "text/css"; break;
			case "jpg":
				contentType = "image/jpeg"; break;
			case "bmp":
				contentType = "image/bmp"; break;
			case "png":
				contentType = "image/png"; break;
			case "gif":
				contentType = "image/gif"; break;
			default:
				contentType = "text/html";
			}
			
			//定义响应报文内容
			String resp = "HTTP/1.1 200 OK\r\n";
			resp += "Content-Type: "+contentType+"\r\n";
			resp += "\r\n";//CRLF 空行
			out.write(resp.getBytes());
			String rootPath = "D:/apache-tomcat-8.0.51/webapps/photo";
			String filePath = request.getRequestURL();
			//判断访问文件是否存在
			String diskPath = rootPath + filePath;
			if (new File(diskPath).exists() == false){
				diskPath = rootPath + "/404.html";
			}
			FileInputStream fis = new FileInputStream(diskPath);
			
			//向浏览器发送报文
			while ((count = fis.read(buf)) > 0){
				out.write(buf, 0, count);
			}
			fis.close();
			//如果访问的文件不存在，则返回404.html
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//解析请求报文
		//给予对应的相应
		
	}
	
	public HttpServletRequest ParseRequest(String content){
		HttpServletRequest request = new HttpServletRequest(content);
		return request;
	}
}
