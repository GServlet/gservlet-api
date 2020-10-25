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
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import org.junit.Test;
import groovy.util.ScriptException;

public class StartupListenerTest {

	@Test(expected = Exception.class)
	public void test() throws InterruptedException, IOException, ServletException, ScriptException {
		File folder = new File("src/test/resources/");
		StartupListener listener = new StartupListener();
		assertEquals(true, listener.getClass().isAnnotationPresent(WebListener.class));
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class), isA(Filter.class)))
				.thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class), isA(Servlet.class)))
				.thenReturn(mock(ServletRegistration.Dynamic.class));
		ServletContextEvent event = new ServletContextEvent(context);
		listener.contextInitialized(event);
		assertNotNull(listener.getApplication().getInitializer());
		assertEquals(11, listener.getApplication().getInitializer().getHandlers().size());
		assertNotNull(listener.getApplication().getDatabaseManager());
		wait(2000);
		File configuration = new File(folder + File.separator + APP_CONFIG_FILE);
		byte[] bytes = Files.readAllBytes(Paths.get(configuration.getAbsolutePath()));
		configuration.delete();
		wait(2000);
		Files.write(Paths.get(configuration.getAbsolutePath()), bytes);
		wait(2000);
		listener.contextDestroyed(event);
		assertEquals(0, listener.getApplication().getInitializer().getHandlers().size());
		when(context.getFilterRegistration(isA(String.class)))
		.thenReturn(mock(FilterRegistration.Dynamic.class));
		folder = new File(folder + File.separator + SCRIPTS_FOLDER);
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + File.separator + "HttpFilter.groovy");
		listener.getApplication().getInitializer().register(scriptManager.loadObject(script));
	}
	
	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}