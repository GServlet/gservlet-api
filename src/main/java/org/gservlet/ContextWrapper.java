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
* 
* 
* @author Mamadou Lamine Ba
* 
*/
public class ContextWrapper implements ServletContext {

	protected final ServletContext context;

	public ContextWrapper(ServletContext context) {
		this.context = context;
	}

	@Override
	public Dynamic addFilter(String arg0, String arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public Dynamic addFilter(String arg0, Filter arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		return context.addFilter(arg0, arg1);
	}

	@Override
	public void addListener(String arg0) {
		context.addListener(arg0);
	}

	@Override
	public <T extends EventListener> void addListener(T arg0) {
		context.addListener(arg0);
	}

	@Override
	public void addListener(Class<? extends EventListener> arg0) {
		context.addListener(arg0);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, String arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Servlet arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
		return context.addServlet(arg0, arg1);
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
		return context.createFilter(arg0);
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
		return context.createListener(arg0);
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
		return context.createServlet(arg0);
	}

	@Override
	public void declareRoles(String... arg0) {
		context.declareRoles(arg0);
	}

	@Override
	public Object getAttribute(String arg0) {
		return context.getAttribute(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return context.getAttributeNames();
	}

	@Override
	public ClassLoader getClassLoader() {
		return context.getClassLoader();
	}

	@Override
	public ServletContext getContext(String arg0) {
		return context.getContext(arg0);
	}

	@Override
	public String getContextPath() {
		return context.getContextPath();
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return context.getDefaultSessionTrackingModes();
	}

	@Override
	public int getEffectiveMajorVersion() {
		return context.getEffectiveMajorVersion();
	}

	@Override
	public int getEffectiveMinorVersion() {
		return context.getEffectiveMinorVersion();
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return context.getEffectiveSessionTrackingModes();
	}

	@Override
	public FilterRegistration getFilterRegistration(String arg0) {
		return context.getFilterRegistration(arg0);
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return context.getFilterRegistrations();
	}

	@Override
	public String getInitParameter(String arg0) {
		return context.getInitParameter(arg0);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return context.getInitParameterNames();
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return context.getJspConfigDescriptor();
	}

	@Override
	public int getMajorVersion() {
		return context.getMajorVersion();
	}

	@Override
	public String getMimeType(String arg0) {
		return context.getMimeType(arg0);
	}

	@Override
	public int getMinorVersion() {
		return context.getMinorVersion();
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		return context.getNamedDispatcher(arg0);
	}

	@Override
	public String getRealPath(String arg0) {
		return context.getRealPath(arg0);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return context.getRequestDispatcher(arg0);
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		return context.getResource(arg0);
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		return context.getResourceAsStream(arg0);
	}

	@Override
	public Set<String> getResourcePaths(String arg0) {
		return context.getResourcePaths(arg0);
	}

	@Override
	public String getServerInfo() {
		return context.getServerInfo();
	}

	/**
     * @deprecated	As of Java Servlet API 2.1, with no direct replacement.
     *
     * <p>This method was originally defined to retrieve a servlet
     * from a <code>ServletContext</code>. In this version, this method
     * always returns <code>null</code> and remains only to preserve
     * binary compatibility. This method will be permanently removed
     * in a future version of the Java Servlet API.
     *
     * <p>In lieu of this method, servlets can share information using the
     * <code>ServletContext</code> class and can perform shared business logic
     * by invoking methods on common non-servlet classes.
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

	@Override
	public String getServletContextName() {
		return context.getServletContextName();
	}

	/**
     * @deprecated	As of Java Servlet API 2.1, with no replacement.
     *
     * <p>This method was originally defined to return an
     * <code>Enumeration</code>
     * of all the servlet names known to this context. In this version,
     * this method always returns an empty <code>Enumeration</code> and
     * remains only to preserve binary compatibility. This method will
     * be permanently removed in a future version of the Java Servlet API.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet} names
     */
	@Deprecated
	public Enumeration<String> getServletNames() {
		return context.getServletNames();
	}

	@Override
	public ServletRegistration getServletRegistration(String arg0) {
		return context.getServletRegistration(arg0);
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return context.getServletRegistrations();
	}

	/**
     * @deprecated	As of Java Servlet API 2.0, with no replacement.
     *
     * <p>This method was originally defined to return an
     * <code>Enumeration</code> of all the servlets known to this servlet
     * context.
     * In this version, this method always returns an empty enumeration and
     * remains only to preserve binary compatibility. This method
     * will be permanently removed in a future version of the Java
     * Servlet API.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet}
     */
	@Deprecated
	public Enumeration<Servlet> getServlets() {
		return context.getServlets();
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return context.getSessionCookieConfig();
	}

	@Override
	public void log(String arg0) {
		context.log(arg0);
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

	@Override
	public void log(String arg0, Throwable arg1) {
		context.log(arg0, arg1);
	}

	@Override
	public void removeAttribute(String arg0) {
		context.removeAttribute(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		context.setAttribute(arg0, arg1);
	}

	@Override
	public boolean setInitParameter(String arg0, String arg1) {
		return context.setInitParameter(arg0, arg1);
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
		context.setSessionTrackingModes(arg0);
	}

	public void propertyMissing(String property, Object value) {
		setAttribute(property, value);
	}

	public Object propertyMissing(String property) {
		Object value = getAttribute(property);
		return value != null ? value : getInitParameter(property);
	}

}