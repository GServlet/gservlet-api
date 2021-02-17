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

import static org.gservlet.Constants.SCRIPTS_FOLDER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.junit.Test;

public class ContainerManagerTest {

	@Test(expected = ServletException.class)
	public void init() throws Exception {
		File folder = new File("src/test/resources");
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class), isA(Filter.class)))
				.thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class), isA(Servlet.class)))
				.thenReturn(mock(ServletRegistration.Dynamic.class));
		ContainerManager manager = new ContainerManager(context);
		manager.init("src/test/resources", new ArrayList<ScriptListener>());
		assertEquals(9, manager.getHandlers().size());
		manager.getHandlers().values().forEach( handler -> assertNotNull(handler.getTarget()));
		wait(2000);
		manager.process(new File(folder + "/groovy/listeners/" + "ServletContextListener.groovy"));
		manager.process(new File(folder + "/groovy/" + "HttpFilter.groovy"));
		manager.process(new File(folder + "/groovy/" + "HttpServlet.groovy"));
		manager.process(new File(folder + "/groovy/" + "InvocationHandler.groovy"));
		assertEquals(9, manager.getHandlers().size());
		File file = new File(folder + "/groovy/script.groovy");
		try(PrintWriter printWriter = new PrintWriter(new FileWriter(file))) {
			printWriter.println("import org.gservlet.annotation.Servlet");
			printWriter.println("@Servlet(\"/servlet\")");
			printWriter.println("class HttpServlet {}");
		}
		wait(2000);
		assertEquals(9, manager.getHandlers().size());
		file.delete();
		manager.stop();
		assertEquals(0, manager.getHandlers().size());
		when(context.getServletRegistration(isA(String.class)))
		.thenReturn(mock(ServletRegistration.Dynamic.class));
		folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpServlet.groovy");
		manager.register(scriptManager.createObject(script));
	}
	
	@Test
	public void testDynamicInvocationHandler() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "InvocationHandler.groovy");
		Object object = scriptManager.createObject(script);
		DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
		handler.setTarget(object);
		FileFilter proxy = (FileFilter) Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { FileFilter.class }, handler);
		assertTrue(proxy.accept(script));
	}

	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}