package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;

public class ServletContextAttributeListener extends BaseListener
		implements javax.servlet.ServletContextAttributeListener {

	protected ServletContextAttributeEvent event;

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		route(event, "add");
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		route(event, "remove");
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		route(event, "replace");
	}

	private void route(ServletContextAttributeEvent event, String methodName) {
		this.event = event;
		route(event, methodName);
	}

	public ServletContextAttributeEvent getEvent() {
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

	public Object getValue() {
		return event.getValue();
	}

}