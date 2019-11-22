import org.gservlet.annotation.RequestListener

@RequestListener
class ServletRequestListener {
	
	void requestInitialized() {
		context.setAttribute("state1","requestInitialized");
	}
	
	void requestDestroyed() {
		context.setAttribute("state2","requestDestroyed");
	}
	
}