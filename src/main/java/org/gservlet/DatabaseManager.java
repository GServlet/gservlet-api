package org.gservlet;

import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseManager {

	/**
	 * The servlet context
	 */
	protected final ServletContext context;

	protected final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

	public DatabaseManager(ServletContext context) throws Exception {
		this.context = context;
		init();
	}

	/**
	 * Configures the data source and stores it as attribute in the context with the
	 * datasource key
	 *
	 * @throws Exception throws an exception if the data source can't be configured
	 */

	protected void init() throws Exception {
		setupDataSource();
		watch(new File(context.getRealPath("/") + File.separator + Constants.CONFIG_FOLDER));
	}

	protected void setupDataSource() throws Exception {
		File configuration = new File(
				context.getRealPath("/") + File.separator + Constants.CONFIG_FOLDER + 
				File.separator + Constants.DB_CONFIG_FILE);
		if (configuration.exists()) {
			Properties properties = loadConfiguration(configuration);
			if (isConfigurationValid(properties)) {
				context.setAttribute(Constants.DATASOURCE, createDataSource(properties));
			}
		}
	}

	protected void watch(File folder) {
		boolean reload = Boolean.parseBoolean(System.getenv(Constants.RELOAD));
		if (reload) {
			new FileWatcher().addListener(new FileAdapter() {
				public void onCreated(String fileName) {
					if (fileName.equals(Constants.DB_CONFIG_FILE)) {
						logger.info("reloading configuration file " + fileName);
						try {
							setupDataSource();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).watch(folder);
		}
	}

	public void destroy() {
		try {
			BasicDataSource dataSource = (BasicDataSource) context.getAttribute(Constants.DATASOURCE);
			if (dataSource != null) {
				dataSource.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Loads the configuration file properties
	 * </p>
	 * 
	 * @param configuration the configuration file
	 * @throws Exception throws an Exception if the configuration file is invalid
	 * @return the properties of the configuration file
	 */
	public Properties loadConfiguration(File configuration) throws Exception {
		Properties properties = new Properties();
		FileReader reader = new FileReader(configuration);
		properties.load(reader);
		reader.close();
		return properties;
	}

	/**
	 * <p>
	 * Checks if the configuration file is valid. The required properties are : <br>
	 * <br>
	 * db.driver : the jdbc driver class <br>
	 * db.url : the database url <br>
	 * db.user : the database user <br>
	 * db.password : the database user password <br>
	 * db.minPoolSize : the database min pool size <br>
	 * db.maxPoolSize : the database max pool size <br>
	 * db.batchSize : the database batch size
	 * 
	 * </p>
	 * 
	 * @param properties the configuration file properties
	 * @return true if the required configuration file properties are not missing
	 */
	public boolean isConfigurationValid(Properties properties) {
		return properties.containsKey("db.driver") && properties.containsKey("db.url")
				&& properties.containsKey("db.user") && properties.containsKey("db.password")
				&& properties.containsKey("db.minPoolSize") && properties.containsKey("db.maxPoolSize");
	}

	/**
	 * <p>
	 * Creates the pooled data source from the configuration file properties
	 * </p>
	 * 
	 * @param properties the configuration file properties
	 * @return the pooled data source from which the database connections are
	 *         created
	 * 
	 */
	public DataSource createDataSource(Properties properties) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(properties.getProperty("db.driver").trim());
		dataSource.setUrl(properties.getProperty("db.url").trim());
		dataSource.setUsername(properties.getProperty("db.user").trim());
		dataSource.setPassword(properties.getProperty("db.password"));
		dataSource.setInitialSize(Integer.parseInt(properties.getProperty("db.minPoolSize").trim()));
		dataSource.setMaxTotal(Integer.parseInt(properties.getProperty("db.maxPoolSize").trim()));
		return dataSource;
	}

}