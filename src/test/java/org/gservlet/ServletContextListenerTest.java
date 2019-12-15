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
		AbstractContextListener listener = (AbstractContextListener) scriptManager.loadScript(script);
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