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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import org.gservlet.annotation.SessionActivationListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpSessionActivationListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/listeners/" + "HttpSessionActivationListener.groovy");
		AbstractSessionActivationListener listener = (AbstractSessionActivationListener) scriptManager.loadObject(script);
		assertNotNull(listener);
		assertTrue(listener.getClass().isAnnotationPresent(SessionActivationListener.class));
		final Map<Object, Object> map = new HashMap<Object, Object>();
		HttpSession session = mock(HttpSession.class);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(session).setAttribute(anyString(), any());
		HttpSessionEvent event = new HttpSessionEvent(session);
		listener.sessionDidActivate(event);
		assertEquals("sessionDidActivate", map.get("state"));
		listener.sessionWillPassivate(event);
		assertEquals("sessionWillPassivate", map.get("state"));
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
		assertNotNull(listener.getLogger());
	}

}