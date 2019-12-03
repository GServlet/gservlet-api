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
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import groovy.sql.Sql;

/**
* 
* The DefaultRequestListener handles the creation and the closing of a database connection.   
* 
* @author Mamadou Lamine Ba
* 
*/
@WebListener
public class DefaultRequestListener implements ServletRequestListener {

	/**
	* 
	* Receives notification that a ServletRequest is about to come into scope of the web application
	* 
	* @param event the ServletRequestEvent containing the ServletRequest and the ServletContext representing the web application
	* 
	*/
	@Override
	public void requestInitialized(ServletRequestEvent event) {
		ServletRequest request = event.getServletRequest();
		ServletContext context = event.getServletContext();
		request.setAttribute(Constants.CONNECTION, new Sql((DataSource) context.getAttribute(Constants.DATASOURCE)));
	}

	/**
	* 
	* Receives notification that a ServletRequest is about to go out of scope of the web application
	* 
	* @param event the ServletRequestEvent containing the ServletRequest and the ServletContext representing the web application
	* 
	*/
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		ServletRequest request = event.getServletRequest();
		Sql connection = (Sql) request.getAttribute(Constants.CONNECTION);
		connection.close();
	}

}