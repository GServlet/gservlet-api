package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.lang.annotation.Annotation;
import javax.servlet.ServletContext;
import static org.mockito.Mockito.mock;
import org.gservlet.annotation.Servlet;
import org.junit.Test;
import static org.mockito.Mockito.when;

public class ScriptManagerTest {

	@Test
	public void loadScripts() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.getRealPath("/")).thenReturn(folder.getAbsolutePath());
		ScriptManager scriptManager = new ScriptManager(context);
		File[] files = folder.listFiles();
		if(files!=null) {
			for(File script : files) {
				Object object = scriptManager.loadScript(script);
				Annotation[] annotations = object.getClass().getAnnotations();
				for(Annotation annotation : annotations) {
				   if(annotation instanceof Servlet) {
					   assertEquals("ServletTest",object.getClass().getName());
					   assertEquals(HttpServlet.class.getName(), object.getClass().getSuperclass().getName());
					   Servlet servletAnnotation = (Servlet) annotation;
					   assertEquals("/servlet", servletAnnotation.value()[0]);
				   }
				}
			}
		}
	}

}
