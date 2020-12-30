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
import static org.gservlet.Constants.HANDLERS;
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
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.gservlet.annotation.Filter;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import groovy.sql.Sql;

public class HttpFilterTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testFilter() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpFilter.groovy");
		AbstractFilter filter = (AbstractFilter) scriptManager.createObject(script);
		assertNotNull(filter);
		assertTrue(filter.getClass().isAnnotationPresent(Filter.class));
		Filter annotation = filter.getClass().getAnnotation(Filter.class);
		assertEquals(1, annotation.urlPatterns().length);
		assertEquals(2, annotation.initParams().length);
		assertEquals("HttpFilter", filter.getClass().getName());
		assertEquals("/*", annotation.value()[0]);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(DB_CONNECTION)).thenReturn(new Sql(mock(DataSource.class)));
		ServletContext context = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(context);
		when(request.getSession(true)).thenReturn(mock(HttpSession.class));
		final Map<String, DynamicInvocationHandler> handlers = new HashMap<>();
		when(context.getAttribute(HANDLERS)).thenReturn(handlers);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(request).setAttribute(anyString(), any());
		filter.doFilter(request, mock(HttpServletResponse.class), mock(FilterChain.class));
		assertEquals("filtering", map.get("state"));
		DefaultFilterConfig config = new DefaultFilterConfig();
		FilterConfigWrapper configWrapper = new FilterConfigWrapper(config);
		config.addInitParameter("param1", "paramValue1");
		config.addInitParameter("param2", "paramValue2");
		filter.init(configWrapper);
		assertEquals(configWrapper.getFilterName(), config.getClass().getName());
		assertEquals(2, Collections.list(filter.getConfig().getInitParameterNames()).size());
		assertEquals("paramValue1", filter.getConfig().getInitParameter("param1"));
		assertEquals("paramValue2", filter.getConfig().getInitParameter("param2"));
		assertEquals("paramValue2", configWrapper.propertyMissing("param2"));
		assertNull(filter.getConfig().getServletContext());
		assertEquals("init", map.get("state"));
		assertNotNull(filter.getConfig());
		filter.destroy();
		assertEquals("destroy", map.get("state"));
		assertEquals(RequestWrapper.class, filter.getRequest().getClass());
		assertEquals(SessionWrapper.class, filter.getSession().getClass());
		assertEquals(ServletContextWrapper.class, filter.getContext().getClass());
		assertNotNull(filter.getChain());
		assertNotNull(filter.getSql());
		filter.next();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testOutput() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpFilter.groovy");
		AbstractFilter filter = (AbstractFilter) scriptManager.createObject(script);
		assertNotNull(filter);
		assertTrue(filter.getClass().isAnnotationPresent(Filter.class));
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		filter.doFilter(request, response, mock(FilterChain.class));
		assertNotNull(filter.getOut());
		assertNotNull(filter.getHtml());
		assertNotNull(filter.getXml());
		assertEquals("<!DOCTYPE html>", out.toString().trim());
		out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		filter.json(map);
		assertEquals("{\"key\":\"value\"}", out.toString());
		assertEquals("{\"key\":\"value\"}", filter.stringify(map));
		Map object = (Map) filter.parse(new ByteArrayInputStream(out.toString().getBytes()));
		assertNotNull(object);
		assertEquals("value", object.get("key"));
		assertNotNull(filter.getLogger());
	}

}