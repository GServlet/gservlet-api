package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import org.gservlet.annotation.RequestListener;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class ServletRequestListenerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractRequestListener listener = (AbstractRequestListener) scriptManager.loadScript("ServletRequestListener.groovy");
		assertTrue(listener.getClass().isAnnotationPresent(RequestListener.class));
		assertNotNull(listener);
		final Map<Object,Object> map = new HashMap<Object,Object>();
		Answer initializeMap = new Answer() {
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		      map.put(invocation.getArguments()[0],invocation.getArguments()[1]);
		      return null;
		    }
		};
		ServletContext context = mock(ServletContext.class);
		doAnswer(initializeMap).when(context).setAttribute(anyString(),any());
		ServletRequestEvent event = new ServletRequestEvent(context, mock(HttpServletRequest.class));
		listener.requestInitialized(event);
		assertEquals("requestInitialized", map.get("state"));
		listener.requestDestroyed(event);
		assertEquals("requestDestroyed", map.get("state"));
		assertEquals(RequestWrapper.class, listener.getRequest().getClass());
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
		assertEquals(ContextWrapper.class, listener.getContext().getClass());
	}

}
