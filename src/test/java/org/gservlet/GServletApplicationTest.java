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

import static org.gservlet.Constants.DATASOURCE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class GServletApplicationTest {

	@Test
	public void testMethods() throws Exception {
		File folder = new File("src/test/resources/");
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class), isA(Filter.class)))
				.thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class), isA(Servlet.class)))
				.thenReturn(mock(ServletRegistration.Dynamic.class));
		BasicDataSource dataSource = new BasicDataSource();
		when(context.getAttribute(DATASOURCE)).thenReturn(dataSource);
		GServletApplication application = new GServletApplication(context);
		ScriptListener listener = new ScriptListener() {
			@Override
			public void onCreated(Object object) {				
			}
		};
		application.addScriptListener(listener);
		application.addScriptListener(mock(ScriptListener.class));
		application.addScriptListeners(Arrays.asList(mock(ScriptListener.class), mock(ScriptListener.class)));
		assertEquals(4, application.getScriptListeners().size());
		application.start();
		assertEquals(4, application.getContainerManager().getScriptManager().getScriptListeners().size());
		application.addScriptListener(mock(ScriptListener.class));
		application.addScriptListeners(Arrays.asList(mock(ScriptListener.class), mock(ScriptListener.class)));
		assertEquals(7, application.getScriptListeners().size());
		assertEquals(7, application.getContainerManager().getScriptManager().getScriptListeners().size());
		application.setDataSource(dataSource);
		assertEquals(dataSource, application.getDatabaseManager().getDataSource());
	}


}