package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
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
				assertNotNull(object);
			}
		}
	}

}
