package org.gservlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import org.junit.Test;

public class InitializerTest {

	@Test
	public void init() throws Exception {
		File folder = new File("src/test/resources");
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		when(context.addFilter(isA(String.class), isA(Filter.class)))
				.thenReturn(mock(FilterRegistration.Dynamic.class));
		when(context.addServlet(isA(String.class), isA(Servlet.class)))
				.thenReturn(mock(ServletRegistration.Dynamic.class));
		Initializer initializer = new Initializer(context);
		assertEquals(11, initializer.getHandlers().size());
		for (DynamicInvocationHandler handler : initializer.getHandlers().values()) {
			assertNotNull(handler.getTarget());
			assertTrue(handler.isRegistered());
		}
		wait(2000);
		File file = new File(folder + "/scripts/script.groovy");
		PrintWriter printWriter = new PrintWriter(new FileWriter(file));
		printWriter.println("import org.gservlet.annotation.Servlet");
		printWriter.println("@Servlet(\"/servlet\")");
		printWriter.println("class MyServlet {}");
		printWriter.close();
		wait(2000);
		assertEquals(12, initializer.getHandlers().size());
		file.delete();
		printWriter = new PrintWriter(new FileWriter(file));
		printWriter.println("import org.gservlet.annotation.Filter");
		printWriter.println("@Filter(\"/*\")");
		printWriter.println("class MyFilter {}");
		printWriter.close();
		wait(2000);
		assertEquals(12, initializer.getHandlers().size());
		file.delete();
		initializer.destroy();
		assertEquals(0, initializer.getHandlers().size());
	}

	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}