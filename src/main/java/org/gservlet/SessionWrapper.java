/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.gservlet;

import java.io.Serializable;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * 
 * A wrapper class around the HttpSession class.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
@SuppressWarnings("serial")
public class SessionWrapper implements HttpSession, Serializable {

	/**
	 * The session object
	 */
	protected final transient HttpSession session;

	/**
	 * 
	 * Constructs a SessionWrapper for the given HttpSession
	 * 
	 * @param session the session object
	 * 
	 */
	public SessionWrapper(HttpSession session) {
		this.session = session;
	}

	/**
	 * Returns the object bound with the specified name in this session, or
	 * <code>null</code> if no object is bound under the name.
	 *
	 * @param name a string specifying the name of the object
	 *
	 * @return the object with the specified name
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 * 
	 */
	@Override
	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	/**
	 * Returns an <code>Enumeration</code> of <code>String</code> objects containing
	 * the names of all the objects bound to this session.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects specifying
	 *         the names of all the objects bound to this session
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 * 
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

	/**
	 *
	 * Returns the time when this session was created, measured in milliseconds
	 * since midnight January 1, 1970 GMT.
	 *
	 * @return a <code>long</code> specifying when this session was created,
	 *         expressed in milliseconds since 1/1/1970 GMT
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	/**
	 * Returns a string containing the unique identifier assigned to this session.
	 * The identifier is assigned by the servlet container and is implementation
	 * dependent.
	 * 
	 * @return a string specifying the identifier assigned to this session
	 */
	@Override
	public String getId() {
		return session.getId();
	}

	/**
	 *
	 * Returns the last time the client sent a request associated with this session,
	 * as the number of milliseconds since midnight January 1, 1970 GMT, and marked
	 * by the time the container received the request.
	 *
	 * <p>
	 * Actions that your application takes, such as getting or setting a value
	 * associated with the session, do not affect the access time.
	 *
	 * @return a <code>long</code> representing the last time the client sent a
	 *         request associated with this session, expressed in milliseconds since
	 *         1/1/1970 GMT
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 * 
	 */
	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	/**
	 * Returns the maximum time interval, in seconds, that the servlet container
	 * will keep this session open between client accesses. After this interval, the
	 * servlet container will invalidate the session. The maximum time interval can
	 * be set with the <code>setMaxInactiveInterval</code> method.
	 *
	 * <p>
	 * A return value of zero or less indicates that the session will never timeout.
	 *
	 * @return an integer specifying the number of seconds this session remains open
	 *         between client requests
	 *
	 * @see #setMaxInactiveInterval
	 */
	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	/**
	 * Returns the ServletContext to which this session belongs.
	 * 
	 * @return The ServletContext object for the web application
	 * @since Servlet 2.3
	 */
	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	/**
	 *
	 * @deprecated As of Version 2.1, this method is deprecated and has no
	 *             replacement. It will be removed in a future version of the Java
	 *             Servlet API.
	 *
	 * @return the @see javax.servlet.http.HttpSessionContext for this session.
	 */
	@Deprecated
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	/**
	 * @deprecated As of Version 2.2, this method is replaced by
	 *             {@link #getAttribute}.
	 *
	 * @param name a string specifying the name of the object
	 *
	 * @return the object with the specified name
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Deprecated
	public Object getValue(String name) {
		return session.getValue(name);
	}

	/**
	 * @deprecated As of Version 2.2, this method is replaced by
	 *             {@link #getAttributeNames}
	 *
	 * @return an array of <code>String</code> objects specifying the names of all
	 *         the objects bound to this session
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Deprecated
	public String[] getValueNames() {
		return session.getValueNames();
	}

	/**
	 * Invalidates this session then unbinds any objects bound to it.
	 *
	 * @exception IllegalStateException if this method is called on an already
	 *                                  invalidated session
	 */
	@Override
	public void invalidate() {
		session.invalidate();
	}

	/**
	 * Returns <code>true</code> if the client does not yet know about the session
	 * or if the client chooses not to join the session. For example, if the server
	 * used only cookie-based sessions, and the client had disabled the use of
	 * cookies, then a session would be new on each request.
	 *
	 * @return <code>true</code> if the server has created a session, but the client
	 *         has not yet joined
	 *
	 * @exception IllegalStateException if this method is called on an already
	 *                                  invalidated session
	 */
	@Override
	public boolean isNew() {
		return session.isNew();
	}

	/**
	 * @deprecated As of Version 2.2, this method is replaced by
	 *             {@link #setAttribute}
	 *
	 * @param name  the name to which the object is bound; cannot be null
	 *
	 * @param value the object to be bound; cannot be null
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Deprecated
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	/**
	 * Removes the object bound with the specified name from this session. If the
	 * session does not have an object bound with the specified name, this method
	 * does nothing.
	 *
	 * <p>
	 * After this method executes, and if the object implements
	 * <code>HttpSessionBindingListener</code>, the container calls
	 * <code>HttpSessionBindingListener.valueUnbound</code>. The container then
	 * notifies any <code>HttpSessionAttributeListener</code>s in the web
	 * application.
	 *
	 * @param name the name of the object to remove from this session
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Override
	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	/**
	 * @deprecated As of Version 2.2, this method is replaced by
	 *             {@link #removeAttribute}
	 *
	 * @param name the name of the object to remove from this session
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Deprecated
	public void removeValue(String name) {
		session.removeValue(name);
	}

	/**
	 * Binds an object to this session, using the name specified. If an object of
	 * the same name is already bound to the session, the object is replaced.
	 *
	 * <p>
	 * After this method executes, and if the new object implements
	 * <code>HttpSessionBindingListener</code>, the container calls
	 * <code>HttpSessionBindingListener.valueBound</code>. The container then
	 * notifies any <code>HttpSessionAttributeListener</code>s in the web
	 * application.
	 * 
	 * <p>
	 * If an object was already bound to this session of this name that implements
	 * <code>HttpSessionBindingListener</code>, its
	 * <code>HttpSessionBindingListener.valueUnbound</code> method is called.
	 *
	 * <p>
	 * If the value passed in is null, this has the same effect as calling
	 * <code>removeAttribute()</code>.
	 *
	 *
	 * @param name  the name to which the object is bound; cannot be null
	 *
	 * @param value the object to be bound
	 *
	 * @exception IllegalStateException if this method is called on an invalidated
	 *                                  session
	 */
	@Override
	public void setAttribute(String name, Object value) {
		session.setAttribute(name, value);
	}

	/**
	 * Specifies the time, in seconds, between client requests before the servlet
	 * container will invalidate this session.
	 *
	 * <p>
	 * An <tt>interval</tt> value of zero or less indicates that the session should
	 * never timeout.
	 *
	 * @param interval An integer specifying the number of seconds
	 */
	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	/**
	 * 
	 * Sets an attribute
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value
	 * 
	 */
	public void propertyMissing(String name, Object value) {
		setAttribute(name, value);
	}

	/**
	 * 
	 * Gets an attribute value
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 */
	public Object propertyMissing(String name) {
		return getAttribute(name);
	}

}