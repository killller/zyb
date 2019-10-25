package demo05;

import java.io.PrintWriter;

import demo05.HttpServletRequest;

public class HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		
		PrintWriter pw = response.getWriter();
		
		pw.print("<h1>Hello world</h1>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response){
	
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response){
		switch (request.getMethod()) {
		case "GET":
			doGet(request, response);
			break;
		case "POST":
			doPost(request, response);
			break;
		case "PUT":
			doPut(request, response);
			break;
		}
	}
}
