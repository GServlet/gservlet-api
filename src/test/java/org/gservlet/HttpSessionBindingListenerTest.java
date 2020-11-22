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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import org.gservlet.annotation.SessionBindingListener;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class HttpSessionBindingListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/listeners/" + "HttpSessionBindingListener.groovy");
		AbstractSessionBindingListener listener = (AbstractSessionBindingListener) scriptManager.createObject(script);
		assertNotNull(listener);
		assertTrue(listener.getClass().isAnnotationPresent(SessionBindingListener.class));
		HttpSessionBindingEvent event = new HttpSessionBindingEvent(mock(HttpSession.class), "myAttribute", "myValue");
		listener.valueBound(event);
		assertEquals("valueBound", listener.getName());
		listener.valueUnbound(event);
		assertEquals("valueUnbound", listener.getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
		assertNotNull(listener.getLogger());
	}

}