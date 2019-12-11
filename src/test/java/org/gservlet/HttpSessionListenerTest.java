package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import org.gservlet.annotation.SessionListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpSessionListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpSessionListener.groovy");
		AbstractSessionListener listener = (AbstractSessionListener) scriptManager.loadScript(script);
		assertTrue(listener.getClass().isAnnotationPresent(SessionListener.class));
		assertNotNull(listener);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		HttpSession session = mock(HttpSession.class);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(session).setAttribute(anyString(), any());
		HttpSessionEvent event = new HttpSessionEvent(session);
		listener.sessionCreated(event);
		assertEquals("sessionCreated", map.get("state"));
		listener.sessionDestroyed(event);
		assertEquals("sessionDestroyed", map.get("state"));
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
	}

}