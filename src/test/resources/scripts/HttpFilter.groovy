import org.gservlet.annotation.Filter
import javax.servlet.annotation.WebInitParam

@Filter(value="/*", urlPatterns = ['/test/*'],
initParams = [
	@WebInitParam(name = "location", value = "D:/Uploads"),
	@WebInitParam(name = "maxUploadSize", value = "9900000")
])
class HttpFilter {
	
	void init() {
		request.setAttribute("state","init")
	}
	
	void filter() {
		request.setAttribute("state","filtering")
	}
	
	void destroy() {
		request.setAttribute("state","destroy")
	}
	
}