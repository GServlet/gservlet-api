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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
* 
* Abstract class for receiving notification events about requests coming into and going out of scope of a web application
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractRequestListener extends AbstractListener implements ServletRequestListener {

	/**
	* 
	* Receives notification that a ServletRequest is about to come into scope of the web application. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the ServletRequestEvent containing the ServletRequest and the ServletContext representing the web application
	* 
	*/
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		invoke("requestInitialized", event);
	}

	/**
	* 
	* Receives notification that a ServletRequest is about to go out of scope of the web application. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the ServletRequestEvent containing the ServletRequest and the ServletContext representing the web application
	* 
	*/
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		invoke("requestDestroyed", event);
	}

	/**
	* 
	* The HttpServletRequest object
	* 
	* @return the HttpServletRequest object
	* 
	*/
	public HttpServletRequest getRequest() {
		return new RequestWrapper((HttpServletRequest) getEvent().getServletRequest());
	}

	/**
	* 
	* The HttpSession object
	* 
	* @return the HttpSession object
	* 
	*/
	public HttpSession getSession() {
		return new SessionWrapper(((HttpServletRequest) getEvent().getServletRequest()).getSession(true));
	}

	/**
	* 
	* Returns the ServletContext object
	* 
	* @return the ServletContext object
	* 
	*/
	public ServletContext getContext() {
		return new ContextWrapper(getEvent().getServletContext());
	}

	/**
	* 
	* Returns the ServletRequestEvent object
	* 
	* @return the ServletRequestEvent object
	* 
	*/
	public ServletRequestEvent getEvent() {
		return (ServletRequestEvent) eventHolder.get();
	}

}