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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.json.JsonSlurper;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

@SuppressWarnings("serial")
public abstract class HttpServlet extends javax.servlet.http.HttpServlet {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "get");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "post");
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "put");
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "delete");
	}

	public void doHead(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "head");
	}

	public void doTrace(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "trace");
	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response) {
		route(request, response, "options");
	}

	private void route(HttpServletRequest request, HttpServletResponse response, String methodName) {
		this.request = request;
		this.response = response;
		invoke(methodName);
	}

	private void invoke(String method) {
		response.setContentType("text/html");
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method " + method + " has been declared for the servlet " + this.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during invoke method", e);
		}
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest wrapper = (HttpServletRequest) request.getAttribute(Constants.REQUEST_WRAPPER);
		if (wrapper == null) {
			wrapper = new RequestWrapper(request);
			request.setAttribute(Constants.REQUEST_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpSession getSession() {
		HttpSession session = request.getSession(true);
		HttpSession wrapper = (HttpSession) session.getAttribute(Constants.SESSION_WRAPPER);
		if (wrapper == null) {
			wrapper = new SessionWrapper(session);
			session.setAttribute(Constants.SESSION_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public ServletContext getContext() {
		ServletContext context = request.getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Sql getConnection() {
		return (Sql) request.getAttribute(Constants.CONNECTION);
	}

	public void forward(String location) {
		try {
			request.getRequestDispatcher(location).forward(request, response);
		} catch (ServletException | IOException e) {
			logger.log(Level.INFO, "exception during forward method", e);
		}
	}

	public void redirect(String location) throws IOException {
		response.sendRedirect(location);
	}

	public PrintWriter getOut() throws IOException {
		return response.getWriter();
	}

	public void json(Object object) throws IOException {
		response.setHeader("Content-Type", "application/json");
		response.getWriter().write(toJson(object));
	}

	public String stringify(Object object) throws IOException {
		return toJson(object);
	}

	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = (MarkupBuilder) request.getAttribute("MarkupBuilder");
		if (builder == null) {
			response.setHeader("Content-Type", "text/html");
			response.getWriter().println("<!DOCTYPE html>");
			builder = new MarkupBuilder(response.getWriter());
			request.setAttribute("MarkupBuilder", builder);
		}
		return builder;
	}

	public Object parse(InputStream inputStream) throws IOException {
		return new JsonSlurper().parse(inputStream);
	}

}