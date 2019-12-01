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

package org.gservlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.servlet.annotation.WebInitParam;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
* 
* Annotation used to declare a servlet.
*
* This annotation is processed at deployment time,
* and the corresponding servlet made available at the specified URL
* patterns.
* 
* @see javax.servlet.Servlet
* 
* @author Mamadou Lamine Ba
* 
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Servlet {

	/**
     * The name of the servlet
     *
     * @return the name of the servlet
     */
	String name() default "";
	
	/**
    * The URL patterns of the servlet
    *
    * @return the URL patterns of the servlet
    */
    String[] value() default {};
    
    /**
     * The URL patterns of the servlet
     *
     * @return the URL patterns of the servlet
     */
    String[] urlPatterns() default {};
    
    /**
     * The load-on-startup order of the servlet 
     *
     * @return the load-on-startup order of the servlet
     */
    int loadOnStartup() default -1;
    
    /**
     * The init parameters of the servlet
     *
     * @return the init parameters of the servlet
     */
    WebInitParam [] initParams() default {};
    
    /**
     * Declares whether the servlet supports asynchronous operation mode.
     *
     * @return {@code true} if the servlet supports asynchronous operation mode
     * @see javax.servlet.ServletRequest#startAsync
     * @see javax.servlet.ServletRequest#startAsync(ServletRequest,
     * ServletResponse)
     */
    boolean asyncSupported() default false;
    
    /**
     * The small-icon of the servlet
     *
     * @return the small-icon of the servlet
     */
    String smallIcon() default "";
    
    /**
     * The large-icon of the servlet
     *
     * @return the large-icon of the servlet
     */
    String largeIcon() default "";
    
    /**
     * The description of the servlet
     *
     * @return the description of the servlet
     */
    String description() default "";
    
    /**
     * The display name of the servlet
     *
     * @return the display name of the servlet
     */
    String displayName() default "";

}