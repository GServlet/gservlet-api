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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
* 
* An InvocationHandler implementation class that must be associated with a proxy instance.
* 
* @author Mamadou Lamine Ba
* 
*/
public class DynamicInvocationHandler implements InvocationHandler {

	/**
	 * The target object.
	 */
	protected Object target;
	/**
	 * The registration flag. By default, the value is set to true.
	 */
	protected boolean registered = true;
	
	
	/**
	* 
	* Construct a DynamicInvocationHandler for the given target object.
	* 
	* @param target the target object
	*  
	*/
	
	public DynamicInvocationHandler(Object target) {
	    this.target = target;	
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(target, args);
	}

	/**
	* 
	* Returns the target object.
	* 
	* @return the target object
	* 
	*/
	
	public Object getTarget() {
		return target;
	}

	/**
	* 
	* Sets the target object.
	* 
	* @param target the target object
	* 
	*/
	public void setTarget(Object target) {
		this.target = target;
	}

	/**
	* 
	* Returns the registration flag.
	* 
	* @return the registration flag
	* 
	*/
	public boolean isRegistered() {
		return registered;
	}

	/**
	* 
	* Sets the registration flag.
	* 
	* @param registered the registration flag
	* 
	*/
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}