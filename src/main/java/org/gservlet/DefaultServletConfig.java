package org.gservlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
* 
* A servlet configuration object used by a servlet container to pass information to a servlet during initialization..   
* 
* @author Mamadou Lamine Ba
* 
*/
public class DefaultServletConfig implements ServletConfig {

	/**
	 * The initialization parameters Map
	 */
	private final Map<String, String> parameters = new HashMap<>();

	/**
	 * The servlet context
	 */
	private ServletContext servletContext;
	
	/**
	 * Returns the name of this servlet instance. The name may be provided via
	 * server administration, assigned in the web application deployment descriptor,
	 * or for an unregistered (and thus unnamed) servlet instance it will be the
	 * servlet's class name.
	 *
	 * @return the name of the servlet instance
	 */
	@Override
	public String getServletName() {
		return null;
	}

	/**
	 * Returns a reference to the ServletContext in which the caller is executing.
	 *
	 * @return a ServletContext object, used by the caller to interact with its servlet container
	 * 
	 * @see ServletContext
	 */
	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	/**
	 * 
	 * Sets the servlet context
	 * 
	 * @param servletContext  the servlet context
	 * 
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}


	/**
	 * 
	 * Sets an initialization parameter
	 * 
	 * @param name  the initialization parameter name
	 * @param value the initialization parameter value
	 * 
	 */
	public void addInitParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	/**
	 * Gets the value of the initialization parameter with the given name.
	 *
	 * @param name the name of the initialization parameter whose value to get
	 *
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if the initialization parameter does
	 *         not exist
	 */
	@Override
	public String getInitParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Returns the names of the servlet's initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the servlet has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the servlet's initialization parameters
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

}