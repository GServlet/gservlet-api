package org.gservlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletRequestListener extends BaseListener implements javax.servlet.ServletRequestListener {

	protected ServletRequestEvent event;

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		route(event, "init");
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		route(event, "destroy");
	}

	private void route(ServletRequestEvent event, String methodName) {
		this.event = event;
		invoke(methodName);
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
		ServletContext context = event.getServletContext();
		ServletContext wrapper = (ServletContext) context.getAttribute(Constants.CONTEXT_WRAPPER);
		if (wrapper == null) {
			wrapper = new ContextWrapper(context);
			context.setAttribute(Constants.CONTEXT_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public ServletRequestEvent getEvent() {
		return event;
	}

}