package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class HttpSessionAttributeListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractSessionAttributeListener listener = (AbstractSessionAttributeListener) scriptManager.loadScript("HttpSessionAttributeListener.groovy");
		assertNotNull(listener);
		HttpSessionBindingEvent event = new HttpSessionBindingEvent(mock(HttpSession.class), "myAttribute");
		listener.attributeAdded(event);
		assertEquals("attributeAdded", listener.getEvent().getName());
		listener.attributeRemoved(event);
		assertEquals("attributeRemoved", listener.getEvent().getName());
		listener.attributeReplaced(event);
		assertEquals("attributeReplaced", listener.getEvent().getName());
	}

}
