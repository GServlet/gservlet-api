package org.gservlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import java.io.File;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.junit.Test;

public class DatabaseManagerTest {

	@Test
	public void loadConfiguration() throws Exception {
		File folder = new File("src/test/resources/"+Constants.CONFIG_FOLDER);
		assertEquals(true, folder.exists());
		DatabaseManager databaseManager = new DatabaseManager(mock(ServletContext.class));
		File configuration = new File(folder + File.separator + Constants.DB_CONFIG_FILE);
		Properties properties = databaseManager.loadConfiguration(configuration);
		assertTrue(databaseManager.isConfigurationValid(properties));
		DataSource dataSource = databaseManager.createDataSource(properties);
		assertNotNull(dataSource);
		
	}

}
