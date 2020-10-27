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

import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 
 * A wrapper class around the ServletConfig interface.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class ServletConfigWrapper implements ServletConfig {

	/**
	 * The servlet config object
	 */
	private final ServletConfig servletConfig;

	/**
	 * 
	 * Constructs a ServletConfigWrapper for the given ServletConfig
	 * 
	 * @param servletConfig the servlet config object
	 * 
	 */
	public ServletConfigWrapper(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	/**
	 * Returns the name of this servlet instance. The name may be provided via
	 * server administration, assigned in the web application deployment descriptor,
	 * or for an unregistered (and thus unnamed) servlet instance it will be the
	 * servlet's class name.
	 *
	 * @return the name of the servlet instance
	 * 
	 */
	@Override
	public String getServletName() {
		return servletConfig.getServletName();
	}

	/**
	 * Returns a reference to the ServletContext in which the caller is executing.
	 *
	 * @return a ServletContext object, used by the caller to interact with its servlet container
	 * 
	 * 
	 */
	@Override
	public ServletContext getServletContext() {
		return servletConfig.getServletContext();
	}

	/**
	 * Gets the value of the initialization parameter with the given name.
	 *
	 * @param name the name of the initialization parameter whose value to get
	 *
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if the initialization parameter does
	 *         not exist
	 */
	@Override
	public String getInitParameter(String name) {
		return servletConfig.getInitParameter(name);
	}

	/**
	 * Returns the names of the servlet's initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the servlet has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the servlet's initialization parameters
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return servletConfig.getInitParameterNames();
	}

	/**
	 * 
	 * Gets the value of the initialization parameter with the given name.
	 * 
	 * @param name the name of the initialization parameter whose value to get
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if the initialization parameter does
	 *         not exist
	 */
	public Object propertyMissing(String name) {
		return servletConfig.getInitParameter(name);
	}

}