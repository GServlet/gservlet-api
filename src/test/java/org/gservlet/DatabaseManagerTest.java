package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import java.io.File;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.junit.Test;

public class DatabaseManagerTest {

	@Test
	public void loadConfiguration() throws Exception {
		File folder = new File("src/test/resources/" + CONFIG_FOLDER);
		assertEquals(true, folder.exists());
		DatabaseManager databaseManager = new DatabaseManager(mock(ServletContext.class));
		File configuration = new File(folder + "/" + DB_CONFIG_FILE);
		Properties properties = databaseManager.loadConfiguration(configuration);
		assertTrue(databaseManager.isConfigurationValid(properties));
	}

}
