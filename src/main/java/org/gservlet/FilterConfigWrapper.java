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
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * 
 * A wrapper class around the FilterConfig interface
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class FilterConfigWrapper implements FilterConfig {

	/**
	 * The filter config object
	 */
	private final FilterConfig filterConfig;

	/**
	 * 
	 * Constructs a FilterConfigWrapper for the given FilterConfig
	 * 
	 * @param filterConfig the filter config object
	 * 
	 */
	public FilterConfigWrapper(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	/**
	 * Returns the name of the filter instance.
	 *
	 * @return the name of the filter instance
	 * 
	 */
	@Override
	public String getFilterName() {
		return filterConfig.getFilterName();
	}

	/**
	 * Returns a reference to the ServletContext
	 *
	 * @return the ServletContext object
	 * 
	 * 
	 */
	@Override
	public ServletContext getServletContext() {
		return filterConfig.getServletContext();
	}

	/**
	 * Gets the value of the initialization parameter with the given name.
	 *
	 * @param name the name of the initialization parameter whose value to get
	 *
	 * @return a <code>String</code> containing the value of the initialization
	 *         parameter, or <code>null</code> if it does not exist
	 *         
	 */
	@Override
	public String getInitParameter(String name) {
		return filterConfig.getInitParameter(name);
	}

	/**
	 * Returns the names of the filter's initialization parameters as an
	 * <code>Enumeration</code> of <code>String</code> objects, or an empty
	 * <code>Enumeration</code> if the filter has no initialization parameters.
	 *
	 * @return an <code>Enumeration</code> of <code>String</code> objects containing
	 *         the names of the filter's initialization parameters
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return filterConfig.getInitParameterNames();
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
		return filterConfig.getInitParameter(name);
	}

}