import org.gservlet.annotation.RequestListener

@RequestListener
class ServletRequestListener {
	
	void init() {
		println "request initialized"
	}
	
	void destroy() {
		println "request destroyed"
	}
}
