package org.gservlet;

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DatabaseManagerTest {

	@Test
	public void loadConfiguration() throws Exception {
		File folder = new File("src/test/resources");
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.getAttribute(DATASOURCE)).thenReturn(new BasicDataSource());
		DatabaseManager databaseManager = new DatabaseManager(context);
		File file = new File(folder + "/" + APP_CONFIG_FILE);
		Properties configuration = new Properties();
		FileReader reader = new FileReader(file);
		configuration.load(reader);
		reader.close();
		assertTrue(databaseManager.isConfigurationValid(configuration));
		databaseManager.destroy();
	}

}