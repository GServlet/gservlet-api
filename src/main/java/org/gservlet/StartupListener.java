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

import java.io.IOException;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * Bootstraps the application and the registration of the servlets, filters,
 * listeners into the web container.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
@WebListener
public class StartupListener implements ServletContextListener {

	/**
	 * The initializer object
	 */
	protected Initializer initializer;
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
	 * Receives notification that the web application initialization process is
	 * starting
	 * 
	 * @param event the ServletContextEvent containing the ServletContext that is
	 *              being initialized
	 * 
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		try {
			initializer = new Initializer(context);
			databaseManager = new DatabaseManager(context);
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during contextInitialized method", e);
		}
		logger.info("application started on context " + context.getContextPath());
	}

	/**
	 * 
	 * Receives notification that the ServletContext is about to be shut down
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
	 * Returns the Initializer object
	 * 
	 * @return the Initializer object
	 * 
	 */
	public Initializer getInitializer() {
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