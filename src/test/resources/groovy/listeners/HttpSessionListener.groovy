package listeners

import org.gservlet.annotation.SessionListener

@SessionListener
class HttpSessionListener {
	
	void sessionCreated() {
		session.setAttribute("state","sessionCreated")
	}
	
	void sessionDestroyed() {
		session.setAttribute("state","sessionDestroyed")
	}
	
}