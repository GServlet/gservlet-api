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

import static org.gservlet.Constants.DB_CONNECTION;
import static org.gservlet.Constants.SCRIPTS_FOLDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.gservlet.annotation.Servlet;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

public class HttpServletTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testMethods() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpServlet.groovy");
		AbstractServlet servlet = (AbstractServlet) scriptManager.createObject(script);
		assertNotNull(servlet);
		assertTrue(servlet.getClass().isAnnotationPresent(Servlet.class));
		Servlet annotation = servlet.getClass().getAnnotation(Servlet.class);
		assertEquals("HttpServlet", servlet.getClass().getName());
		assertEquals(AbstractServlet.class, servlet.getClass().getSuperclass());
		assertEquals("/servlet", annotation.value()[0]);
		assertEquals(1, annotation.urlPatterns().length);
		assertEquals(2, annotation.initParams().length);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getSession(true)).thenReturn(mock(HttpSession.class));
		when(request.getAttribute(DB_CONNECTION)).thenReturn(new Sql(mock(DataSource.class)));
		when(request.getServletContext()).thenReturn(mock(ServletContext.class));
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(request).setAttribute(anyString(), any());
		
		DefaultServletConfig config = new DefaultServletConfig();
		ServletConfigWrapper configWrapper = new ServletConfigWrapper(config);
		config.addInitParameter("param1", "paramValue1");
		config.addInitParameter("param2", "paramValue2");
		servlet.init(configWrapper);
		assertEquals(config.getServletName(), config.getClass().getName());
		assertEquals(2, Collections.list(servlet.getConfig().getInitParameterNames()).size());
		assertEquals("paramValue1", servlet.getConfig().getInitParameter("param1"));
		assertEquals("paramValue2", servlet.getConfig().getInitParameter("param2"));
		assertEquals("paramValue2", configWrapper.propertyMissing("param2"));
		assertNull(servlet.getConfig().getServletContext());
		HttpServletResponse response = mock(HttpServletResponse.class);
		servlet.doGet(request, response);
		assertEquals("get", map.get("state"));
		servlet.doPost(request, response);
		assertEquals("post", map.get("state"));
		servlet.doPut(request, response);
		assertEquals("put", map.get("state"));
		servlet.doDelete(request, response);
		assertEquals("delete", map.get("state"));
		servlet.doOptions(request, response);
		assertEquals("options", map.get("state"));
		servlet.doTrace(request, response);
		assertEquals("trace", map.get("state"));
		servlet.doHead(request, response);
		assertEquals("head", map.get("state"));
		assertEquals(RequestWrapper.class, servlet.getRequest().getClass());
		assertEquals(SessionWrapper.class, servlet.getSession().getClass());
		assertEquals(ServletContextWrapper.class, servlet.getContext().getClass());
		assertNotNull(servlet.getConfig());
		assertNotNull(servlet.getSql());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOutput() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpServlet.groovy");
		AbstractServlet servlet = (AbstractServlet) scriptManager.createObject(script);
		assertNotNull(servlet);
		assertTrue(servlet.getClass().isAnnotationPresent(Servlet.class));
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		servlet.doGet(request, response);
		assertNotNull(servlet.getOut());
		MarkupBuilder builder = servlet.getHtml();
		assertNotNull(builder);
		assertEquals("<!DOCTYPE html>", out.toString().trim());
		out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		servlet.json(map);
		assertEquals("{\"key\":\"value\"}", out.toString());
		assertEquals("{\"key\":\"value\"}", servlet.stringify(map));
		Map object = (Map) servlet.parse(new ByteArrayInputStream(out.toString().getBytes()));
		assertNotNull(object);
		assertEquals("value", object.get("key"));
		assertNotNull(servlet.getLogger());
	}

}