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
import javax.servlet.ServletContextEvent;
import org.gservlet.annotation.ContextListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class ServletContextListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/listeners/" + "ServletContextListener.groovy");
		AbstractContextListener listener = (AbstractContextListener) scriptManager.createObject(script);
		assertNotNull(listener);
		assertTrue(listener.getClass().isAnnotationPresent(ContextListener.class));
		final Map<Object, Object> map = new HashMap<Object, Object>();
		ServletContext context = mock(ServletContext.class);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(context).setAttribute(anyString(), any());
		ServletContextEvent event = new ServletContextEvent(context);
		listener.contextInitialized(event);
		assertEquals("contextInitialized", map.get("state"));
		listener.contextDestroyed(event);
		assertEquals("contextDestroyed", map.get("state"));
		assertNotNull(listener.getLogger());
	}

}