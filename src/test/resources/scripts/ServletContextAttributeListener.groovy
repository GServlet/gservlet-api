import org.gservlet.annotation.ContextAttributeListener

@ContextAttributeListener
class ServletContextAttributeListener {
	
	void attributeAdded() {
		event.name = "attributeAdded"
	}
	
	void attributeRemoved() {
		event.name = "attributeRemoved"
	}
	
	void attributeReplaced() {
		event.name = "attributeReplaced"
	}
	
}