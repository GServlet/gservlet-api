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
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextAttributeEvent;
import org.gservlet.annotation.ContextAttributeListener;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class ServletContextAttributeListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/listeners/" + "ServletContextAttributeListener.groovy");
		AbstractContextAttributeListener listener = (AbstractContextAttributeListener) scriptManager.createObject(script);
		assertNotNull(listener);
		assertTrue(listener.getClass().isAnnotationPresent(ContextAttributeListener.class));
		ServletContextAttributeEvent event = new ServletContextAttributeEvent(mock(ServletContext.class), "myAttribute", "myValue");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(ServletContextWrapper.class, listener.getContext().getClass());
		assertNotNull(listener.getLogger());
	}

}