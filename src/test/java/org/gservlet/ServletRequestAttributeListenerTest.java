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
		File folder = new File("src/test/resources/" + Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "ServletRequestAttributeListener.groovy");
		AbstractRequestAttributeListener listener = (AbstractRequestAttributeListener) scriptManager.loadScript(script);
		assertTrue(listener.getClass().isAnnotationPresent(RequestAttributeListener.class));
		assertNotNull(listener);
		ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(mock(ServletContext.class),
				mock(HttpServletRequest.class), "myAttribute", "myValue");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(RequestWrapper.class, listener.getRequest().getClass());
		assertEquals(ContextWrapper.class, listener.getContext().getClass());
	}

}
