package demo05;

public class RequestDispatcher {
	private String webPath;
	
	public RequestDispatcher(String webPath){
		this.webPath = webPath;
	}
	
	public void forword(HttpServletRequest request, HttpServletResponse response){
		request.setRequestURL(webPath);
	}
}
