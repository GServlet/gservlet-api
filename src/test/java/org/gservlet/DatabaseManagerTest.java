package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.junit.Test;

public class DatabaseManagerTest {

	@Test
	public void loadConfiguration() throws Exception {
		File folder = new File("src/test/resources/" + CONFIG_FOLDER);
		assertEquals(true, folder.exists());
		DatabaseManager databaseManager = new DatabaseManager(mock(ServletContext.class));
		File file = new File(folder + "/" + APP_CONFIG_FILE);
		Properties configuration = new Properties();
		FileReader reader = new FileReader(file);
		configuration.load(reader);
		reader.close();
		assertTrue(databaseManager.isConfigurationValid(configuration));
		databaseManager.destroy();
	}

}
