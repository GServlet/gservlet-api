package listeners

import org.gservlet.annotation.RequestListener

@RequestListener
class ServletRequestListener {
	
	void requestInitialized() {
		context.setAttribute("state","requestInitialized")
	}
	
	void requestDestroyed() {
		context.setAttribute("state","requestDestroyed")
	}
	
}