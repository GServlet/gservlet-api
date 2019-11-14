package org.gservlet;

import static groovy.json.JsonOutput.toJson;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.logging.Logger;
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

public abstract class Filter implements javax.servlet.Filter {

	protected FilterConfig config;
	protected FilterChain chain;
	protected ServletRequest request;
	protected ServletResponse response;
	protected Logger logger = Logger.getLogger(Filter.class.getName());

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		try {
			Method method = getClass().getDeclaredMethod("init");
			method.invoke(this);
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.request = request;
		this.response = response;
		this.chain = chain;
		try {
			Method method = getClass().getDeclaredMethod("filter");
			method.invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method filter has been declared for the filter " + this.getClass().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void next() throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest wrapper = (HttpServletRequest) request.getAttribute(Constants.REQUEST_WRAPPER);
		if (wrapper == null) {
			wrapper = new RequestWrapper((HttpServletRequest) request);
			request.setAttribute(Constants.REQUEST_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpSession getSession() {
		HttpSession session = getRequest().getSession(true);
		HttpSession wrapper = (HttpSession) session.getAttribute(Constants.SESSION_WRAPPER);
		if (wrapper == null) {
			wrapper = new SessionWrapper(session);
			session.setAttribute(Constants.SESSION_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public ServletContext getContext() {
		ServletContext context = getRequest().getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) response;
	}

	public FilterConfig getConfig() {
		return config;
	}

	public FilterChain getChain() {
		return chain;
	}

	public Sql getConnection() {
		return (Sql) request.getAttribute(Constants.CONNECTION);
	}

	public PrintWriter getOut() throws IOException {
		return response.getWriter();
	}

	public void json(Object object) throws IOException {
		getResponse().setHeader("Content-Type", "application/json");
		getResponse().getWriter().write(toJson(object));
	}

	public String stringify(Object object) throws IOException {
		return toJson(object);
	}
	
	public MarkupBuilder getHtml() throws IOException {
		MarkupBuilder builder = (MarkupBuilder) request.getAttribute("MarkupBuilder");
		if(builder==null) {
			getResponse().setHeader("Content-Type", "text/html");
			getResponse().getWriter().println("<!DOCTYPE html>");
			builder = new MarkupBuilder(response.getWriter());
			request.setAttribute("MarkupBuilder",builder);
		}
		return builder;
	}

}