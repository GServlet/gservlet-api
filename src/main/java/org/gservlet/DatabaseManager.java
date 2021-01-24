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
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * 
 * Handles the creation and the configuration of the data source
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class DatabaseManager {

	/**
	 * The servlet context object
	 */
	protected final ServletContext context;

	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * Constructs a DatabaseManager for the given ServletContext
	 * 
	 * @param context the servlet context
	 */
	public DatabaseManager(ServletContext context) {
		this.context = context;
	}

	/**
	 * Creates the data source and stores it as an attribute in the context
	 *
	 * @param configuration the application configuration properties
	 * 
	 */
	public void setupDataSource(Properties configuration) {
		if (isConfigurationValid(configuration)) {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(getDriverClassName(configuration));
			dataSource.setUrl(getUrl(configuration));
			dataSource.setUsername(getUsername(configuration));
			dataSource.setPassword(getPassword(configuration));
			dataSource.setInitialSize(getMinPoolSize(configuration));
			dataSource.setMaxTotal(getMaxPoolSize(configuration));
			context.setAttribute(DATASOURCE, dataSource);
		}
	}
	

	/**
	 * Stores the provided data source as an attribute in the context
	 *
	 * @param dataSource the provided data source
	 * 
	 */
	public void setupDataSource(DataSource dataSource) {
		context.setAttribute(DATASOURCE, dataSource);
	}
	
	/**
	 * Returns the driver class name.
	 * 
	 * @param configuration the configuration
	 * @return the driver class name
	 * 
	 */
	private String getDriverClassName(Properties configuration) {
		return configuration.getProperty("db.driver").trim();
	}
	
	/**
	 * Returns the connection url
	 * 
	 * @param configuration the configuration
	 * @return the connection url
	 * 
	 */
	private String getUrl(Properties configuration) {
		return configuration.getProperty("db.url").trim();
	}

	/**
	 * Returns the user name
	 * 
	 * @param configuration the configuration
	 * @return the user name.
	 * 
	 */
	private String getUsername(Properties configuration) {
		return configuration.getProperty("db.user").trim();
	}
	
	/**
	 * Returns the user password
	 * 
	 * @param configuration the configuration
	 * @return the user password.
	 * 
	 */
	private String getPassword(Properties configuration) {
		return configuration.getProperty("db.password").trim();
	}
	
	/**
	 * Returns the min pool size. The default value is 1
	 * 
	 * @param configuration the configuration
	 * @return the min pool size.
	 * 
	 */
	private int getMinPoolSize(Properties configuration) {
		String minPoolSize = configuration.getProperty("db.minPoolSize");
		return minPoolSize != null ? Integer.parseInt(minPoolSize.trim()) : 1;
	}
	
	/**
	 * Returns the max pool size. The default value is 3
	 * 
	 * @param configuration the configuration
	 * @return the max pool size
	 * 
	 */
	private int getMaxPoolSize(Properties configuration) {
		String maxPoolSize = configuration.getProperty("db.maxPoolSize");
		return maxPoolSize!= null ? Integer.parseInt(maxPoolSize.trim()) : 3;
	}

	/**
	 * Checks if the configuration is valid
	 * <p>
	 * The required properties are : <br>
	 * db.driver : the jdbc driver class <br>
	 * db.url : the database url <br>
	 * db.user : the database user <br>
	 * db.password : the database user password <br>
	 * </p>
	 * 
	 * @param configuration the configuration
	 * @return true if the required properties are present
	 * 
	 */
	public boolean isConfigurationValid(Properties configuration) {
		return configuration.containsKey("db.driver") && configuration.containsKey("db.url")
				&& configuration.containsKey("db.user") && configuration.containsKey("db.password");
	}

	/**
	 * Closes the data source
	 * 
	 */
	public void shutDown() {
		try {
			Object dataSource = context.getAttribute(DATASOURCE);
			if(dataSource instanceof BasicDataSource) {
				BasicDataSource basicDataSource = (BasicDataSource) context.getAttribute(DATASOURCE);
				basicDataSource.close();	
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "exception during destroy method", e);
		}
	}
	
	/**
	 * Returns the data source
	 * 
	 * @return the data source
	 * 
	 */
	
	public DataSource getDataSource() {
		return (DataSource) context.getAttribute(DATASOURCE);
	}
	
}