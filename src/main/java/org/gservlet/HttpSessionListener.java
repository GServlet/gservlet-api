package org.gservlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

public class HttpSessionListener extends BaseListener implements javax.servlet.http.HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		route(event, "sessionCreated");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		route(event, "sessionDestroyed");
	}

	public HttpSession getSession() {
		HttpSession session = getEvent().getSession();
		HttpSession wrapper = (HttpSession) session.getAttribute(Constants.SESSION_WRAPPER);
		if (wrapper == null) {
			wrapper = new SessionWrapper(session);
			session.setAttribute(Constants.SESSION_WRAPPER, wrapper);
		}
		return wrapper;
	}

	public HttpSessionEvent getEvent() {
		return (HttpSessionEvent) event;
	}

}