import org.gservlet.annotation.Servlet

@Servlet("/customers")
class CustomerServlet {
	
	void get() {
		def customers = []
		customers << [firstName : "Mamadou Lamine", lastName : "Sy"]
		customers << [firstName : "Moussa", lastName : "Fall"]
		customers << [firstName : "Demba", lastName : "Sy"]
		customers << [firstName : "Bamba", lastName : "Fall"]
		json(customers)
	}
	
}