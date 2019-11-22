package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
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
		ServletRequestListener listener = (ServletRequestListener) scriptManager.loadScript("ServletRequestListener.groovy");
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
		ServletRequestEvent event = new ServletRequestEvent(context, mock(ServletRequest.class));
		listener.requestInitialized(event);
		assertEquals("requestInitialized", map.get("state1"));
		listener.requestDestroyed(event);
		assertEquals("requestDestroyed", map.get("state2"));
		assertNotNull(map.get("contextWrapper"));
		
	}

}
