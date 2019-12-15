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
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.gservlet.annotation.Servlet;

/**
* 
* The DefaultRequestFilter handles the request forwarding to a newly created servlet.
* 
* @author Mamadou Lamine Ba
* 
*/
public class DefaultRequestFilter implements Filter {

	/**
	* 
	* Called by the web container to indicate that it is being placed into service
	* @param config the filter config
	* @throws ServletException the ServletException
	* 
	*/
	@Override
	public void init(FilterConfig config) throws ServletException {
		// implementation not required
	}

	/**
	* 
	* Called by the web container each time a request/response pair is passed through the chain due to a client request for a resource at the end of the chain
	* @param request the request
	* @param response the response
	* @param chain the filter chain
	* @throws IOException the IOException 
	* @throws ServletException the ServletException
	* 
	*/
	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		boolean reload = Boolean.parseBoolean(System.getenv(Constants.RELOAD));
		if (reload) {
			ServletContext context = request.getServletContext();
			Map<String, DynamicInvocationHandler> servletHandlers = (Map<String, DynamicInvocationHandler>) context
					.getAttribute(Constants.HANDLERS);
			for (DynamicInvocationHandler handler : servletHandlers.values()) {
				Object target = handler.getTarget();
				if (target instanceof AbstractServlet && !handler.isRegistered()) {
					Servlet annotation = target.getClass().getAnnotation(Servlet.class);
					String path = annotation.value()[0];
					if (httpServletRequest.getRequestURI().endsWith(path)) {
						AbstractServlet servlet = (AbstractServlet) target;
						String method = httpServletRequest.getMethod();
						servlet.service(httpServletRequest, httpServletResponse, method.toLowerCase());
					}
				}
			}

		}
		chain.doFilter(request, response);
	}

	/**
	* 
	* Called by the web container to indicate that it is being taken out of service
	* 
	*/
	@Override
	public void destroy() {
		// implementation not required
	}
}