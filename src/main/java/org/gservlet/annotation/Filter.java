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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.DispatcherType;

/**
* 
* This annotation is used to declare a {@link javax.servlet.Filter}. 
*
* This annotation is processed at deployment time,
* and the corresponding filter applied to the specified URL patterns,
* servlets, and dispatcher types.
* 
* @see org.gservlet.AbstractFilter
* 
* @author Mamadou Lamine Ba
* 
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Filter {

	/**
     * The name of the filter
     *
     * @return the name of the filter
     */
	String filterName() default "";
	
	/**
     * The URL patterns to which the filter applies
     * The default value is an empty array.
     *
     * @return the URL patterns to which the filter applies
     */
	String[] value() default {};
	
	/**
     * The URL patterns to which the filter applies
     *
     * @return the URL patterns to which the filter applies
     */
    String[] urlPatterns() default {};
    
    /**
     * The init parameters of the filter
     *
     * @return the init parameters of the filter
     */
    InitParam[] initParams() default {};
    
    /**
     * The dispatcher types to which the filter applies
     *
     * @return the dispatcher types to which the filter applies
     */
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};
    
    /**
     * The names of the servlets to which the filter applies.
     *
     * @return the names of the servlets to which the filter applies
     */
    String[] servletNames() default {};
    
    /**
     * Declares whether the filter supports asynchronous operation mode.
     *
     * @return {@code true} if the filter supports asynchronous operation mode
     * @see javax.servlet.ServletRequest#startAsync
     * @see javax.servlet.ServletRequest#startAsync(ServletRequest,
     * ServletResponse)
     */
	boolean asyncSupported() default false;
	
	/**
     * The small-icon of the filter
     *
     * @return the small-icon of the filter
     */
    String smallIcon() default "";
    
    /**
     * The large-icon of the filter
     *
     * @return the large-icon of the filter
     */
    String largeIcon() default "";
    
    /**
     * The description of the filter
     * 
     * @return the description of the filter
     */
    String description() default "";
    
    /**
     * The display name of the filter
     *
     * @return the display name of the filter
     */
    String displayName() default "";
    
}