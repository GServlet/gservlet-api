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

/**
* 
* Interface for receiving notification events about files changes.
* 
* @author Mamadou Lamine Ba
* 
*/
public interface FileListener extends EventListener {

	/**
	* 
	* Receives notification that a file has been created
	* 
	* @param event the FileEvent containing the file that was created 
	* 
	*/
	public void onCreated(FileEvent event);
	
	/**
	* 
	* Receives notification that a file has been deleted
	* 
	* @param event the FileEvent containing the file that was deleted
	* 
	*/
	public void onDeleted(FileEvent event);
	
	
	/**
	* 
	* Receives notification that a file has been modified
	* 
	* @param event the FileEvent containing the file that was modified
	* 
	*/
	public void onModified(FileEvent event);
	
}