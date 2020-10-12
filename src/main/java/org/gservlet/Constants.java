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

/**
 * 
 * Final class that defines the constants used to control the behavior of the
 * application.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public final class Constants {

	/**
	 * The scripts folder constant
	 */
	public static final String SCRIPTS_FOLDER = "scripts";
	/**
	 * The data source constant
	 */
	public static final String DATASOURCE = "GSERVLET_DATASOURCE";
	/**
	 * The database connection constant
	 */
	public static final String DB_CONNECTION = "GSERVLET_CONNECTION";
	/**
	 * The reload constant
	 */
	public static final String RELOAD = "GSERVLET_RELOAD";

	/**
	 * The application configuration file constant
	 */
	public static final String APP_CONFIG_FILE = "application.properties";
	/**
	 * The handlers constant
	 */
	public static final String HANDLERS = "GSERVLET_HANDLERS";
	/**
	 * The default request filter constant
	 */
	public static final String REQUEST_FILTER = "GSERVLET_REQUESTFILTER";

	/**
	 * The private constructor
	 */
	private Constants() {
	}

}