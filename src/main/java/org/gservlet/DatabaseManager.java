/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.gservlet;

import static org.gservlet.Constants.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * 
 * The DatabaseManager handles the creation and the configuration of the data
 * source.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class DatabaseManager {

	/**
	 * The servlet context
	 */
	protected final ServletContext context;

	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

	/**
	 * 
	 * Constructs a DatabaseManager for the given ServletContext
	 * 
	 * @param context the servlet context
	 * @throws IOException the IOException
	 */
	public DatabaseManager(ServletContext context) throws IOException {
		this.context = context;
		init();
	}

	/**
	 * Initializes the configuration of the data source and watches the
	 * configuration file for changes
	 *
	 * @throws IOException throws an exception if the data source can't be
	 *                     configured
	 */
	protected void init() throws IOException {
		setupDataSource();
		watch(new File(context.getRealPath("/") + File.separator + CONFIG_FOLDER));
	}

	/**
	 * Creates the data source and stores it as an attribute in the context
	 *
	 * @throws IOException throws an IOException if the data source can't be created
	 * 
	 */
	protected void setupDataSource() throws IOException {
		File file = new File(context.getRealPath("/") + File.separator + CONFIG_FOLDER + File.separator + DB_CONFIG_FILE);
		if (file.exists()) {
			Properties configuration = loadConfiguration(file);
			if (isConfigurationValid(configuration)) {
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName(configuration.getProperty("db.driver").trim());
				dataSource.setUrl(configuration.getProperty("db.url").trim());
				dataSource.setUsername(configuration.getProperty("db.user").trim());
				dataSource.setPassword(configuration.getProperty("db.password"));
				dataSource.setInitialSize(Integer.parseInt(configuration.getProperty("db.minPoolSize").trim()));
				dataSource.setMaxTotal(Integer.parseInt(configuration.getProperty("db.maxPoolSize").trim()));
				context.setAttribute(DATASOURCE, dataSource);
			}
		}
	}

	/**
	 * Watches the configuration folder for file changes
	 *
	 * @param folder the configuration folder
	 */
	protected void watch(File folder) {
		new FileWatcher(folder).addListener(new FileAdapter() {
			@Override
			public void onCreated(FileEvent event) {
				File file = event.getFile();
				if (file.getName().equals(DB_CONFIG_FILE)) {
					logger.info("reloading configuration file " + file.getName());
					try {
						setupDataSource();
					} catch (IOException e) {
						logger.log(Level.INFO, "exception during reload", e);
					}
				}
			}
		}).watch();
	}

	/**
	 * Loads the configuration file properties
	 * 
	 * @param file the configuration file
	 * @throws IOException throws an Exception if the configuration file is invalid
	 * @return the properties of the configuration file
	 */
	public Properties loadConfiguration(File file) throws IOException {
		Properties configuration = new Properties();
		FileReader reader = new FileReader(file);
		configuration.load(reader);
		reader.close();
		return configuration;
	}

	/**
	 * Checks if the configuration is valid.
	 * <p>
	 * The required properties are : <br>
	 * db.driver : the jdbc driver class <br>
	 * db.url : the database url <br>
	 * db.user : the database user <br>
	 * db.password : the database user password <br>
	 * db.minPoolSize : the database min pool size <br>
	 * db.maxPoolSize : the database max pool size
	 * </p>
	 * 
	 * @param configuration the configuration
	 * @return true if the required properties are present
	 * 
	 */
	public boolean isConfigurationValid(Properties configuration) {
		return configuration.containsKey("db.driver") && configuration.containsKey("db.url")
				&& configuration.containsKey("db.user") && configuration.containsKey("db.password")
				&& configuration.containsKey("db.minPoolSize") && configuration.containsKey("db.maxPoolSize");
	}

	/**
	 * Closes the data source
	 * 
	 * 
	 */
	public void destroy() {
		try {
			BasicDataSource dataSource = (BasicDataSource) context.getAttribute(DATASOURCE);
			if (dataSource != null) {
				dataSource.close();
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "exception during destroy method", e);
		}
	}
}