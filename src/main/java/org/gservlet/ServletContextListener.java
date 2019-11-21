package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class ServletContextListener extends BaseListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		route(event, "init");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		route(event, "destroy");
	}

	public ServletContext getContext() {
		ServletContext context = (ServletContext) getEvent().getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}
	
	public ServletContextEvent getEvent() {
		return (ServletContextEvent) event;
	}

}