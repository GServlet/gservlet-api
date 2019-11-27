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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected Initializer initializer;
	protected DatabaseManager databaseManager;

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

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		initializer.destroy();
		databaseManager.destroy();
	}

}