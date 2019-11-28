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

public abstract class BaseListener {

	protected ThreadLocal<Object> eventHolder = new ThreadLocal<>();
	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	
	protected void route(Object event, String method) {
		eventHolder.set(event);
		invoke(method);
	}
	
	protected void invoke(String method) {
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
			// the exception is ignored if the method does not exist
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during invoke method", e);
		}
	}

}