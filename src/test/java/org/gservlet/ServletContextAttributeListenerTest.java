package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import org.gservlet.annotation.ContextAttributeListener;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class ServletContextAttributeListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "ServletContextAttributeListener.groovy");
		AbstractContextAttributeListener listener = (AbstractContextAttributeListener) scriptManager.loadScript(script);
		assertTrue(listener.getClass().isAnnotationPresent(ContextAttributeListener.class));
		assertNotNull(listener);
		ServletContextAttributeEvent event = new ServletContextAttributeEvent(mock(ServletContext.class), "myAttribute", "myValue");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(ContextWrapper.class, listener.getContext().getClass());
	}

}