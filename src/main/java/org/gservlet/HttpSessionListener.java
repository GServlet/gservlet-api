package org.gservlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class HttpSessionListener extends BaseListener implements javax.servlet.http.HttpSessionListener {

	protected HttpSessionEvent event;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		route(event, "create");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		route(event, "destroy");
	}

	private void route(HttpSessionEvent event, String methodName) {
		this.event = event;
		route(event, methodName);
	}

	public HttpSession getSession() {
		HttpSession session = event.getSession();
		HttpSession wrapper = (HttpSession) session.getAttribute(Constants.SESSION_WRAPPER);
		if (wrapper == null) {
			wrapper = new SessionWrapper(session);
			session.setAttribute(Constants.SESSION_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpSessionEvent getEvent() {
		return event;
	}

}