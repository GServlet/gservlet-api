package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;

public class ServletContextAttributeListener extends BaseListener implements javax.servlet.ServletContextAttributeListener {

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		route(event, "attributeAdded");
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		route(event, "attributeRemoved");
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		route(event, "attributeReplaced");
	}

	public ServletContextAttributeEvent getEvent() {
		return (ServletContextAttributeEvent) event;
	}

	public String getName() {
		return getEvent().getName();
	}

	public ServletContext getContext() {
		ServletContext context = getEvent().getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public Object getValue() {
		return getEvent().getValue();
	}

}