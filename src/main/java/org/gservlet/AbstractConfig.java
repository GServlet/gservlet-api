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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletContext;

/**
* 
* Abstract base class inherited by the filters and servlets default config classes
* 
* @author Mamadou Lamine Ba
* 
*/

public class AbstractConfig {

	/**
	 * The initialization parameters Map
	 */
	protected final Map<String, String> parameters = new HashMap<>();
	
	/**
	 * The servlet context
	 */
	private ServletContext servletContext;
	

	/**
	 * Returns a reference to the ServletContext 
	 *
	 * @return the ServletContext object
	 * 
	 * 
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	/**
	 * 
	 * Sets the servlet context
	 * 
	 * @param servletContext the servlet context
	 * 
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}


	/**
	 * 
	 * Sets an initialization parameter
	 * 
	 * @param name  the initialization parameter name
	 * @param value the initialization parameter value
	 * 
	 */
	public void addInitParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	/**
	 * Gets the value of the initialization parameter with the given name.
	 *
	 * @param name the name of the initialization parameter
	 *
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if it does not exist
	 *         
	 */
	public String getInitParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Returns the names of the initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the servlet or filter has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the initialization parameters
	 */
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

}
