package org.gservlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

public class HttpSessionAttributeListener extends BaseListener implements javax.servlet.http.HttpSessionAttributeListener {

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		route(event, "add");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		route(event, "remove");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		route(event, "replace");
	}

	public HttpSessionBindingEvent getEvent() {
		return (HttpSessionBindingEvent) event;
	}

	public String getName() {
		return getEvent().getName();
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

	public Object getValue() {
		return getEvent().getValue();
	}

}