import org.gservlet.annotation.Servlet

@Servlet("/servlet")
class HttpServlet {
	
	void get() {
		request.setAttribute("state","get")
	}
	
	void post() {
		request.setAttribute("state","post")
	}
	
	void put() {
		request.setAttribute("state","put")
	}
	
	void delete() {
		request.setAttribute("state","delete")
	}
	
	void options() {
		request.setAttribute("state","options")
	}
	
	void trace() {
		request.setAttribute("state","trace")
	}
	
	void head() {
		request.setAttribute("state","head")
	}
}