package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.http.HttpServletRequest;
import org.gservlet.annotation.RequestAttributeListener;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class ServletRequestAttributeListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractRequestAttributeListener listener = (AbstractRequestAttributeListener) scriptManager.loadScript("ServletRequestAttributeListener.groovy");
		assertTrue(listener.getClass().isAnnotationPresent(RequestAttributeListener.class));
		assertNotNull(listener);
		ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(mock(ServletContext.class), mock(HttpServletRequest.class), "myAttribute", "myValue");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getEvent().getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getEvent().getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getEvent().getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(RequestWrapper.class, listener.getRequest().getClass());
		assertEquals(ContextWrapper.class, listener.getContext().getClass());
	}

}
