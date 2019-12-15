import org.gservlet.annotation.Filter
import org.gservlet.annotation.InitParam

@Filter(value="/*", urlPatterns = ['/test/*'],
initParams = [
	@InitParam(name = "location", value = "D:/Uploads"),
	@InitParam(name = "maxUploadSize", value = "9900000")
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