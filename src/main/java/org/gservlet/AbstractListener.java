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

import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* 
* Abstract base class inherited by all the listeners classes.
* 
* @author Mamadou Lamine Ba
* 
*/
public abstract class AbstractListener implements EventListener{

	/**
	 * The event holder object
	 */
	protected final ThreadLocal<Object> eventHolder = new ThreadLocal<>();
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	* 
	* Invokes the corresponding method defined on the subclasses
	* 
	* @param method the method
	* @param event the event object
	* 
	*/
	protected void invoke(String method, Object event) {
		try {
			eventHolder.set(event);
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			// the exception is ignored if the method does not exist
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during invoke method", e);
		}
	}

	/**
	* 
	* Returns the Logger object
	* 
	* @return the Logger object
	* 
	*/
	public Logger getLogger() {
		return logger;
	}
	
}