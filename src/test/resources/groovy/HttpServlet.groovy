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
@MultipartConfig( fileSizeThreshold = 1048576, maxFileSize = 5242880L, maxRequestSize = 26214400L )
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
	
	void patch() {
		throw new UnsupportedOperationException()
	}
}