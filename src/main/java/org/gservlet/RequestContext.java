package org.gservlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain filterChain;

	public RequestContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public RequestContext(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		this(request, response);
		this.filterChain = filterChain;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public FilterChain getFilterChain() {
		return filterChain;
	}
	
	public ServletContext getServletContext() {
		return request.getServletContext();
	}
	
	public HttpSession getSession() {
		return request.getSession(true);
	}

}
