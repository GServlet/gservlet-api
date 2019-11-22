package org.gservlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import groovy.json.JsonSlurper;

public class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public void propertyMissing(String property, Object value) {
		setAttribute(property, value);
	}

	public Object propertyMissing(String property) throws IOException {
		if (property.equals("body") && getContentType().equalsIgnoreCase("application/json")) {
			return new JsonSlurper().parse(getInputStream());
		}
		Object value = getAttribute(property);
		return value != null ? value : getParameter(property);
	}

}