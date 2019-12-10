import org.gservlet.annotation.SessionBindingListener

@SessionBindingListener
class HttpSessionBindingListener {
	
	void valueBound() {
		event.name = "valueBound"
	}
	
	void valueUnbound() {
		event.name = "valueUnbound"
	}
	
}