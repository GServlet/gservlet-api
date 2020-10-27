/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.gservlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
* 
* A wrapper class around the ServletContext class
* 
* @author Mamadou Lamine Ba
* 
*/
public class ContextWrapper implements ServletContext {

	/**
	 * The servlet context object
	 */
	protected final ServletContext context;

	/**
	* 
	* Constructs a ContextWrapper for the given ServletContext
	* 
	* @param context the ServletContext object 
	*  
	*/
	public ContextWrapper(ServletContext context) {
		this.context = context;
	}

	/**
     * Adds the given filter instance with the given name and class name to this ServletContext.
     * The registered filter may be further configured via the returned
     * {@link javax.servlet.FilterRegistration} object
     *
     *
     * @param filterName the name of the filter
     * @param className the fully qualified class name of the filter
     *
     * @return a {@link javax.servlet.FilterRegistration} object that may be used to further
     * configure the registered filter, or <tt>null</tt> if this
     * ServletContext already contains a complete {@link javax.servlet.FilterRegistration} for
     * a filter with the given <tt>filterName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Dynamic addFilter(String filterName, String className) {
		return context.addFilter(filterName, className);
	}

	/**
     * Adds the given filter instance with this ServletContext
     * under the given <tt>filterName</tt>. The registered filter may be further configured via the returned
     * {@link javax.servlet.FilterRegistration} object.
     *
     *
     * @param filterName the name of the filter
     * @param filter the filter instance to register
     *
     * @return a FilterRegistration object that may be used to further
     * configure the given filter, or <tt>null</tt> if this
     * ServletContext already contains a complete FilterRegistration for a
     * filter with the given <tt>filterName</tt> or if the same filter
     * instance has already been registered with this or another
     * ServletContext in the same container
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Dynamic addFilter(String filterName, Filter filter) {
		return context.addFilter(filterName, filter);
	}

	/**
     * Adds the filter with the given name and class type to this servlet
     * context. The registered filter may be further configured via the returned
     * {@link javax.servlet.FilterRegistration} object.
     *
     * @param filterName the name of the filter
     * @param filterClass the class object from which the filter will be
     * instantiated
     *
     * @return a {@link javax.servlet.FilterRegistration} object that may be used to further
     * configure the registered filter, or <tt>null</tt> if this
     * ServletContext already contains a complete {@link javax.servlet.FilterRegistration} for a
     * filter with the given <tt>filterName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		return context.addFilter(filterName, filterClass);
	}

	/**
     * Adds the listener with the given class name to this ServletContext.
     * 
     * @param className the fully qualified class name of the listener
     *
     * @throws IllegalArgumentException if the class with the given name
     * does not implement any of the above interfaces, or if it implements
     * {@link javax.servlet.ServletContextListener} and this ServletContext was not
     * passed to {@link javax.servlet.ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public void addListener(String className) {
		context.addListener(className);
	}

	/**
     * Adds the given listener to this ServletContext.
     * 
     * @param <T> the class of the EventListener to add
     * @param t the listener to be added
     *
     * @throws IllegalArgumentException if the given listener is not
     * an instance of any of the above interfaces, or if it is an instance
     * of {@link javax.servlet.ServletContextListener} and this ServletContext was not
     * passed to {@link javax.servlet.ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public <T extends EventListener> void addListener(T t) {
		context.addListener(t);
	}

	/**
     * Adds a listener of the given class type to this ServletContext.
     *
     * @param listenerClass the listener class to be instantiated
     *
     * @throws IllegalArgumentException if the given <tt>listenerClass</tt>
     * does not implement any of the above interfaces, or if it implements
     * {@link javax.servlet.ServletContextListener} and this ServletContext was not passed
     * to {@link javax.servlet.ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
		context.addListener(listenerClass);
	}

	/**
     * Adds the servlet with the given name and class name to this servlet
     * context.
     *
     * @param servletName the name of the servlet
     * @param className the fully qualified class name of the servlet
     *
     * @return a ServletRegistration object that may be used to further
     * configure the registered servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for
     * a servlet with the given <tt>servletName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>servletName</code> is null
     * or an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName, String className) {
		return context.addServlet(servletName, className);
	}

	 /**
     * Adds the given servlet instance with this ServletContext
     * under the given <tt>servletName</tt>.
     *
     *
     * @param servletName the name of the servlet
     * @param servlet the servlet instance to register
     *
     * @return a ServletRegistration object that may be used to further
     * configure the given servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for a
     * servlet with the given <tt>servletName</tt> or if the same servlet
     * instance has already been registered with this or another
     * ServletContext in the same container
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if the given servlet instance
     * implements {@link javax.servlet.SingleThreadModel}, or <code>servletName</code> is null
     * or an empty String
     *
     * @since Servlet 3.0
     */
	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
		return context.addServlet(servletName, servlet);
	}

	 /**
     * Adds the servlet with the given name and class type to this servlet
     * context.
     *
     * @param servletName the name of the servlet
     * @param servletClass the class object from which the servlet will be
     * instantiated
     *
     * @return a ServletRegistration object that may be used to further
     * configure the registered servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for
     * the given <tt>servletName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>servletName</code> is null
     * or an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return context.addServlet(servletName, servletClass);
	}

	/**
     * Instantiates the given Filter class.
     *
     * @param <T> the class of the Filter to create
     * @param clazz the Filter class to instantiate
     *
     * @return the new Filter instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		return context.createFilter(clazz);
	}

	/**
     * Instantiates the given EventListener class.
     *
     *
     * @param <T> the class of the EventListener to create
     * @param clazz the EventListener class to instantiate
     *
     * @return the new EventListener instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if the specified EventListener class
     * does not implement any of the
     * {@link javax.servlet.ServletContextListener},
     * {@link javax.servlet.ServletContextAttributeListener},
     * {@link javax.servlet.ServletRequestListener},
     * {@link javax.servlet.ServletRequestAttributeListener},
     * {@link javax.servlet.http.HttpSessionAttributeListener},
     * {@link javax.servlet.http.HttpSessionListener}
     * interfaces.
     *
     * @since Servlet 3.0
     */
	@Override
	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		return context.createListener(clazz);
	}

	/**
     * Instantiates the given Servlet class.
     *
     * @param <T> the class of the Servlet to create
     * @param clazz the Servlet class to instantiate
     *
     * @return the new Servlet instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		return context.createServlet(clazz);
	}

	/**
     * Declares role names that are tested using <code>isUserInRole</code>.
     *
     * <p>Roles that are implicitly declared as a result of their use within
     * the {@link ServletRegistration.Dynamic#setServletSecurity
     * setServletSecurity} or {@link ServletRegistration.Dynamic#setRunAsRole
     * setRunAsRole} methods of the {@link ServletRegistration} interface need
     * not be declared.
     *
     * @param roleNames the role names being declared
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if any of the argument roleNames is
     * null or the empty string
     *
     * @throws IllegalStateException if the ServletContext has already
     * been initialized
     *
     * @since Servlet 3.0
     */
	@Override
	public void declareRoles(String... roleNames) {
		context.declareRoles(roleNames);
	}

	/**
	 * Returns the servlet container attribute with the given name, or
	 * <code>null</code> if there is no attribute by that name.
	 *
	 *
	 * @param name a <code>String</code> specifying the name of the attribute
	 *
	 * @return an <code>Object</code> containing the value of the attribute, or
	 *         <code>null</code> if no attribute exists matching the given name.
	 *
	 * @see ServletContext#getAttributeNames
	 *
	 * @throws NullPointerException if the argument {@code name} is {@code null}
	 *
	 */
	@Override
	public Object getAttribute(String name) {
		return context.getAttribute(name);
	}

	/**
	 * Returns an <code>Enumeration</code> containing the attribute names available
	 * within this ServletContext.
	 *
	 * <p>
	 * Use the {@link #getAttribute} method with an attribute name to get the value
	 * of an attribute.
	 *
	 * @return an <code>Enumeration</code> of attribute names
	 *
	 * @see #getAttribute
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		return context.getAttributeNames();
	}

	/**
     * Gets the class loader of the web application represented by this
     * ServletContext
     *
     * @return the class loader of the web application represented by this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws SecurityException if a security manager denies access to
     * the requested class loader
     *
     * @since Servlet 3.0
     */
	@Override
	public ClassLoader getClassLoader() {
		return context.getClassLoader();
	}

	/**
	 * Returns a <code>ServletContext</code> object that corresponds to a specified
	 * URL on the server
	 *
	 *
	 * @param uripath a <code>String</code> specifying the context path of another
	 *                web application in the container.
	 * @return the <code>ServletContext</code> object that corresponds to the named
	 *         URL, or null if either none exists or the container wishes to
	 *         restrict this access.
	 *
	 * @see RequestDispatcher
	 */
	@Override
	public ServletContext getContext(String uripath) {
		return context.getContext(uripath);
	}

	/**
     * Returns the context path of the web application.
     *
     *
     * @return The context path of the web application, or "" for the
     * root context
     *
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     *
     * @since Servlet 2.5
     */
	@Override
	public String getContextPath() {
		return context.getContextPath();
	}

	 /**
     * Gets the session tracking modes that are supported by default for this
     * <tt>ServletContext</tt>.
     *
     * @return set of the session tracking modes supported by default for
     * this <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return context.getDefaultSessionTrackingModes();
	}

	 /**
     * Gets the major version of the Servlet specification that the
     * application represented by this ServletContext is based on.
     *
     *
     * @return the major version of the Servlet specification that the
     * application represented by this ServletContext is based on
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public int getEffectiveMajorVersion() {
		return context.getEffectiveMajorVersion();
	}

	/**
     * Gets the minor version of the Servlet specification that the
     * application represented by this ServletContext is based on.
     *
     *
     * @return the minor version of the Servlet specification that the
     * application represented by this ServletContext is based on
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public int getEffectiveMinorVersion() {
		return context.getEffectiveMinorVersion();
	}

	/**
     * Gets the session tracking modes that are in effect for this
     * <tt>ServletContext</tt>.
     *
     * @return set of the session tracking modes in effect for this
     * <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return context.getEffectiveSessionTrackingModes();
	}

	/**
     * Gets the FilterRegistration corresponding to the filter with the
     * given <tt>filterName</tt>.
     *
     * @param filterName the name of a filter
     * @return the (complete or preliminary) FilterRegistration for the
     * filter with the given <tt>filterName</tt>, or null if no
     * FilterRegistration exists under that name
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return context.getFilterRegistration(filterName);
	}

	 /**
     * Gets a (possibly empty) Map of the FilterRegistration
     * objects (keyed by filter name) corresponding to all filters
     * registered with this ServletContext.
     *
     *
     * @return Map of the (complete and preliminary) FilterRegistration
     * objects corresponding to all filters currently registered with this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return context.getFilterRegistrations();
	}

	/**
     * Returns a <code>String</code> containing the value of the named
     * context-wide initialization parameter, or <code>null</code> if
     * the parameter does not exist.
     *
     *
     * @param	name	a <code>String</code> containing the name of the
     *                  parameter whose value is requested
     *
     * @return a <code>String</code> containing the value of the
     * context's initialization parameter, or <code>null</code> if the
     * context's initialization parameter does not exist.
     *
     * @throws NullPointerException if the argument {@code name} is
     * {@code null}
     *
     * @see javax.servlet.ServletConfig#getInitParameter
     */
	@Override
	public String getInitParameter(String name) {
		return context.getInitParameter(name);
	}

	/**
	 * Returns the names of the context's initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the context has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the context's initialization parameters
	 *
	 * @see javax.servlet.ServletConfig#getInitParameter
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return context.getInitParameterNames();
	}

	/**
     * Gets the <code>&lt;jsp-config&gt;</code> related configuration
     * that was aggregated from the <code>web.xml</code> and
     * <code>web-fragment.xml</code> descriptor files of the web application
     * represented by this ServletContext.
     *
     * @return the <code>&lt;jsp-config&gt;</code> related configuration
     * that was aggregated from the <code>web.xml</code> and
     * <code>web-fragment.xml</code> descriptor files of the web application
     * represented by this ServletContext, or null if no such configuration
     * exists
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @see javax.servlet.descriptor.JspConfigDescriptor
     *
     * @since Servlet 3.0
     */
	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return context.getJspConfigDescriptor();
	}

	 /**
     * Returns the major version of the Servlet API that this
     * servlet container supports.
     *
     * @return the major version
     */
	@Override
	public int getMajorVersion() {
		return context.getMajorVersion();
	}

	
	/**
     * Returns the MIME type of the specified file, or <code>null</code> if
     * the MIME type is not known. The MIME type is determined
     * by the configuration of the servlet container, and may be specified
     * in a web application deployment descriptor. Common MIME
     * types include <code>text/html</code> and <code>image/gif</code>.
     *
     * @param file a <code>String</code> specifying the name of a file
     *
     * @return a <code>String</code> specifying the file's MIME type
     */
	@Override
	public String getMimeType(String file) {
		return context.getMimeType(file);
	}

	 /**
     * Returns the minor version of the Servlet API that this
     * servlet container supports.
     *
     * @return the minor version
     */
	@Override
	public int getMinorVersion() {
		return context.getMinorVersion();
	}

	/**
	 * Returns a {@link RequestDispatcher} object that acts as a wrapper for the
	 * named servlet
	 *
	 * @param name a <code>String</code> specifying the name of a servlet to wrap
	 *
	 * @return a <code>RequestDispatcher</code> object that acts as a wrapper for
	 *         the named servlet, or <code>null</code> if the
	 *         <code>ServletContext</code> cannot return a
	 *         <code>RequestDispatcher</code>
	 *
	 * @see RequestDispatcher
	 * @see ServletContext#getContext
	 * @see javax.servlet.ServletConfig#getServletName
	 */
	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return context.getNamedDispatcher(name);
	}

	/**
     * Gets the <i>real</i> path corresponding to the given
     * <i>virtual</i> path
     *
     * @param path the <i>virtual</i> path to be translated to a
     * <i>real</i> path
     *
     * @return the <i>real</i> path, or <tt>null</tt> if the
     * translation cannot be performed
     */
	@Override
	public String getRealPath(String path) {
		return context.getRealPath(path);
	}

	/**
	 *
	 * Returns a {@link RequestDispatcher} object that acts as a wrapper for the
	 * resource located at the given path. A <code>RequestDispatcher</code> object
	 * can be used to forward a request to the resource or to include the resource
	 * in a response. The resource can be dynamic or static.
	 *
	 * @param path a <code>String</code> specifying the pathname to the resource
	 *
	 * @return a <code>RequestDispatcher</code> object that acts as a wrapper for
	 *         the resource at the specified path, or <code>null</code> if the
	 *         <code>ServletContext</code> cannot return a
	 *         <code>RequestDispatcher</code>
	 *
	 * @see RequestDispatcher
	 * @see ServletContext#getContext
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return context.getRequestDispatcher(path);
	}

	/**
     * Returns a directory-like listing of all the paths to resources
     * within the web application whose longest sub-path matches the
     * supplied path argument.
     *
     * @param path the partial path used to match the resources,
     * which must start with a <tt>/</tt>
     * @return a Set containing the directory listing, or null if there
     * are no resources in the web application whose path
     * begins with the supplied path.
     *
     * @since Servlet 2.3
     */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		return context.getResource(path);
	}

	/**
	 * Returns the resource located at the named path as an <code>InputStream</code>
	 * object.
	 *
	 * @param path a <code>String</code> specifying the path to the resource
	 *
	 * @return the <code>InputStream</code> returned to the servlet, or
	 *         <code>null</code> if no resource exists at the specified path
	 */
	@Override
	public InputStream getResourceAsStream(String path) {
		return context.getResourceAsStream(path);
	}

	/**
     * Returns a directory-like listing of all the paths to resources
     * within the web application whose longest sub-path matches the
     * supplied path argument.
     *
     * @param path the partial path used to match the resources,
     * which must start with a <tt>/</tt>
     * @return a Set containing the directory listing, or null if there
     * are no resources in the web application whose path
     * begins with the supplied path.
     *
     * @since Servlet 2.3
     */
	@Override
	public Set<String> getResourcePaths(String path) {
		return context.getResourcePaths(path);
	}

	/**
	 * Returns the name and version of the servlet container on which the servlet is
	 * running.
	 *
	 *
	 * @return a <code>String</code> containing at least the servlet container name
	 *         and version number
	 */
	@Override
	public String getServerInfo() {
		return context.getServerInfo();
	}

	/**
     * @deprecated	As of Java Servlet API 2.1, with no direct replacement.
     *
     * This method was originally defined to retrieve a servlet
     * from a <code>ServletContext</code>.
     * 
     * @param name the servlet name
     * @return the {@code javax.servlet.Servlet Servlet} with the given name
     * @throws ServletException if an exception has occurred that interfaces
     * with servlet's normal operation
     */
	@Deprecated
	public Servlet getServlet(String name) throws ServletException {
		return context.getServlet(name);
	}

	/**
     * Returns the name of this web application corresponding to this
     * ServletContext as specified in the deployment descriptor for this
     * web application by the display-name element.
     *
     * @return The name of the web application or null if no name has been
     * declared in the deployment descriptor.
     *
     * @since Servlet 2.3
     */
	@Override
	public String getServletContextName() {
		return context.getServletContextName();
	}

	/**
     * @deprecated	As of Java Servlet API 2.1, with no replacement.
     *
     * This method was originally defined to return an
     * <code>Enumeration</code>
     * of all the servlet names known to this context.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet} names
     */
	@Deprecated
	public Enumeration<String> getServletNames() {
		return context.getServletNames();
	}

	 /**
     * Gets the ServletRegistration corresponding to the servlet with the
     * given <tt>servletName</tt>.
     *
     * @return the (complete or preliminary) ServletRegistration for the
     * servlet with the given <tt>servletName</tt>, or null if no
     * ServletRegistration exists under that name
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @param servletName the name of a servlet
     * @since Servlet 3.0
     */
	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return context.getServletRegistration(servletName);
	}

	/**
     * Gets a (possibly empty) Map of the ServletRegistration
     * objects (keyed by servlet name) corresponding to all servlets
     * registered with this ServletContext.
     *
     * @return Map of the (complete and preliminary) ServletRegistration
     * objects corresponding to all servlets currently registered with this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return context.getServletRegistrations();
	}

	/**
     * @deprecated	As of Java Servlet API 2.0, with no replacement.
     *
     * This method was originally defined to return an
     * <code>Enumeration</code> of all the servlets known to this servlet
     * context.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet}
     */
	@Deprecated
	public Enumeration<Servlet> getServlets() {
		return context.getServlets();
	}

	/**
     * Gets the {@link SessionCookieConfig} object through which various
     * properties of the session tracking cookies created on behalf of this
     * <tt>ServletContext</tt> may be configured.
     *
     * <p>Repeated invocations of this method will return the same
     * <tt>SessionCookieConfig</tt> instance.
     *
     * @return the <tt>SessionCookieConfig</tt> object through which
     * various properties of the session tracking cookies created on
     * behalf of this <tt>ServletContext</tt> may be configured
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return context.getSessionCookieConfig();
	}

	/**
	 *
	 * Writes the specified message to a servlet log file, usually an event log. The
	 * name and type of the servlet log file is specific to the servlet container.
	 *
	 * @param msg a <code>String</code> specifying the message to be written to the log file
	 * 
	 */
	@Override
	public void log(String msg) {
		context.log(msg);
	}

	/**
     * @deprecated	As of Java Servlet API 2.1, use
     * 			{@link #log(String message, Throwable throwable)}
     *			instead.
     *
     * <p>This method was originally defined to write an
     * exception's stack trace and an explanatory error message
     * to the servlet log file.
     *
     * @param exception the <code>Exception</code> error
     * @param msg a <code>String</code> that describes the exception
     */
	@Deprecated
	public void log(Exception exception, String msg) {
		context.log(exception, msg);
	}

	/**
	 * Writes an explanatory message and a stack trace for a given
	 * <code>Throwable</code> exception to the servlet log file. The name and type
	 * of the servlet log file is specific to the servlet container, usually an
	 * event log.
	 *
	 * @param message   a <code>String</code> that describes the error or exception
	 *
	 * @param throwable the <code>Throwable</code> error or exception
	 */
	@Override
	public void log(String message, Throwable throwable) {
		context.log(message, throwable);
	}

	/**
     * Removes the attribute with the given name from
     * this ServletContext. After removal, subsequent calls to
     * {@link #getAttribute} to retrieve the attribute's value
     * will return <code>null</code>.
     *
     *
     * @param name	a <code>String</code> specifying the name
     * 			of the attribute to be removed
     */
	@Override
	public void removeAttribute(String name) {
		context.removeAttribute(name);
	}

	/**
     * Binds an object to a given attribute name in this ServletContext. If
     * the name specified is already used for an attribute, this
     * method will replace the attribute with the new to the new attribute.
     *
     * @param name 	a <code>String</code> specifying the name of the attribute
     *
     * @param object an <code>Object</code> representing the attribute to be bound
     *
     * @throws NullPointerException if the name parameter is {@code null}
     *
     */
	@Override
	public void setAttribute(String name, Object object) {
		context.setAttribute(name, object);
	}

	/**
     * Sets the context initialization parameter with the given name and
     * value on this ServletContext
     *
     * @param name the name of the context initialization parameter to set
     * @param value the value of the context initialization parameter to set
     *
     * @return true if the context initialization parameter with the given
     * name and value was set successfully on this ServletContext, and false
     * if it was not set because this ServletContext already contains a
     * context initialization parameter with a matching name
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws NullPointerException if the name parameter is {@code null}
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
	@Override
	public boolean setInitParameter(String name, String value) {
		return context.setInitParameter(name, value);
	}

	/**
     * Sets the session tracking modes that are to become effective for this
     * <tt>ServletContext</tt>.
     *
     *
     * @param sessionTrackingModes the set of session tracking modes to
     * become effective for this <tt>ServletContext</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if <tt>sessionTrackingModes</tt>
     * specifies a combination of <tt>SessionTrackingMode.SSL</tt> with a
     * session tracking mode other than <tt>SessionTrackingMode.SSL</tt>,
     * or if <tt>sessionTrackingModes</tt> specifies a session tracking mode
     * that is not supported by the servlet container
     *
     * @since Servlet 3.0
     */
	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
		context.setSessionTrackingModes(sessionTrackingModes);
	}
	
	/**
     * Returns the configuration name of the logical host on which the
     * ServletContext is deployed.
     *
     *
     * @return a <code>String</code> containing the configuration name of the
     * logical host on which the servlet context is deployed.
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link javax.servlet.ServletContextListener#contextInitialized} method
     * of a {@link javax.servlet.ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.1
     */
    public String getVirtualServerName() {
    	return context.getVirtualServerName();
    }

	/**
	* 
	* Sets an attribute with a name and value.
	* 
	* @param name the attribute name
	* @param value the attribute value
	* 
	*/
	public void propertyMissing(String name, Object value) {
		setAttribute(name, value);
	}

	/**
	* 
	* Gets an attribute or a parameter value.
	* 
	* @param name the attribute or parameter name
	* @return the attribute or parameter value
	*/
	public Object propertyMissing(String name) {
		Object value = getAttribute(name);
		return value != null ? value : getInitParameter(name);
	}

}