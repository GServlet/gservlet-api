import org.gservlet.annotation.Filter

@Filter("/*")
class CORSFilter {
	
	void filter() {
		response.addHeader("Access-Control-Allow-Origin", "*")
		response.addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE")
		if (request.getMethod().equals("OPTIONS")) {
			response.status = response.SC_ACCEPTED
			return
		}
		next()
	}
}
