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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import org.gservlet.annotation.ContextAttributeListener;
import org.gservlet.annotation.ContextListener;
import org.gservlet.annotation.Filter;
import org.gservlet.annotation.RequestAttributeListener;
import org.gservlet.annotation.RequestListener;
import org.gservlet.annotation.Servlet;
import org.gservlet.annotation.SessionAttributeListener;
import org.gservlet.annotation.SessionListener;

import groovy.util.ScriptException;

/**
* 
*  The Initializer class initializes the application for the given scripts folder and 
*  manages the registration and the reloading of a servlet, filter or listener.
* 
* @author Mamadou Lamine Ba
* 
*/
public class Initializer {

	/**
	 * The servlet context
	 */
	protected final ServletContext context;
	/**
	 * The map of invocation handlers
	 */
	protected final Map<String, DynamicInvocationHandler> handlers;
	/**
	 * The script manager
	 */
	protected final ScriptManager scriptManager;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(Initializer.class.getName());

	/**
	* 
	* Constructs a Initializer for the given servlet context
	* 
	* @param context the servlet context
	* @throws ServletException the ServletException 
	*  
	*/
	public Initializer(ServletContext context) throws ServletException {
		try {
			this.context = context;
			handlers = new HashMap<>();
			context.setAttribute(Constants.HANDLERS, handlers);
			File folder = new File(context.getRealPath("/") + File.separator + Constants.SCRIPTS_FOLDER);
			scriptManager = new ScriptManager(folder);
			init(folder);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	* 
	* Initializes the application for the given scripts folder
	* 
	* @param folder the scripts folder
	* @throws ServletException the ServletException
	* @throws ScriptException the ScriptException 
	*  
	*/
	protected void init(File folder) throws ServletException, ScriptException {
		loadScripts(folder);
		context.addFilter(Constants.REQUEST_FILTER, new DefaultRequestFilter())
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	}

	/**
	* 
	* Loads the scripts for the given scripts folder
	* 
	* @param folder the scripts folder
	* @throws ServletException the ServletException
	* @throws ScriptException the ScriptException 
	*  
	*/
	public void loadScripts(File folder) throws ServletException, ScriptException {
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						Object object = scriptManager.loadScript(file.getName());
						register(object);
					} else {
						loadScripts(file);
					}
				}
			}
			watch(folder);
		}
	}

	/**
	* 
	* Registers a servlet, filter or listener into the web container
	* 
	* @param object the object
	* @throws ServletException the ServletException 
	*  
	*/
	protected void register(Object object) throws ServletException {
		Annotation[] annotations = object.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Servlet) {
				addServlet(context, (Servlet) annotation, object);
			} else if (annotation instanceof Filter) {
				addFilter(context, (Filter) annotation, object);
			} else if (annotation instanceof ContextListener || annotation instanceof ContextAttributeListener
					|| annotation instanceof RequestListener || annotation instanceof RequestAttributeListener
					|| annotation instanceof SessionListener || annotation instanceof SessionAttributeListener) {
				addListener(context, object);
			}
		}
	}

	/**
	* 
	* Registers a servlet into the web container
	* 
	* @param context the servlet context
	* @param annotation the servlet annotation
	* @param object the object
	* @throws ServletException the ServletException 
	*  
	*/
	protected void addServlet(ServletContext context, Servlet annotation, Object object) throws ServletException {
		String name = annotation.name().trim().equals("") ? object.getClass().getName() : annotation.name();
		ServletRegistration registration = context.getServletRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object servlet = Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { javax.servlet.Servlet.class }, handler);
			handlers.put(name, handler);
			registration = context.addServlet(name, (javax.servlet.Servlet) servlet);
			if (annotation.value().length > 0) {
				registration.addMapping(annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				registration.addMapping(annotation.urlPatterns());
			}
			for(WebInitParam param : annotation.initParams()) {
				registration.setInitParameter(param.name(), param.value());
			}
		} else {
			String message = "The servlet with the name " + name
					+ " has already been registered. Please use a different name or package";
			throw new ServletException(message);
		}
	}

	/**
	* 
	* Registers a filter into the web container
	* 
	* @param context the servlet context
	* @param annotation the filter annotation
	* @param object the object
	* @throws ServletException the ServletException 
	*  
	*/
	protected void addFilter(ServletContext context, Filter annotation, Object object) throws ServletException {
		String name = object.getClass().getName();
		FilterRegistration registration = context.getFilterRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object filter = Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { javax.servlet.Filter.class }, handler);
			handlers.put(name, handler);
			registration = context.addFilter(name, (javax.servlet.Filter) filter);
			Collection<DispatcherType> dispatcherTypes = Arrays.asList(annotation.dispatcherTypes());
			if (annotation.value().length > 0) {
				registration.addMappingForUrlPatterns(EnumSet.copyOf(dispatcherTypes), true,
						annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				registration.addMappingForUrlPatterns(EnumSet.copyOf(dispatcherTypes), true,
						annotation.urlPatterns());
			}
			for(WebInitParam param : annotation.initParams()) {
				registration.setInitParameter(param.name(), param.value());
			}
		} else {
			String message = "The filter with the name " + name
					+ " has already been registered. Please use a different name or package";
			throw new ServletException(message);
		}
	}

	/**
	* 
	* Registers a listener into the web container
	* 
	* @param context the servlet context
	* @param object the object
	*  
	*/
	protected void addListener(ServletContext context, Object object) {
		DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
		EventListener listener = null;
		if (object instanceof ServletContextAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletContextAttributeListener.class }, handler);
		} else if (object instanceof ServletRequestListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletRequestListener.class }, handler);
		} else if (object instanceof ServletRequestAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletRequestAttributeListener.class }, handler);
		} else if (object instanceof HttpSessionListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { HttpSessionListener.class }, handler);
		} else if (object instanceof HttpSessionAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { HttpSessionAttributeListener.class }, handler);
		}
		if (listener != null) {
			context.addListener(listener);
		} else if (object instanceof AbstractContextListener) {
			AbstractContextListener contextListener = (AbstractContextListener) object;
			contextListener.contextInitialized(new ServletContextEvent(context));
		}
		handlers.put(object.getClass().getName(), handler);
	}

	/**
	* 
	* Watches the scripts folder for file changes
	* 
	* @param folder the scripts folder
	*  
	*/
	protected void watch(File folder) {
		boolean reload = Boolean.parseBoolean(System.getenv(Constants.RELOAD));
		if (reload) {
			new FileWatcher(folder).addListener(new FileAdapter() {
				@Override
				public void onCreated(FileEvent event) {
					String script = event.getFileName();
					logger.info("reloading script " + script);
					reload(script);
				}

			}).watch();
		}
	}

	/**
	* 
	* Reloads a script
	* 
	* @param script the changed script 
	*  
	*/
	protected void reload(String script) {
		try {
			Object object = scriptManager.loadScript(script);
			Annotation[] annotations = object.getClass().getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Servlet) {
					reloadServlet((Servlet) annotation, object);
				} else if (annotation instanceof Filter || annotation instanceof RequestListener
						|| annotation instanceof ContextAttributeListener
						|| annotation instanceof RequestAttributeListener || annotation instanceof SessionListener
						|| annotation instanceof SessionAttributeListener) {
					DynamicInvocationHandler handler = handlers.get(object.getClass().getName());
					if (handler != null) {
						handler.setTarget(object);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "exception during reload", e);
		}
	}

	/**
	* 
	* Reloads a servlet into the web container
	* 
	* @param servlet the servlet annotation
	* @param object the object
	*  
	*/
	protected void reloadServlet(Servlet servlet, Object object) {
		String name = servlet.name().trim().equals("") ? object.getClass().getName() : servlet.name();
		DynamicInvocationHandler handler = handlers.get(name);
		if (handler != null) {
			handler.setTarget(object);
		} else {
			handler = new DynamicInvocationHandler(object);
			handler.setRegistered(false);
			handlers.put(name, handler);
		}
	}

	/**
	* 
	* Calls the destroy method of the ServletContextListener objects when the 
	* ServletContext is about to be shut down
	*  
	*/
	public void destroy() {
		for (DynamicInvocationHandler handler : handlers.values()) {
			Object target = handler.getTarget();
			if (target instanceof AbstractContextListener) {
				AbstractContextListener contextListener = (AbstractContextListener) target;
				contextListener.contextDestroyed(new ServletContextEvent(context));
			}
		}
		handlers.clear();
	}

	/**
	* 
	* Returns the map of invocation handlers
	* @return the map of invocation handlers 
	*/
	public Map<String, DynamicInvocationHandler> getHandlers() {
		return handlers;
	}

}