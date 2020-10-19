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

import javax.servlet.ServletConfig;

/**
* 
* A servlet configuration object used by a servlet container to pass information to a servlet during initialization.   
* 
* @author Mamadou Lamine Ba
* 
*/
public class DefaultServletConfig extends AbstractConfig implements ServletConfig {
	
	/**
	 * Returns the name of this servlet instance.
	 *
	 * @return the name of the servlet instance
	 */
	@Override
	public String getServletName() {
		return null;
	}

}