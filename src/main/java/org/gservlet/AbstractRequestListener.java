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
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class AbstractRequestListener extends BaseListener implements ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		route(event, "requestInitialized");
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		route(event, "requestDestroyed");
	}

	public HttpServletRequest getRequest() {
		return new RequestWrapper((HttpServletRequest) getEvent().getServletRequest());
	}

	public HttpSession getSession() {
		return new SessionWrapper(((HttpServletRequest) getEvent().getServletRequest()).getSession(true));
	}

	public ServletContext getContext() {
		return new ContextWrapper(getEvent().getServletContext());
	}

	public ServletRequestEvent getEvent() {
		return (ServletRequestEvent) event;
	}

}