import org.gservlet.annotation.Filter

@Filter("/*")
class HttpFilter {
	
	void init() {
		request.setAttribute("state","init");
	}
	
	void filter() {
		request.setAttribute("state","filtering");
	}
	
	void destroy() {
		request.setAttribute("state","destroy");
	}
	
}