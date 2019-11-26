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
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.gservlet.annotation.Servlet;

public class RequestFilter implements javax.servlet.Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		boolean reload = Boolean.parseBoolean(System.getenv(Constants.RELOAD));
		if (reload) {
			ServletContext context = request.getServletContext();
			Map<String, DynamicInvocationHandler> servletHandlers = (Map<String, DynamicInvocationHandler>) context.getAttribute(Constants.HANDLERS);
			for (DynamicInvocationHandler handler : servletHandlers.values()) {
				Object target = handler.getTarget();
				if (target instanceof HttpServlet && !handler.isRegistered()) {
					Servlet annotation = target.getClass().getAnnotation(Servlet.class);
					String path = annotation.value()[0];
					if (httpServletRequest.getRequestURI().endsWith(path)) {
						HttpServlet servlet = (HttpServlet) target;
						String method = httpServletRequest.getMethod();
						if (method.equalsIgnoreCase("get")) {
							servlet.doGet(httpServletRequest, httpServletResponse);
							return;
						} else if (method.equalsIgnoreCase("post")) {
							servlet.doPost(httpServletRequest, httpServletResponse);
							return;
						}
						else if (method.equalsIgnoreCase("put")) {
							servlet.doPut(httpServletRequest, httpServletResponse);
							return;
						}
						else if (method.equalsIgnoreCase("delete")) {
							servlet.doDelete(httpServletRequest, httpServletResponse);
							return;
						}
						else if (method.equalsIgnoreCase("head")) {
							servlet.doHead(httpServletRequest, httpServletResponse);
							return;
						}
						else if (method.equalsIgnoreCase("trace")) {
							servlet.doTrace(httpServletRequest, httpServletResponse);
							return;
						}
						else if (method.equalsIgnoreCase("options")) {
							servlet.doOptions(httpServletRequest, httpServletResponse);
							return;
						}
					}
				}
			}
			
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}