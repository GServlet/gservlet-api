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

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.dbcp2.BasicDataSource;
import org.gservlet.annotation.RequestListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import groovy.sql.Sql;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class ServletRequestListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/listeners/" + "ServletRequestListener.groovy");
		AbstractRequestListener listener = (AbstractRequestListener) scriptManager.loadObject(script);
		assertNotNull(listener);
		assertTrue(listener.getClass().isAnnotationPresent(RequestListener.class));
		final Map<Object, Object> map = new HashMap<Object, Object>();
		ServletContext context = mock(ServletContext.class);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(context).setAttribute(anyString(), any());
		ServletRequestEvent event = new ServletRequestEvent(context, mock(HttpServletRequest.class));
		listener.requestInitialized(event);
		assertEquals("requestInitialized", map.get("state"));
		listener.requestDestroyed(event);
		assertEquals("requestDestroyed", map.get("state"));
		assertEquals(RequestWrapper.class, listener.getRequest().getClass());
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
		assertEquals(ContextWrapper.class, listener.getContext().getClass());
		assertNotNull(listener.getLogger());
	}

	@Test
	public void testDefaultListener() {
		DefaultRequestListener listener = new DefaultRequestListener();
		assertTrue(listener.getClass().isAnnotationPresent(WebListener.class));
		ServletContext context = mock(ServletContext.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(DB_CONNECTION)).thenReturn(new Sql(new BasicDataSource()));
		ServletRequestEvent event = new ServletRequestEvent(context, request);
		listener.requestInitialized(event);
		listener.requestDestroyed(event);
	}

}