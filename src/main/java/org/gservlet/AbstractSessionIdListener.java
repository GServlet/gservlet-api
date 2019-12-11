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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

/**
 * 
 * Abstract class for receiving notification events about HttpSession id
 * changes.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public abstract class AbstractSessionIdListener extends AbstractListener implements HttpSessionIdListener {

	/**
	 * The old session Id constant
	 */
	
	protected static final String OLDSESSIONID = "oldSessionId";
	/**
	 * 
	 * Notifies the object that the session id has been changed in a session
	 * 
	 * @param event the HttpSessionBindingEvent containing the session
	 * @param oldSessionId the old session Id
	 * 
	 */
	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
		event.getSession().setAttribute(OLDSESSIONID, oldSessionId);
		invoke("sessionIdChanged", event);
	}

	/**
	 * 
	 * Returns the HttpSession object
	 * 
	 * @return the HttpSession object
	 * 
	 */
	public HttpSession getSession() {
		return new SessionWrapper(getEvent().getSession());
	}

	/**
	 * 
	 * Returns the HttpSessionEvent object
	 * 
	 * @return the HttpSessionEvent object
	 * 
	 */
	public HttpSessionEvent getEvent() {
		return (HttpSessionEvent) eventHolder.get();
	}

	/**
	 * 
	 * Returns the old session Id
	 * 
	 * @return the old session Id
	 * 
	 */
	public String getOldSessionId() {
		return (String) getEvent().getSession().getAttribute(OLDSESSIONID);
	}

}