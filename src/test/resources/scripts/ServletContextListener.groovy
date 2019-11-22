import org.gservlet.annotation.ContextListener

@ContextListener
class ServletContextListener {
	
	void contextInitialized() {
		context.setAttribute("state1","contextInitialized");
	}
	
	void contextDestroyed() {
		context.setAttribute("state2","contextDestroyed");
	}
	
}