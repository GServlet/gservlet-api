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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
* 
* Abstract class for receiving notification events about ServletContext lifecycle changes.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractContextListener extends AbstractListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		route(event, "contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		route(event, "contextDestroyed");
	}

	public ServletContext getContext() {
		return new ContextWrapper(getEvent().getServletContext());
	}
	
	public ServletContextEvent getEvent() {
		return (ServletContextEvent) eventHolder.get();
	}

}