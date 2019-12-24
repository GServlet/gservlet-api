package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import groovy.util.ScriptException;

public class ScriptManagerTest {

	@Test(expected = ScriptException.class)
	public void loadScripts() throws Exception {
		File folder = new File("src/test/resources/" + SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File[] files = folder.listFiles();
		if (files != null) {
			for (File script : files) {
				Object object = scriptManager.loadObject(script);
				assertNotNull(object);
			}
		}
		scriptManager.loadObject(new File("test.groovy"));
	}

}