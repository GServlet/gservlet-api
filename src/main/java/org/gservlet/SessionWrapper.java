package org.gservlet;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class SessionWrapper implements HttpSession {

    protected final HttpSession session;
    
    public SessionWrapper(HttpSession session) {
    	this.session = session;
    }
   
	@Override
	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getValue(String name) {
		return session.getValue(name);
	}

	@Override
	public String[] getValueNames() {
		return session.getValueNames();
	}

	@Override
	public void invalidate() {
		session.invalidate();
	}

	@Override
	public boolean isNew() {
		return session.isNew();
	}

	@Override
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	@Override
	public void removeValue(String name) {
		session.removeValue(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		session.setAttribute(name,value);
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}
	
	public void propertyMissing(String property,Object value) {
		setAttribute(property,value);
	}
	
	public Object propertyMissing(String property) {
		return getAttribute(property);
	}

}