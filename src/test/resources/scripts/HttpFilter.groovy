import org.gservlet.annotation.Filter

@Filter("/*")
class HttpFilter {
	
	void filter() {
		request.setAttribute("state","filtering");
	}
	
}