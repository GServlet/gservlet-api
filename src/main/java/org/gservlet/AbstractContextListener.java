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

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
* 
* Abstract class for receiving notification events about ServletContext lifecycle changes
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractContextListener extends AbstractListener implements ServletContextListener {

	/**
	* 
	* Receives notification that the web application initialization process is starting. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the ServletContextEvent containing the ServletContext that is being initialized
	* 
	*/
	@Override
	public void contextInitialized(ServletContextEvent event) {
		invoke("contextInitialized", event);
	}

	/**
	* 
	* Receives notification that the ServletContext is about to be shutdown. The call
	* will be forwarded to the same method with no arguments defined on the subclasses
	* 
	* @param event the ServletContextEvent containing the ServletContext that is being initialized
	* 
	*/
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		invoke("contextDestroyed", event);
	}

	/**
	* 
	* Returns the ServletContext object.
	* 
	* @return the ServletContext object
	* 
	*/
	public ServletContext getContext() {
		return new ServletContextWrapper(getEvent().getServletContext());
	}
	
	/**
	* 
	* Returns the ServletContextEvent object
	* 
	* @return the ServletContextEvent object
	* 
	*/
	public ServletContextEvent getEvent() {
		return (ServletContextEvent) eventHolder.get();
	}

}