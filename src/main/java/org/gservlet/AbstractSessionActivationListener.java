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
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

/**
* 
* Abstract class for receiving notification events about sessions being activated and passivated.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractSessionActivationListener extends AbstractListener implements HttpSessionActivationListener {

	/**
	* 
	* Receives notification that the session has just been activated. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the HttpSessionEvent containing the session
	* 
	*/
	@Override
	public void sessionDidActivate(HttpSessionEvent event) {
		invoke("sessionDidActivate", event);
	}

	/**
	* 
	* Receives notification that the session is about to be passivated. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the HttpSessionEvent containing the session
	* 
	*/
	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {
		invoke("sessionWillPassivate", event);
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

}