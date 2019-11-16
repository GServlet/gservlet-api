import org.gservlet.annotation.Dao

@Dao
class CustomerDao {
	
	def getCustomers() {
		def customers = []
		customers << [firstName : "Mamadou Lamine", lastName : "Sy"]
		customers << [firstName : "Moussa", lastName : "Fall"]
		customers << [firstName : "Demba", lastName : "Sy"]
		//customers << [firstName : "Bamba", lastName : "Fall"]
		return customers
	}
	
}