package demo05;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import demo05.HttpServletRequest;

public class Processer {
	
	//定义Servlet容器
	private HashMap<String, HttpServlet> servletContainer = new HashMap<>();
	{
		servletContainer.put("/redirect.s", new RedirectServlet());
		//添加一个servlet实现请求转发
		servletContainer.put("/forword.s", new ForwordServlet());
		//添加一个Servlet
		servletContainer.put("/hello.s", new RedirectServlet());
		
		servletContainer.put("/cookie.s", new CookieServlet());
		
		servletContainer.put("/user/getcookie.s", new GetCookieServlet());
	}
	
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
			HttpServletResponse response = new HttpServletResponse(request, out);
			/**
			 *	静态请求：对应着一个html,js,css...
			 * 	动态请求：hello.s
			 * 	非法404请求	即没有物理间文件也没有虚拟的地址
			 */
			//判断物理文件是否存在
			String rootPath = "/apache-tomcat-8.0.51/webapps/photo";
			String webPath = request.getRequestURL();
			//判断访问文件是否存在
			String diskPath = rootPath + webPath;
			if (new File(diskPath).exists() == true){
				//静态请求直接commit
			}else if (servletContainer.containsKey(webPath)){
				//判断虚拟路径中有没有该地址（Servlet容器中去找）
				//动态请求先由servlet处理
				HttpServlet servlet = servletContainer.get(webPath);
				servlet.service(request, response);
			}else {
				//404 改写资源文件为404.html
				response.setStatus(404, "Not Found");
				request.setRequestURL("/404.html");
			}
			
			response.commit();
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
