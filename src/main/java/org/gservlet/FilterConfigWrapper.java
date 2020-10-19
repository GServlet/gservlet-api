package org.gservlet;

import java.util.Enumeration;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * 
 * A wrapper class around the FilterConfig interface.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class FilterConfigWrapper implements FilterConfig {

	/**
	 * The filter config object
	 */
	private final FilterConfig filterConfig;

	/**
	 * 
	 * Constructs a FilterConfigWrapper for the given FilterConfig
	 * 
	 * @param filterConfig the filter config object
	 * 
	 */
	public FilterConfigWrapper(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * Returns the name of this filter instance.
	 *
	 * @return the name of the filter instance
	 * 
	 */
	@Override
	public String getFilterName() {
		return filterConfig.getFilterName();
	}

	/**
	 * Returns a reference to the ServletContext in which the caller is executing.
	 *
	 * @return a ServletContext object, used by the caller to interact with its servlet container
	 * 
	 * 
	 */
	@Override
	public ServletContext getServletContext() {
		return filterConfig.getServletContext();
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
		return filterConfig.getInitParameter(name);
	}

	/**
	 * Returns the names of the filter's initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the filter has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the filter's initialization parameters
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return filterConfig.getInitParameterNames();
	}

	/**
	 * 
	 * Sets an initialization parameter
	 * 
	 * @param name  the initialization parameter name
	 * @param value the initialization parameter value
	 * 
	 */
	public void propertyMissing(String name, Object value) {
	}

	/**
	 * 
	 * Gets the value of the initialization parameter with the given name.
	 * 
	 * @param name the name of the initialization parameter whose value to get
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if the initialization parameter does
	 *         not exist
	 */
	public Object propertyMissing(String name) {
		return filterConfig.getInitParameter(name);
	}

}