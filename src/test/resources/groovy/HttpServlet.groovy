import org.gservlet.annotation.Servlet
import org.gservlet.annotation.InitParam
import javax.servlet.annotation.MultipartConfig
import javax.servlet.annotation.ServletSecurity
import javax.servlet.annotation.ServletSecurity.TransportGuarantee
import javax.servlet.annotation.HttpConstraint
import javax.servlet.annotation.HttpMethodConstraint

@Servlet(value="/servlet", urlPatterns = ['/test/servlet'],
initParams = [
	@InitParam(name = "location", value = "D:/Uploads"),
	@InitParam(name = "maxUploadSize", value = "9900000")
])
@MultipartConfig( fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 25 )
@ServletSecurity(
 httpMethodConstraints = [
  @HttpMethodConstraint(value = "GET", rolesAllowed = "admin"),
  @HttpMethodConstraint(value = "POST", rolesAllowed = "admin"),      
 ]
)
class HttpServlet {
	
	void get() {
		request.setAttribute("state","get")
	}
	
	void post() {
		request.setAttribute("state","post")
	}
	
	void put() {
		request.setAttribute("state","put")
	}
	
	void delete() {
		request.setAttribute("state","delete")
	}
	
	void options() {
		request.setAttribute("state","options")
	}
	
	void trace() {
		request.setAttribute("state","trace")
	}
	
	void head() {
		request.setAttribute("state","head")
	}
}