package demo05;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import demo05.HttpServletRequest;

public class HttpServletResponse {
	//web.xml解析器
	private static WebXmlParser1 webXmlParser = new WebXmlParser1("web.xml");
	private HttpServletRequest request;
	private OutputStream out;
	
	private int status = 200;
	private String message = "OK";
	private HashMap<String, String> headerMap = new HashMap<>();
	public HttpServletResponse(HttpServletRequest request, OutputStream out){
		super();
		this.request = request;
		this.out = out;
	}
	

	//提交方法
	public void commit() throws IOException{
		String suffix = request.getRequestURL().substring(request.getRequestURL().lastIndexOf(".") + 1);
		
		//从web.xml文件中取contentType 替代之前的硬编码判断
		
		//设置响应类型
		
		//判断有设置content-Type，替代之前的硬编码判断
		if (headerMap.containsKey("Content-Type") == false){
			//设置响应类型
			String contentType = webXmlParser.getContentType(suffix);
			setContentType(contentType);
		}
		
		String resp = "HTTP/1.1 " + status + " " + message + "\r\n";
		//写头域信息
		for (Entry<String, String> entry : headerMap.entrySet())
			resp += entry.getKey() + ": " + entry.getValue() + "\r\n";
		//写入Cookie数据
		for (Cookie cookie : cookies)
			resp += cookie + "\r\n";
		resp += "\r\n";
		out.write(resp.getBytes());
		
		//响应重定向不需要写body
		if (status < 300 || status > 399){
			if(caw.toString().isEmpty()){
				String rootPath = "D:/apache-tomcat-8.0.51/webapps/photo";
				String filePath = request.getRequestURL();
				//判断访问文件是否存在
				String diskPath = rootPath + filePath;
				if (new File(diskPath).exists() == false){
					diskPath = rootPath + "/404.html";
				}
				FileInputStream fis = new FileInputStream(diskPath);
				
				byte[] buf = new byte[1024];
				int count;
				//向浏览器发送报文
				while ((count = fis.read(buf)) > 0){
					out.write(buf, 0, count);
				}
				fis.close();
			} else {
				out.write(caw.toString().getBytes());
			}
		}
	}
	
	void setContentType(String contentType) {
		this.headerMap.put("Content-Type", contentType);
	}


	public void setStatus (int status, String message){
		this.status = status;
		this.message = message;
	}
	
	/**
	 * 响应重定向
	 * @param webPath
	 */
	public void sendRedirect(String webPath){
		/**
		 * 响应结果码
		 * 1xx 接受请求，继续处理
		 * 2xx 正常响应 200
		 * 3xx 响应重定向 301 302
		 * 4xx 浏览器端错误 404 405
		 * 5xx 服务器端错误
		 */
		
		this.setStatus(301, "Redirect");
		this.addHeader("Location", webPath);
	}


	private void addHeader(String key, String value) {
		this.headerMap.put(key, value);
	}

	/*
	 * 如何定义  PrintWriter， 在commit()要考虑和文件输出的配合问题
	 */
	CharArrayWriter caw = new CharArrayWriter();
	PrintWriter pw = new PrintWriter(caw);
	public PrintWriter getWriter() {
		return pw;
	}

	private List<Cookie> cookies = new ArrayList<Cookie>();
	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
	}
}
