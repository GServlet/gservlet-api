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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.json.JsonSlurper;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

@SuppressWarnings("serial")
public abstract class AbstractServlet extends HttpServlet {

	protected final ThreadLocal<RequestContext> requestContext = new ThreadLocal<>();
	protected final Logger logger = Logger.getLogger(AbstractServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "get");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "post");
	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "put");
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "delete");
	}

	@Override
	public void doHead(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "head");
	}

	@Override
	public void doTrace(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "trace");
	}

	@Override
	public void doOptions(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "options");
	}

	private void route(HttpServletRequest request, HttpServletResponse response, String methodName) {
		requestContext.set(new RequestContext(request, response));
		invoke(methodName);
	}

	private void invoke(String method) {
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method " + method + " has been declared for the servlet " + this.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during invoke method", e);
		}
	}
	
	public HttpServletRequest getRequest() {
		return requestContext.get().getRequest();
	}

	public HttpSession getSession() {
		return requestContext.get().getSession();
	}

	public ServletContext getContext() {
		return requestContext.get().getServletContext();
	}

	public HttpServletResponse getResponse() {
		return requestContext.get().getResponse();
	}

	public Sql getConnection() {
		return (Sql) requestContext.get().getRequest().getAttribute(Constants.CONNECTION);
	}

	public void forward(String location) {
		try {
			HttpServletRequest request = requestContext.get().getRequest(); 
			request.getRequestDispatcher(location).forward(request, getResponse());
		} catch (ServletException | IOException e) {
			logger.log(Level.INFO, "exception during forward method", e);
		}
	}

	public void redirect(String location) throws IOException {
		getResponse().sendRedirect(location);
	}

	public PrintWriter getOut() throws IOException {
		return getResponse().getWriter();
	}

	public void json(Object object) throws IOException {
		getResponse().setHeader("Content-Type", "application/json");
		getResponse().getWriter().write(toJson(object));
	}

	public String stringify(Object object) {
		return toJson(object);
	}

	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = new MarkupBuilder(getOut());
		getResponse().setHeader("Content-Type", "text/html");
		getResponse().getWriter().println("<!DOCTYPE html>");
		return builder;
	}

	public Object parse(InputStream inputStream) {
		return new JsonSlurper().parse(inputStream);
	}

}