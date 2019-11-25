import org.gservlet.annotation.RequestAttributeListener

@RequestAttributeListener
class ServletRequestAttributeListener {
	
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