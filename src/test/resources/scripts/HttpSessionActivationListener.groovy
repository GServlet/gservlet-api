import org.gservlet.annotation.SessionActivationListener

@SessionActivationListener
class HttpSessionActivationListener {
	
	void sessionDidActivate() {
		session.setAttribute("state","sessionDidActivate")
	}
	
	void sessionWillPassivate() {
		session.setAttribute("state","sessionWillPassivate")
	}
	
}