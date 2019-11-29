package org.gservlet;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.xml.MarkupBuilder;

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
		HttpServletRequest wrapper = (HttpServletRequest) request.getAttribute(Constants.REQUEST_WRAPPER);
		if (wrapper == null) {
			wrapper = new RequestWrapper(request);
			request.setAttribute(Constants.REQUEST_WRAPPER, wrapper);
		}
		return wrapper;

	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public FilterChain getFilterChain() {
		return filterChain;
	}
	
	public ServletContext getServletContext() {
		ServletContext context = request.getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}
	
	public HttpSession getSession() {
		HttpSession session = request.getSession(true);
		HttpSession wrapper = (HttpSession) session.getAttribute(Constants.SESSION_WRAPPER);
		if (wrapper == null) {
			wrapper = new SessionWrapper(session);
			session.setAttribute(Constants.SESSION_WRAPPER, (Serializable) wrapper);
		}
		return wrapper;
	}
	
	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = new MarkupBuilder(getResponse().getWriter());
		getResponse().setHeader("Content-Type", "text/html");
		getResponse().getWriter().println("<!DOCTYPE html>");
		return builder;
	}

}
