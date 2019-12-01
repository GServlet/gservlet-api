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
* 
* 
* @author Mamadou Lamine Ba
* 
*/
public class RequestContext {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected FilterChain filterChain;

	public RequestContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public RequestContext(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		this(request, response);
		this.filterChain = filterChain;
	}

	public HttpServletRequest getRequest() {
		return new RequestWrapper(request);
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public FilterChain getFilterChain() {
		return filterChain;
	}

	public ServletContext getServletContext() {
		return new ContextWrapper(request.getServletContext());
	}

	public HttpSession getSession() {
		return new SessionWrapper(request.getSession(true));
	}

	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = new MarkupBuilder(response.getWriter());
		getResponse().setHeader("Content-Type", "text/html");
		getResponse().getWriter().println("<!DOCTYPE html>");
		return builder;
	}
	
	public Sql getConnection() {
		return (Sql) request.getAttribute(Constants.CONNECTION);
	}

}