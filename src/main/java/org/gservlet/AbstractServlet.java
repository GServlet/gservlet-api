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
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * Abstract class to create a servlet that can respond to HTTP requests
 * 
 * @author Mamadou Lamine Ba
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractServlet extends HttpServlet implements RequestHandler {

	/**
	 * The RequestContext object
	 */
	protected final ThreadLocal<RequestContext> requestContext = new ThreadLocal<>();
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * Invokes the get method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "get");
	}

	/**
	 * 
	 * Invokes the post method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "post");
	}

	/**
	 * 
	 * Invokes the put method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "put");
	}

	/**
	 * 
	 * Invokes the delete method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "delete");
	}

	/**
	 * 
	 * Invokes the head method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "head");
	}

	/**
	 * 
	 * Invokes the trace method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "trace");
	}

	/**
	 * 
	 * Invokes the options method defined on the subclasses
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException the ServletException
	 * 
	 */
	@Override
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		service(request, response, "options");
	}

	/**
	 * 
	 * Allows a servlet to respond to a request
	 * 
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @param method   the http method
	 * @throws ServletException the ServletException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response, String method)
			throws ServletException {
		try {
			requestContext.set(new RequestContext(request, response));
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method " + method + " has been declared for the servlet " + this.getClass().getName());
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * 
	 * Forwards the request to another location
	 * 
	 * @param location the location
	 * @throws IOException      the IOException
	 * @throws ServletException the ServletException
	 * 
	 */
	public void forward(String location) throws ServletException, IOException {
		HttpServletRequest request = getRequest();
		request.getRequestDispatcher(location).forward(request, getResponse());
	}

	/**
	 * 
	 * Redirect the request to another location
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
	 * Returns the ServletConfig object
	 * 
	 * @return the ServletConfig object
	 * 
	 */
	public ServletConfig getConfig() {
		return new ServletConfigWrapper(getServletConfig());
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