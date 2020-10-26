import org.gservlet.annotation.Servlet

@Servlet(value="/projects", loadOnStartup = 1)
class ProjectServlet {

	def projects = []

	void init() {
		projects << [id : 1, name : "Groovy", url : "https://groovy-lang.org"]
		projects << [id : 2, name : "Spring", url : "https://spring.io"]
		projects << [id : 3, name : "Maven", url : "https://maven.apache.org"]
	}

	void get() {
		json(projects)
	}

	void post() {
		def project = request.body
		projects << project
		json(project)
	}

	void put() {
		def project = request.body
		int index = projects.findIndexOf { it.id == project.id }
		projects[index] = project
		json(project)
	}

	void delete() {
		def project = request.body
		int index = projects.findIndexOf { it.id == project.id }
		json(projects.remove(index))
	}
	
}