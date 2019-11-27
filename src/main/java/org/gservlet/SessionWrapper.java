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

	@Deprecated
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Deprecated
	public Object getValue(String name) {
		return session.getValue(name);
	}

	@Deprecated
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

	@Deprecated
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		session.removeAttribute(name);
	}

	@Deprecated
	public void removeValue(String name) {
		session.removeValue(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		session.setAttribute(name, (Serializable) value);
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	public void propertyMissing(String property, Object value) {
		setAttribute(property, value);
	}

	public Object propertyMissing(String property) {
		return getAttribute(property);
	}

}