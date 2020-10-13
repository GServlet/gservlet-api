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

import static org.gservlet.Constants.APP_CONFIG_FILE;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * Bootstraps the application and starts the registration of the servlets, filters,
 * listeners into the web container.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
@WebListener
public class StartupListener implements ServletContextListener {

	/**
	 * The container initializer object
	 */
	protected ContainerInitializer initializer;
	/**
	 * The database manager object
	 */
	protected DatabaseManager databaseManager;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * initializes the application and the registration process 
	 * 
	 * @param event the ServletContextEvent containing the ServletContext that is
	 *              being initialized
	 * 
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext context = event.getServletContext();
			initializer = new ContainerInitializer(context);
			databaseManager = new DatabaseManager(context);
			File root = new File(context.getRealPath("/"));
			File configuration = new File(root + File.separator + APP_CONFIG_FILE);
			if(configuration.exists()) {
				databaseManager.setupDataSource(loadConfiguration(configuration));
			}
			watch(root);
			logger.info("application started on context " + context.getContextPath());
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during contextInitialized method", e);
		}
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
		try(FileReader reader = new FileReader(file)) {
			configuration.load(reader);
		}
		return configuration;
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
				if (file.getName().equals(APP_CONFIG_FILE)) {
					logger.info("processing configuration file " + file.getName());
					try {
						databaseManager.setupDataSource(loadConfiguration(file));
					} catch (IOException e) {
						logger.log(Level.INFO, "exception during reload", e);
					}
				}
			}
		}).watch();
	}


	/**
	 * 
	 * Shutdowns the application in a clean way
	 * 
	 * @param event the ServletContextEvent containing the ServletContext that is
	 *              being destroyed
	 * 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		initializer.destroy();
		databaseManager.destroy();
		for (WatchService watchService : FileWatcher.getWatchServices()) {
			try {
				watchService.close();
			} catch (IOException e) {
				// the exception is ignored
			}
		}
	}

	/**
	 * 
	 * Returns the ContainerInitializer object
	 * 
	 * @return the ContainerInitializer object
	 * 
	 */
	public ContainerInitializer getInitializer() {
		return initializer;
	}

	/**
	 * 
	 * Returns the DatabaseManager object
	 * 
	 * @return the DatabaseManager object
	 * 
	 */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

}