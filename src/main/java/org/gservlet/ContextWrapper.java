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

	public javax.servlet.ServletRegistration.Dynamic addJspFile(String arg0, String arg1) {
		return null;
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

	public String getRequestCharacterEncoding() {
		return null;
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

	public String getResponseCharacterEncoding() {
		return null;
	}

	@Override
	public String getServerInfo() {
		return context.getServerInfo();
	}

	@Deprecated
	public Servlet getServlet(String arg0) throws ServletException {
		return context.getServlet(arg0);
	}

	@Override
	public String getServletContextName() {
		return context.getServletContextName();
	}

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

	@Deprecated
	public Enumeration<Servlet> getServlets() {
		return context.getServlets();
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return context.getSessionCookieConfig();
	}

	public int getSessionTimeout() {
		return 0;
	}

	public String getVirtualServerName() {
		return null;
	}

	@Override
	public void log(String arg0) {
		context.log(arg0);
	}

	@Deprecated
	public void log(Exception arg0, String arg1) {
		context.log(arg0, arg1);
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

	public void setRequestCharacterEncoding(String arg0) {
	}

	public void setResponseCharacterEncoding(String arg0) {
	}

	public void setSessionTimeout(int arg0) {
	}

	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
	}

	public void propertyMissing(String property, Object value) {
		setAttribute(property, value);
	}

	public Object propertyMissing(String property) {
		Object value = getAttribute(property);
		return value != null ? value : getInitParameter(property);
	}

	public void remove(String name) {
		removeAttribute(name);
	}

}