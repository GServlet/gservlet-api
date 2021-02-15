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

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Abstract class to perform filtering tasks on either the request to a resource,
 *  or on the response from a resource, or both
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public abstract class AbstractFilter implements Filter, RequestHandler {

	/**
	 * The filter config object
	 */
	protected FilterConfig config;
	/**
	 * The request context object
	 */
	protected final ThreadLocal<RequestContext> requestContext = new ThreadLocal<>();
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * Called by the web container to indicate to a filter that it is being placed
	 * into service
	 * 
	 * @param config the filter config
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		init();
	}

	/**
	 * 
	 * A convenience method which can be overridden so that there's no need to call
	 * <code>super.init(config)</code>
	 * 
	 * The <code>FilterConfig</code> object can still be retrieved via
	 * {@link #getConfig}.
	 * 
	 * @throws ServletException the ServletException
	 * 
	 */
	public void init() throws ServletException {
		// no implementation provided
	}

	/**
	 * 
	 * Invokes the filter method defined on the subclasses
	 * 
	 * @param request  the request
	 * @param response the response
	 * @param chain    the filter chain
	 * @throws IOException      the IOException
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		requestContext.set(new RequestContext((HttpServletRequest) request, (HttpServletResponse) response, chain));
		filter();
	}

	/**
	 * 
	 * A convenience method which can be overridden so that there's no need to implement the
	 * <code>doFilter(request, response, chain)</code> method
	 * 
	 * @throws ServletException the ServletException 
	 * @throws IOException the IOException 
	 * 
	 * 
	 */
	public void filter() throws IOException, ServletException {
		getChain().doFilter(getRequest(), getResponse());
	}

	/**
	 * 
	 * Invokes the doFilter(request, response) method of the FilterChain object
	 * 
	 * @throws IOException the IOException
	 * @throws ServletException the ServletException
	 * 
	 */
	public void next() throws IOException, ServletException {
		FilterChain chain = getRequestContext().getFilterChain();
		chain.doFilter(getRequest(), getResponse());
	}

	/**
	 * 
	 * Called by the web container to indicate to a filter that it is being taken
	 * out of service
	 * 
	 */
	@Override
	public void destroy() {
		// no implementation provided
	}
	
	/**
	 * 
	 * Returns a String containing the value of the named initialization parameter,
	 * or null if the parameter does not exist
	 * 
	 * @param name a String specifying the name of the initialization parameter
	 * @return a String containing the value of the initialization parameter
	 * 
	 */
	public String getInitParameter(String name) {
		return config.getInitParameter(name);
	}

	/**
	 * 
	 * Returns the FilterConfig object
	 * 
	 * @return the FilterConfig object
	 * 
	 */
	public FilterConfig getConfig() {
		return new FilterConfigWrapper(config);
	}

	/**
	 * 
	 * Returns the FilterChain object
	 * 
	 * @return the FilterChain object
	 * 
	 */
	public FilterChain getChain() {
		return getRequestContext().getFilterChain();
	}

	

	/**
	 * 
	 * Returns the ServletContext object
	 * 
	 * @return the ServletContext object
	 * 
	 */
	public ServletContext getContext() {
		ServletContext context = getConfig().getServletContext();
		return context !=null ? new ServletContextWrapper(context) : getRequestContext().getServletContext();
	}

	/**
	 * 
	 * Returns the Logger object
	 * 
	 * @return the Logger object
	 * 
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * 
	 * Returns the RequestContext object
	 * 
	 * @return the RequestContext object
	 * 
	 */
	public RequestContext getRequestContext() {
		return requestContext.get();
	}

}