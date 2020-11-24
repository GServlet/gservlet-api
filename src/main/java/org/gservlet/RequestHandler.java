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

import static groovy.json.JsonOutput.toJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import groovy.json.JsonSlurper;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

/**
 * 
 * Interface for handling requests
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public interface RequestHandler {

	/**
	 * 
	 * Converts the object to JSON
	 * 
	 * @param object the object
	 * @return the JSON output
	 * 
	 */
	default public String stringify(Object object) {
		return toJson(object);
	}

	/**
	 * 
	 * Parses the input stream to JSON
	 * 
	 * @param inputStream the input stream
	 * @return the JSON output
	 * 
	 */
	default public Object parse(InputStream inputStream) {
		return new JsonSlurper().parse(inputStream);
	}
	
	
	/**
	 * 
	 * Sends the response as JSON
	 * 
	 * @param response the response object
	 * @throws IOException the IOException
	 * 
	 */
	default public void json(Object response) throws IOException {
		getResponse().setHeader("Content-Type", "application/json");
		getResponse().getWriter().write(toJson(response));
	}

	/**
	 * 
	 * Returns the RequestContext object
	 * 
	 * @return the RequestContext object
	 * 
	 */
	public RequestContext getRequestContext();
	
	
	/**
	 * 
	 * Returns the HttpServletRequest object
	 * 
	 * @return the HttpServletRequest object
	 * 
	 */
	default public HttpServletRequest getRequest() {
		return getRequestContext().getRequest();
	}

	/**
	 * 
	 * Returns the HttpSession object
	 * 
	 * @return the HttpSession object
	 * 
	 */
	default public HttpSession getSession() {
		return getRequestContext().getSession();
	}
	
	/**
	 * 
	 * Returns the HttpServletResponse object
	 * 
	 * @return the HttpServletResponse object
	 * 
	 */
	default public HttpServletResponse getResponse() {
		return getRequestContext().getResponse();
	}

	/**
	 * 
	 * Returns the Sql object
	 * 
	 * @return the Sql object
	 * 
	 */
	default public Sql getSql() {
		return getRequestContext().getSql();
	}

	/**
	 * 
	 * Returns the PrintWriter object
	 * 
	 * @return the PrintWriter object
	 * @throws IOException the IOException
	 * 
	 */
	default public PrintWriter getOut() throws IOException {
		return getResponse().getWriter();
	}

	/**
	 * 
	 * Returns the MarkupBuilder object for producing HTML content
	 * 
	 * @return the MarkupBuilder object
	 * @throws IOException the IOException
	 * 
	 */
	default public MarkupBuilder getHtml() throws IOException {
		return getRequestContext().getHtml();
	}
	
	/**
	 * 
	 * Returns the MarkupBuilder object for producing XML content
	 * 
	 * @return the MarkupBuilder object
	 * @throws IOException the IOException
	 * 
	 */
	default public MarkupBuilder getXml() throws IOException {
		return getRequestContext().getXml();
	}
}