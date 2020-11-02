package listeners

import org.gservlet.annotation.SessionIdListener

@SessionIdListener
class HttpSessionIdListener {
	
	void sessionIdChanged() {
		session.setAttribute("state","sessionIdChanged")
	}
	
}