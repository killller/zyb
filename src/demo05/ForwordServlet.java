package demo05;

public class ForwordServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		//请求转发其实跟Http协议无关
		RequestDispatcher rd = request.getRequestDispatcher("/index.html");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
}
