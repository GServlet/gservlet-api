import org.gservlet.annotation.Filter
import org.gservlet.annotation.InitParam
import org.gservlet.AbstractFilter

@Filter(value="/*", urlPatterns = ['/test/*'],
initParams = [
	@InitParam(name = "location", value = "D:/Uploads"),
	@InitParam(name = "maxUploadSize", value = "9900000")
])
class HttpFilter extends AbstractFilter {
	
	void init() {
		super.init()
		if(requestContext) {
			request.setAttribute("state","init")
		}
	}
	
	void filter() {
		super.filter()
		if(requestContext) {
			request.setAttribute("state","filtering")
		}
	}
	
	void destroy() {
		super.destroy()
		if(requestContext) {
			request.setAttribute("state","destroy")
		}
	}
	
}