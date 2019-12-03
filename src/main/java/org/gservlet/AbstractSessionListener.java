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
import javax.servlet.http.HttpSessionListener;

/**
* 
* Abstract class for receiving notification events about HttpSession lifecycle changes.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractSessionListener extends AbstractListener implements HttpSessionListener {

	/**
	* 
	* Receives notification that a session has been created.
	* 
	* @param event the HttpSessionEvent containing the session
	* 
	*/
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		route(event, "sessionCreated");
	}

	/**
	* 
	* Receives notification that a session is about to be invalidated.
	* 
	* @param event the HttpSessionEvent containing the session
	* 
	*/
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		route(event, "sessionDestroyed");
	}

	/**
	* 
	* The HttpSession object.
	* 
	* @return the HttpSession object
	* 
	*/
	public HttpSession getSession() {
		return new SessionWrapper(getEvent().getSession());
	}

	/**
	* 
	* The HttpSessionEvent object.
	* 
	* @return the HttpSessionEvent object
	* 
	*/
	public HttpSessionEvent getEvent() {
		return (HttpSessionEvent) eventHolder.get();
	}

}