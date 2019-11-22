import org.gservlet.annotation.SessionAttributeListener
import javax.servlet.http.HttpSessionBindingEvent;

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