import org.gservlet.annotation.RequestListener

@RequestListener
class ServletRequestListener {
	
	void requestInitialized() {
		println "request initialized"
	}
	
	void requestDestroyed() {
		println "request destroyed"
	}
}
