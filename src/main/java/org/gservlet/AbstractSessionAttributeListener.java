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
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
* 
* Abstract class for receiving notification events about HttpSession attribute changes.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractSessionAttributeListener extends AbstractListener implements HttpSessionAttributeListener {

	/**
	* 
	* Receives notification that an attribute has been added to the HttpSession. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the HttpSessionBindingEvent containing the HttpSession to which the attribute was added, along with the attribute name and value
	* 
	*/
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		invoke("attributeAdded", event);
	}

	/**
	* 
	* Receives notification that an attribute has been removed to the HttpSession. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the HttpSessionBindingEvent containing the HttpSession to which the attribute was added, along with the attribute name and value
	* 
	*/
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		invoke("attributeRemoved", event);
	}

	/**
	* 
	* Receives notification that an attribute has been replaced to the HttpSession. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the HttpSessionBindingEvent containing the HttpSession to which the attribute was added, along with the attribute name and value
	* 
	*/
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		invoke("attributeReplaced", event);
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