package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.lang.annotation.Annotation;
import org.gservlet.annotation.Servlet;
import org.junit.Test;


public class ScriptManagerTest {

	@Test
	public void loadScripts() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File[] files = folder.listFiles();
		if(files!=null) {
			for(File script : files) {
				Object object = scriptManager.loadScript(script.getName());
				Annotation[] annotations = object.getClass().getAnnotations();
				for(Annotation current : annotations) {
				   if(current instanceof Servlet) {
					   assertEquals("HttpServlet",object.getClass().getName());
					   assertEquals(HttpServlet.class, object.getClass().getSuperclass());
					   Servlet annotation = (Servlet) current;
					   assertEquals("/servlet", annotation.value()[0]);
				   }
				}
			}
		}
	}

}
