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

import static groovy.json.JsonOutput.toJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.json.JsonSlurper;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

/**
* 
* Abstract class to create an HTTP servlet suitable for a Web site.
* 
* @author Mamadou Lamine Ba
* 
*/
@SuppressWarnings("serial")
public abstract class AbstractServlet extends HttpServlet {

	/**
	 * The request context object.
	 */
	protected final ThreadLocal<RequestContext> requestContext = new ThreadLocal<>();
	/**
	 * The logger object.
	 */
	protected final Logger logger = Logger.getLogger(AbstractServlet.class.getName());
	
	/**
	* 
	* Invokes the get method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "get");
	}

	/**
	* 
	* Invokes the post method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "post");
	}

	/**
	* 
	* Invokes the put method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "put");
	}

	/**
	* 
	* Invokes the delete method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "delete");
	}

	/**
	* 
	* Invokes the head method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doHead(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "head");
	}

	/**
	* 
	* Invokes the trace method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doTrace(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "trace");
	}

	/**
	* 
	* Invokes the options method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* 
	*/
	@Override
	public void doOptions(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "options");
	}

	/**
	* 
	* Invokes the method defined on the subclasses.
	* 
	* @param request the HttpServletRequest object
	* @param response the HttpServletResponse object
	* @param method the method
	*/
	public void route(HttpServletRequest request, HttpServletResponse response, String method) {
		requestContext.set(new RequestContext(request, response));
		invoke(method);
	}

	/**
	* 
	* Invokes the provided method defined on the subclasses.
	* 
	* @param method the method
	* 
	*/
	protected void invoke(String method) {
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method " + method + " has been declared for the servlet " + this.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during invoke method", e);
		}
	}
	
	/**
	* 
	* Forward the request to another location.
	* 
	* @param location the location
	* 
	*/
	public void forward(String location) {
		try {
			HttpServletRequest request = requestContext.get().getRequest(); 
			request.getRequestDispatcher(location).forward(request, getResponse());
		} catch (ServletException | IOException e) {
			logger.log(Level.INFO, "exception during forward method", e);
		}
	}

	/**
	* 
	* Redirect the request to another location.
	* 
	* @param location the location
	* @throws IOException the IOException
	* 
	*/
	public void redirect(String location) throws IOException {
		getResponse().sendRedirect(location);
	}

	/**
	* 
	* Sends the response as JSON.
	* 
	* @param response the response object
	* @throws IOException the IOException
	* 
	*/
	public void json(Object response) throws IOException {
		getResponse().setHeader("Content-Type", "application/json");
		getResponse().getWriter().write(toJson(response));
	}

	/**
	* 
	* Converts the object to JSON.
	* 
	* @param object the object
	* @return  the JSON output
	* 
	*/
	public String stringify(Object object) {
		return toJson(object);
	}

	/**
	* 
	* Parses the input stream to JSON.
	* 
	* @param inputStream the input stream
	* @return  the JSON output
	* 
	*/
	public Object parse(InputStream inputStream) {
		return new JsonSlurper().parse(inputStream);
	}
	
	/**
	* 
	* The ServletConfig object.
	* 
	* @return the ServletConfig object
	* 
	*/
	public ServletConfig getConfig() {
		return getServletConfig();
	}
	
	/**
	* 
	* The HttpServletRequest object.
	* 
	* @return the HttpServletRequest object
	* 
	*/
	public HttpServletRequest getRequest() {
		return requestContext.get().getRequest();
	}

	/**
	* 
	* The HttpSession object.
	* 
	* @return the HttpSession object
	* 
	*/
	public HttpSession getSession() {
		return requestContext.get().getSession();
	}

	/**
	* 
	* The ServletContext object.
	* 
	* @return the ServletContext object
	* 
	*/
	public ServletContext getContext() {
		return requestContext.get().getServletContext();
	}

	/**
	* 
	* The HttpServletResponse object.
	* 
	* @return the HttpServletResponse object
	* 
	*/
	public HttpServletResponse getResponse() {
		return requestContext.get().getResponse();
	}

	/**
	* 
	* The Sql object.
	* 
	* @return the Sql object
	* 
	*/
	public Sql getConnection() {
		return requestContext.get().getConnection();
	}

	/**
	* 
	* The PrintWriter object.
	* 
	* @return the PrintWriter object
	* @throws IOException the IOException
	* 
	*/
	public PrintWriter getOut() throws IOException {
		return getResponse().getWriter();
	}

	/**
	* 
	* The MarkupBuilder object.
	* 
	* @return the MarkupBuilder object
	* @throws IOException the IOException
	* 
	*/
	public MarkupBuilder getHtml() throws IOException {
		return requestContext.get().getHtml();
	}
	
}