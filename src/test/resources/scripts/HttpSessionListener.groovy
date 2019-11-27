import org.gservlet.annotation.SessionListener

@SessionListener
class ServletContextListener {
	
	void sessionCreated() {
		session.setAttribute("state","sessionCreated");
	}
	
	void sessionDestroyed() {
		session.setAttribute("state","sessionDestroyed");
	}
	
}