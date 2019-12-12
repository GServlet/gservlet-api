package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import org.gservlet.annotation.SessionBindingListener;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class HttpSessionBindingListenerTest {

	@Test
	public void testEvents() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpSessionBindingListener.groovy");
		AbstractSessionBindingListener listener = (AbstractSessionBindingListener) scriptManager.loadScript(script);
		assertTrue(listener.getClass().isAnnotationPresent(SessionBindingListener.class));
		assertNotNull(listener);
		HttpSessionBindingEvent event = new HttpSessionBindingEvent(mock(HttpSession.class), "myAttribute", "myValue");
		listener.valueBound(event);
		assertEquals("valueBound", listener.getName());
		listener.valueUnbound(event);
		assertEquals("valueUnbound", listener.getName());
		assertEquals("myValue", listener.getValue());
		assertEquals(SessionWrapper.class, listener.getSession().getClass());
	}

}