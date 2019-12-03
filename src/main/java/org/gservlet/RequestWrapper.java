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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import groovy.json.JsonSlurper;

/**
* 
* A wrapper around the HttpServletRequest class.
* 
* @author Mamadou Lamine Ba
* 
*/
public class RequestWrapper extends HttpServletRequestWrapper {

	/**
	* 
	* Constructs a RequestWrapper for the given HttpServletRequest.
	* 
	* @param request the request object 
	*  
	*/
	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	* 
	* Sets an attribute.
	* 
	* @param name the attribute name
	* @param value the attribute value
	* 
	*/
	public void propertyMissing(String name, Object value) {
		setAttribute(name, value);
	}

	/**
	* 
	* Gets an attribute or a parameter value.
	* 
	* @param name the attribute or parameter name
	* @return the attribute or parameter value
	* @throws IOException the IOException
	*/
	public Object propertyMissing(String name) throws IOException {
		if (name != null && name.equals("body") && getContentType().equalsIgnoreCase("application/json")) {
			return new JsonSlurper().parse(getInputStream());
		}
		Object value = getAttribute(name);
		return value != null ? value : getParameter(name);
	}

}