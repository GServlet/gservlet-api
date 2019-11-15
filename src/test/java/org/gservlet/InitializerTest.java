package org.gservlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import java.io.File;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.junit.Test;

public class InitializerTest {

	@Test
	public void init() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.addFilter(isA(String.class),isA(Filter.class))).thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class),isA(Servlet.class))).thenReturn(mock(ServletRegistration.Dynamic.class));
		Initializer initializer = new Initializer(context);
		initializer.loadScripts(folder);
		assertEquals(1, initializer.getHandlers().size());
	}

}
