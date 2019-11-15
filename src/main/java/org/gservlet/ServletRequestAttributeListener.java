package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.http.HttpServletRequest;

public class ServletRequestAttributeListener extends BaseListener implements javax.servlet.ServletRequestAttributeListener {

	protected ServletRequestAttributeEvent event;

	@Override
	public void attributeAdded(ServletRequestAttributeEvent event) {
		route(event, "add");
	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent event) {
		route(event, "remove");
	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent event) {
		route(event, "replace");
	}

	private void route(ServletRequestAttributeEvent event, String methodName) {
		route(event, methodName);
	}

	public ServletRequestAttributeEvent getEvent() {
		return event;
	}

	public String getName() {
		return event.getName();
	}

	public ServletContext getContext() {
		ServletContext context = event.getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
		HttpServletRequest wrapper = (HttpServletRequest) request.getAttribute(Constants.REQUEST_WRAPPER);
		if (wrapper == null) {
			wrapper = new RequestWrapper(request);
			request.setAttribute(Constants.REQUEST_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public Object getValue() {
		return event.getValue();
	}

}