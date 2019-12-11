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
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
* 
* Abstract class for receiving notification events when an object is bound to or unbound from a session.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractSessionBindingListener extends AbstractListener implements HttpSessionBindingListener {

	/**
	* 
	* Notifies the object that it is being bound to a session and identifies the session
	* 
	* @param event the HttpSessionBindingEvent containing the session
	* 
	*/
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		invoke("valueBound", event);
	}

	/**
	* 
	* Notifies the object that it is being unbound from a session and identifies the session
	* 
	* @param event the HttpSessionBindingEvent containing the session
	* 
	*/
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		invoke("valueUnbound", event);
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
	* Returns the HttpSessionBindingEvent object
	* 
	* @return the HttpSessionBindingEvent object
	* 
	*/
	public HttpSessionBindingEvent getEvent() {
		return (HttpSessionBindingEvent) eventHolder.get();
	}
	
	/**
	* 
	* Returns the attribute name
	* 
	* @return the attribute name
	* 
	*/
	public String getName() {
		return getEvent().getName();
	}
	
	/**
	* 
	* Returns the attribute value
	* 
	* @return the attribute value
	* 
	*/
	public Object getValue() {
		return getEvent().getValue();
	}

}