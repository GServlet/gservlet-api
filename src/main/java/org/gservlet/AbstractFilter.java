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
import java.io.PrintWriter;
import java.util.logging.Level;
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
import javax.servlet.http.HttpSession;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

public abstract class AbstractFilter implements Filter {

	protected static FilterConfig config;
	protected final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
	protected Logger logger = Logger.getLogger(AbstractFilter.class.getName());
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			AbstractFilter.config = config;
			getClass().getDeclaredMethod("init").invoke(this);
		} catch (NoSuchMethodException e) {
			// the exception is ignored if there is no init method
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during init method", e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		requestHolder.set((HttpServletRequest) request);
		requestHolder.get().setAttribute(Constants.SERVLET_RESPONSE,response);
		requestHolder.get().setAttribute(Constants.FILTER_CHAIN, chain);
		try {
			getClass().getDeclaredMethod("filter").invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method filter has been declared for the filter " + this.getClass().getName());
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during doFilter method", e);
		}
	}

	public void next() throws IOException, ServletException {
		FilterChain chain = (FilterChain) requestHolder.get().getAttribute(Constants.FILTER_CHAIN);
		chain.doFilter(requestHolder.get(), getResponse());
	}

	@Override
	public void destroy() {
		// no implementation provided
	}

	public HttpServletRequest getRequest() {
		return new RequestWrapper(requestHolder.get());
	}

	public HttpSession getSession() {
		return new SessionWrapper(requestHolder.get().getSession(true));
	}

	public ServletContext getContext() {
		return new ContextWrapper(requestHolder.get().getServletContext());
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) requestHolder.get().getAttribute(Constants.SERVLET_RESPONSE);
	}

	public FilterConfig getConfig() {
		return AbstractFilter.config;
	}

	public Sql getConnection() {
		return (Sql) requestHolder.get().getAttribute(Constants.CONNECTION);
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

}