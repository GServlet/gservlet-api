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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import groovy.util.ScriptException;

/**
 * 
 * The Groovy Servlet Application starts the registration of the servlets,
 * filters, listeners into the web container and the data source configuration
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class GServletApplication {

	/**
	 * The container manager object
	 */
	protected ContainerManager containerManager;
	/**
	 * The database manager object
	 */
	protected DatabaseManager databaseManager;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * The servlet context object
	 */
	protected ServletContext context;

	/**
	 * The context real path
	 */
	protected String realPath;

	/**
	 * The list of script listeners
	 */
	protected final List<ScriptListener> listeners = new ArrayList<>();
	
	/**
	 * 
	 * Constructs an application for the given context
	 * 
	 * @param context the servlet context
	 * 
	 */
	public GServletApplication(ServletContext context) {
		this.context = context;
		this.realPath = context.getRealPath("/");
	}

	/**
	 * Starts the initialization process
	 *
	 */
	public void start() {
		try {
			initContainerManager();
			initDatabaseManager();
			logger.info("application started on context " + context.getContextPath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "exception during contextInitialized method", e);
		}
	}

	/**
	 * Creates and initializes the container manager
	 *
	 *
	 * @throws ServletException the ServletException
	 * @throws ScriptException  the  ScriptException
	 * 
	 */
	private void initContainerManager() throws ServletException, ScriptException {
		containerManager = new ContainerManager(context);
		containerManager.init(realPath, listeners);
	}
	
	/**
	 * Creates and initializes the database manager
	 *
	 * @throws IOException the exception
	 * 
	 */
	private void initDatabaseManager() throws IOException {
		databaseManager = new DatabaseManager(context);
		File root = new File(realPath);
		databaseManager.setupDataSource(loadConfiguration(new File(root + File.separator + APP_CONFIG_FILE)));
		watch(root);
	}

	/**
	 * Starts the initialization process on a SpringBoot application
	 *
	 */
	public void startOnSpringBoot() {
		realPath = System.getProperty("user.dir") + File.separator + "src/main/resources";
		if (new File(realPath).exists()) {
			start();
		} else {
			String folder = "BOOT-INF/classes/";
			explodeClassPathResources(context.getRealPath("/"), folder);
			realPath = context.getRealPath("/") + folder;
			start();
		}
		context.addListener(new DefaultRequestListener());
	}

	/**
	 * Loads the configuration file properties
	 * 
	 * @param file the configuration file
	 * @throws IOException throws an Exception if the configuration file is invalid
	 * @return the properties of the configuration file
	 */
	private Properties loadConfiguration(File file) throws IOException {
		Properties configuration = new Properties();
		if(file.exists()) {
			try (FileReader reader = new FileReader(file)) {
				configuration.load(reader);
			}
		}
		return configuration;
	}

	/**
	 * Watches the root folder for a change of the configuration file
	 *
	 * @param folder the root folder
	 */
	protected void watch(File folder) {
		new FileWatcher(folder).addFileListener(new FileAdapter() {
			
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

			@Override
			public void onModified(FileEvent event) {
				onCreated(event);
			}
			
		}).watch();
	}

	/**
	 * 
	 * Stops the application and frees the resources
	 *  
	 */
	public void stop() {
		containerManager.shutDown();
		databaseManager.shutDown();
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
	 * Returns the ContainerManager object
	 * 
	 * @return the ContainerManager object
	 * 
	 */
	public ContainerManager getContainerManager() {
		return containerManager;
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

	/**
	 * 
	 * Explodes the classpath resources
	 * 
	 * @param root  the root folder
	 * 
	 * @param folder the resources folder
	 * 
	 * 
	 */
	private void explodeClassPathResources(String root, String folder) {
		try {
			List<Path> paths = getClassPathResources(folder);
			for (Path resourcePath : paths) {
				String resource = resourcePath.toString();
				if (resource.startsWith("/")) {
					resource = resource.substring(1, resource.length());
				}
				if (resource.indexOf(".") != -1)
					try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
						File file = new File(root + File.separator + resource);
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						Files.copy(inputStream, file.toPath());
					}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "exception encountered while exploding the classpath resources", e);
		}
	}

	/**
	 * 
	 * Returns the list of classpath resources
	 * 
	 * @param folder the base folder
	 * 
	 * @return the list of classpath resources
	 * 
	 */
	private List<Path> getClassPathResources(String folder) throws IOException {
		List<Path> paths = new ArrayList<>();
		String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		URI uri = URI.create("jar:" + jarPath);
		try (FileSystem fs = FileSystems.newFileSystem(uri, new HashMap<>())) {
			try(Stream<Path> stream = Files.walk(fs.getPath(folder + Constants.SCRIPTS_FOLDER))) {
				paths = stream.collect(Collectors.toList());
			}
			paths.add(fs.getPath(folder + Constants.APP_CONFIG_FILE));
		}
		return paths;
	}

	/**
	 * 
	 * Sets the data source
	 * 
	 * @param dataSource the data source
	 * 
	 */
	public void setDataSource(DataSource dataSource) {
		databaseManager = new DatabaseManager(context);
		databaseManager.setupDataSource(dataSource);
	}
	
	/**
	 *  Registers a new ScriptListener
	 * 
	 * @param listener the ScriptListener
	 * 
	 */
	public void addScriptListener(ScriptListener listener) {
		if(containerManager != null) {
			containerManager.getScriptManager().addScriptListener(listener);
		} else {
			listeners.add(listener);
		}
	}
	
	/**
	 *  Registers a ScriptListener list
	 * 
	 * @param listeners the ScriptListener list
	 * 
	 */
	public void addScriptListeners(List<ScriptListener> listeners) {
		if(containerManager != null) {
			containerManager.getScriptManager().addScriptListeners(listeners);
		} else {
			this.listeners.addAll(listeners);
		}
	}

}