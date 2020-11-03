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
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

/**
* 
* Context holder for request-specific state
* 
* @author Mamadou Lamine Ba
* 
*/
public class RequestContext {

	/**
	 * The request object
	 */
	protected HttpServletRequest request;
	/**
	 * The response object
	 */
	protected HttpServletResponse response;
	/**
	 * The filter chain object
	 */
	protected FilterChain filterChain;

	/**
	* 
	* Constructs a RequestContext for the given request and response
	* 
	* @param request the request object
	* @param response the response object 
	*  
	*/
	public RequestContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	* 
	* Constructs a RequestContext for the given request, response and filter chain
	* 
	* @param request the request object
	* @param response the response object 
	* @param filterChain the filter chain object
	*  
	*/
	public RequestContext(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		this(request, response);
		this.filterChain = filterChain;
	}

	/**
	* 
	* Returns the HttpServletRequest object
	* 
	* @return the HttpServletRequest object
	* 
	*/
	public HttpServletRequest getRequest() {
		return new RequestWrapper(request);
	}

	/**
	* 
	* Returns the HttpServletResponse object
	* 
	* @return the HttpServletResponse object
	* 
	*/
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	* 
	* Returns the FilterChain object
	* 
	* @return the FilterChain object
	* 
	*/
	public FilterChain getFilterChain() {
		return filterChain;
	}

	/**
	* 
	* Returns the ServletContext object
	* 
	* @return the ServletContext object
	* 
	*/
	public ServletContext getServletContext() {
		return new ServletContextWrapper(request.getServletContext());
	}

	/**
	* 
	* Returns the HttpSession object
	* 
	* @return the HttpSessiobn object
	* 
	*/
	public HttpSession getSession() {
		return new SessionWrapper(request.getSession(true));
	}

	/**
	* 
	* Returns the MarkupBuilder object
	* 
	* @return the MarkupBuilder object
	* @throws IOException the IOException
	*/
	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = new MarkupBuilder(response.getWriter());
		response.setHeader("Content-Type", "text/html");
		response.getWriter().println("<!DOCTYPE html>");
		return builder;
	}
	
	/**
	* 
	* Returns the Sql object
	* 
	* @return the Sql object
	* 
	*/
	public Sql getSql() {
		return (Sql) request.getAttribute(Constants.DB_CONNECTION);
	}

}