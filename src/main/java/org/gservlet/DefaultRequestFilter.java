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

public class DefaultRequestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// implementation not required
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
			Map<String, DynamicInvocationHandler> servletHandlers = (Map<String, DynamicInvocationHandler>) context
					.getAttribute(Constants.HANDLERS);
			for (DynamicInvocationHandler handler : servletHandlers.values()) {
				Object target = handler.getTarget();
				if (target instanceof AbstractServlet && !handler.isRegistered()) {
					Servlet annotation = target.getClass().getAnnotation(Servlet.class);
					String path = annotation.value()[0];
					if (httpServletRequest.getRequestURI().endsWith(path)) {
						forward((AbstractServlet) target, httpServletRequest, httpServletResponse);
					}
				}
			}

		}
		chain.doFilter(request, response);
	}

	protected void forward(AbstractServlet servlet, HttpServletRequest request, HttpServletResponse response) {
		servlet.route(request, response, request.getMethod().toLowerCase());
	}

	@Override
	public void destroy() {
		// implementation not required
	}
}