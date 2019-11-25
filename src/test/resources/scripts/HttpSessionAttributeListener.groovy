import org.gservlet.annotation.SessionAttributeListener

@SessionAttributeListener
class HttpSessionAttributeListener {
	
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