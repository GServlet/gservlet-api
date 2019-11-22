package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class ServletContextAttributeListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		ServletContextAttributeListener listener = (ServletContextAttributeListener) scriptManager.loadScript("ServletContextAttributeListener.groovy");
		assertNotNull(listener);
		ServletContextAttributeEvent event = new ServletContextAttributeEvent(mock(ServletContext.class), "myAttribute", "myValue");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getEvent().getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getEvent().getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getEvent().getName());
	}

}
