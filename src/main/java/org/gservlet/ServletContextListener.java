package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class ServletContextListener extends BaseListener implements javax.servlet.ServletContextListener {

	protected ServletContext context;

	public void contextInitialized(ServletContext context) {
		route(context, "init");
	}

	public void contextDestroyed(ServletContext context) {
		route(context, "destroy");
	}

	private void route(ServletContext context, String methodName) {
		this.context = context;
		route(context, methodName);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	public ServletContext getContext() {
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

}