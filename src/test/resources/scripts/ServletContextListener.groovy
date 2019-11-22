import org.gservlet.annotation.ContextListener

@ContextListener
class ServletContextListener {
	
	void contextInitialized() {
		context.setAttribute("state","contextInitialized");
	}
	
	void contextDestroyed() {
		context.setAttribute("state","contextDestroyed");
	}
	
}