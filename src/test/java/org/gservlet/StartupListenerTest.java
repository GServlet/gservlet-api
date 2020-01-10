package org.gservlet;

import static org.gservlet.Constants.SCRIPTS_FOLDER;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import org.junit.Test;

import groovy.util.ScriptException;

public class StartupListenerTest {

	@Test(expected = ServletException.class)
	public void test() throws InterruptedException, IOException, ServletException, ScriptException {
		File folder = new File("src/test/resources/");
		StartupListener listener = new StartupListener();
		assertEquals(true, listener.getClass().isAnnotationPresent(WebListener.class));
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class), isA(Filter.class)))
				.thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class), isA(Servlet.class)))
				.thenReturn(mock(ServletRegistration.Dynamic.class));
		ServletContextEvent event = new ServletContextEvent(context);
		listener.contextInitialized(event);
		assertNotNull(listener.getInitializer());
		assertEquals(11, listener.getInitializer().getHandlers().size());
		assertNotNull(listener.getDatabaseManager());
		wait(2000);
		File conf = new File(folder + "/conf/application.properties");
		byte[] bytes = Files.readAllBytes(Paths.get(conf.getAbsolutePath()));
		conf.delete();
		wait(2000);
		Files.write(Paths.get(conf.getAbsolutePath()), bytes);
		wait(2000);
		try {
			listener.contextDestroyed(event);
		} catch (Exception e) {

		}
		assertEquals(0, listener.getInitializer().getHandlers().size());
		when(context.getFilterRegistration(isA(String.class)))
		.thenReturn(mock(FilterRegistration.Dynamic.class));
		folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpFilter.groovy");
		listener.getInitializer().register(scriptManager.loadObject(script));
	}
	
	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}