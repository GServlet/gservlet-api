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
import javax.sql.DataSource;

/**
 * 
 * The Groovy Servlet Application starts the registration of the servlets,
 * filters, listeners into the web container.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class GServletApplication {

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
	 * The servlet context object
	 */
	protected ServletContext context;

	/**
	 * The real path
	 */
	protected String path;

	/**
	 * 
	 * Constructs an application for the given context
	 * 
	 * @param context the servlet context
	 * 
	 * 
	 */
	public GServletApplication(ServletContext context) {
		this(context, context.getRealPath("/"));
	}

	/**
	 * 
	 * Constructs an application for the given context
	 * 
	 * @param context the servlet context
	 * 
	 * @param path the given path
	 * 
	 */
	public GServletApplication(ServletContext context, String path) {
		this.context = context;
		this.path = path;
	}

	/**
	 * Starts the initialization process
	 *
	 */
	public void start() {
		try {
			initializer = new ContainerInitializer(context, path);
			databaseManager = databaseManager != null ? databaseManager : createDatabaseManager();
			logger.info("application started on context " + context.getContextPath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "exception during contextInitialized method", e);
		}
	}

	/**
	 * Creates and configures the database manager
	 *
	 * @throws IOException the exception
	 */
	private DatabaseManager createDatabaseManager() throws IOException {
		databaseManager = new DatabaseManager(context);
		File root = new File(path);
		File configuration = new File(root + File.separator + APP_CONFIG_FILE);
		if (configuration.exists()) {
			databaseManager.setupDataSource(loadConfiguration(configuration));
		}
		watch(root);
		return databaseManager;
	}

	/**
	 * Starts the initialization process on a SpringBoot application
	 *
	 */
	public void startOnSpringBoot() {
		path = System.getProperty("user.dir") + File.separator + "src/main/resources";
		if (new File(path).exists()) {
			start();
		} else {
			String folder = "BOOT-INF/classes/";
			explodeClassPathResources(context.getRealPath("/"), folder);
			path = context.getRealPath("/") + folder;
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
	public Properties loadConfiguration(File file) throws IOException {
		Properties configuration = new Properties();
		try (FileReader reader = new FileReader(file)) {
			configuration.load(reader);
		}
		return configuration;
	}

	/**
	 * Watches the root folder for a change of the configuration file
	 *
	 * @param folder the root folder
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
		initializer.shutDown();
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
			logger.log(Level.SEVERE, "exception encountered when exploding the classpath resources", e);
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

}