package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import org.gservlet.annotation.SessionIdListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpSessionIdListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpSessionIdListener.groovy");
		AbstractSessionIdListener listener = (AbstractSessionIdListener) scriptManager.loadScript(script);
		assertTrue(listener.getClass().isAnnotationPresent(SessionIdListener.class));
		assertNotNull(listener);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		HttpSession session = mock(HttpSession.class);
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(session).setAttribute(anyString(), any());
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return map.get(invocation.getArguments()[0]);
			}
		}).when(session).getAttribute(anyString());
		HttpSessionEvent event = new HttpSessionEvent(session);
		listener.sessionIdChanged(event, "34346FGG7677");
		assertEquals("sessionIdChanged", map.get("state"));
		assertEquals("34346FGG7677", listener.getOldSessionId());
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
	}

}