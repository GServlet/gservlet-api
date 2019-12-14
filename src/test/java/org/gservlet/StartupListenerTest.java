package org.gservlet;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import org.junit.Test;

public class StartupListenerTest {

	@Test
	public void test() {
		File folder = new File("src/test/resources/");
		StartupListener listener = new StartupListener();
		assertEquals(true, listener.getClass().isAnnotationPresent(WebListener.class));
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class),isA(Filter.class))).thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class),isA(Servlet.class))).thenReturn(mock(ServletRegistration.Dynamic.class));
		ServletContextEvent event = new ServletContextEvent(context);
		listener.contextInitialized(event);
		assertNotNull(listener.getInitializer());
		assertEquals(11, listener.getInitializer().getHandlers().size());
		assertNotNull(listener.getDatabaseManager());
	}

}
