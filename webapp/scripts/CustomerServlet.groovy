import org.gservlet.annotation.Dao
import org.gservlet.annotation.Servlet

@Servlet("/customers")
class CustomerServlet {
	
	CustomerDao dao
	
	void get() {
		json(dao.customers)
	}
	
	
}