package org.gservlet;

import static groovy.json.JsonOutput.toJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.gservlet.annotation.Dao;
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

	private void invoke(String methodName) {
		response.setContentType("text/html");
		try {
			injectDaoIfPresent();
			Method method = getClass().getDeclaredMethod(methodName);
			method.invoke(this);
		} catch (NoSuchMethodException e) {
			logger.info("no method " + methodName + " has been declared for the servlet " + this.getClass().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void injectDaoIfPresent() throws Exception {
		Class<?> clazz = this.getClass();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getType().isAnnotationPresent(Dao.class)) {
					BaseDao dao = (BaseDao) field.getType().newInstance();
					dao.setConnection(getConnection());
					field.setAccessible(true);
					field.set(this, dao);
				}
			}
			clazz = clazz.getSuperclass();
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
			e.printStackTrace();
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