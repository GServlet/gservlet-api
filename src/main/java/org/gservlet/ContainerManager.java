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

import static org.gservlet.Constants.HANDLERS;
import static org.gservlet.Constants.SCRIPTS_FOLDER;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletSecurityElement;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;
import org.gservlet.annotation.ContextAttributeListener;
import org.gservlet.annotation.ContextListener;
import org.gservlet.annotation.Filter;
import org.gservlet.annotation.RequestAttributeListener;
import org.gservlet.annotation.RequestListener;
import org.gservlet.annotation.Servlet;
import org.gservlet.annotation.SessionAttributeListener;
import org.gservlet.annotation.SessionIdListener;
import org.gservlet.annotation.SessionListener;
import groovy.util.ScriptException;

/**
 * 
 * The ContainerManager manages the registration and the reloading of a servlet,
 * filter or listener into the web container
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class ContainerManager {

	/**
	 * The servlet context object
	 */
	protected final ServletContext context;
	/**
	 * The map of dynamic invocation handlers
	 */
	protected final Map<String, DynamicInvocationHandler> handlers = new HashMap<>();
	/**
	 * The script manager object
	 */
	protected ScriptManager scriptManager;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * Constructs a ContainerManager for the given servlet context
	 * 
	 * @param context the servlet context
	 * @throws ServletException the ServletException
	 * 
	 */
	public ContainerManager(ServletContext context) throws ServletException {
		try {
			this.context = context;
			context.setAttribute(HANDLERS, handlers);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * 
	 * Initializes the application for the given directory
	 *
	 * @param directory the scripts parent directory
	 * @param listeners the ScriptListener list
	 * @throws ServletException the ServletException
	 * @throws ScriptException  the ScriptException
	 * 
	 */
	public void init(String directory, List<ScriptListener> listeners) throws ServletException, ScriptException {
		File folder = new File(directory + File.separator + SCRIPTS_FOLDER);
		scriptManager = new ScriptManager(folder);
		scriptManager.addScriptListeners(listeners);
		if (folder.exists()) {
			loadScripts(folder);
			watch(folder);
		}
	}

	/**
	 * 
	 * Loads and registers the servlets, filters or listeners
	 * 
	 * @param folder the scripts folder
	 * @throws ServletException the ServletException
	 * @throws ScriptException  the ScriptException
	 * 
	 */
	protected void loadScripts(File folder) throws ServletException, ScriptException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				register(scriptManager.createObject(file));
			} else {
				loadScripts(file);
			}
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
	public void register(Object object) throws ServletException {
		if (isServlet(object)) {
			addServlet(object);
		} else if (isFilter(object)) {
			addFilter(object);
		} else if (isListener(object)) {
			addListener(object);
		}
	}

	/**
	 * 
	 * Registers a servlet into the web container
	 * 
	 * @param object the servlet object
	 * @throws ServletException the ServletException
	 * 
	 */
	protected void addServlet(Object object) throws ServletException {
		Servlet annotation = object.getClass().getAnnotation(Servlet.class);
		String name = object.getClass().getName();
		ServletRegistration registration = context.getServletRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object servlet = createProxy(handler, javax.servlet.Servlet.class);
			handlers.put(name, handler);
			ServletRegistration.Dynamic dynamic = context.addServlet(name, (javax.servlet.Servlet) servlet);
			dynamic.setLoadOnStartup(annotation.loadOnStartup());
			dynamic.setAsyncSupported(annotation.asyncSupported());
			if (annotation.value().length > 0) {
				dynamic.addMapping(annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				dynamic.addMapping(annotation.urlPatterns());
			}
			Arrays.stream(annotation.initParams())
					.forEach(param -> dynamic.setInitParameter(param.name(), param.value()));
			MultipartConfig multiPartConfig = object.getClass().getAnnotation(MultipartConfig.class);
			if (multiPartConfig != null) {
				dynamic.setMultipartConfig(new MultipartConfigElement(multiPartConfig));
			}
			ServletSecurity servletSecurity = object.getClass().getAnnotation(ServletSecurity.class);
			if (servletSecurity != null) {
				dynamic.setServletSecurity(new ServletSecurityElement(servletSecurity));
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
	 * @param object the filter object
	 * @throws ServletException the ServletException
	 * 
	 */
	protected void addFilter(Object object) throws ServletException {
		Filter annotation = object.getClass().getAnnotation(Filter.class);
		String name = object.getClass().getName();
		FilterRegistration registration = context.getFilterRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object filter = createProxy(handler, javax.servlet.Filter.class);
			handlers.put(name, handler);
			FilterRegistration.Dynamic dynamic = context.addFilter(name, (javax.servlet.Filter) filter);
			dynamic.setAsyncSupported(annotation.asyncSupported());
			Collection<DispatcherType> dispatcherTypes = Arrays.asList(annotation.dispatcherTypes());
			if (annotation.value().length > 0) {
				dynamic.addMappingForUrlPatterns(EnumSet.copyOf(dispatcherTypes), true, annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				dynamic.addMappingForUrlPatterns(EnumSet.copyOf(dispatcherTypes), true, annotation.urlPatterns());
			}
			Arrays.stream(annotation.initParams())
					.forEach(param -> dynamic.setInitParameter(param.name(), param.value()));
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
	 * @param object the listener object
	 * 
	 */
	protected void addListener(Object object) {
		DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
		Object listener = null;
		if (object instanceof ServletContextAttributeListener) {
			listener = createProxy(handler, ServletContextAttributeListener.class);
		} else if (object instanceof ServletRequestListener) {
			listener = createProxy(handler, ServletRequestListener.class);
		} else if (object instanceof ServletRequestAttributeListener) {
			listener = createProxy(handler, ServletRequestAttributeListener.class);
		} else if (object instanceof HttpSessionListener) {
			listener = createProxy(handler, HttpSessionListener.class);
		} else if (object instanceof HttpSessionAttributeListener) {
			listener = createProxy(handler, HttpSessionAttributeListener.class);
		} else if (object instanceof HttpSessionIdListener) {
			listener = createProxy(handler, HttpSessionIdListener.class);
		}
		if (listener != null) {
			context.addListener((EventListener) listener);
		} else if (object instanceof AbstractContextListener) {
			AbstractContextListener contextListener = (AbstractContextListener) object;
			contextListener.contextInitialized(new ServletContextEvent(context));
		}
		handlers.put(object.getClass().getName(), handler);
	}

	/**
	 * 
	 * Creates a Proxy object
	 * 
	 * @param handler the handler
	 * 
	 * @param clazz   the class
	 * 
	 * @return the Proxy object
	 * 
	 */
	protected Object createProxy(DynamicInvocationHandler handler, Class<?> clazz) {
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { clazz }, handler);
	}

	/**
	 * 
	 * Watches the scripts folder for file changes
	 * 
	 * @param folder the scripts folder
	 * 
	 */
	protected void watch(File folder) {
		new FileWatcher(folder).addFileListener(new FileAdapter() {

			@Override
			public void onCreated(FileEvent event) {
				File file = event.getFile();
				if (file.isFile()) {
					process(file);
				}
			}

			@Override
			public void onModified(FileEvent event) {
				onCreated(event);
			}
		}).watch();
	}

	/**
	 * 
	 * Processes a script file
	 * 
	 * @param script the script file
	 * 
	 */
	protected void process(File script) {
		try {
			logger.log(Level.INFO, "processing script {0}", script.getName());
			process(scriptManager.createObject(script));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "exception when reloading script", e);
		}
	}

	/**
	 * 
	 * Processes an object
	 * 
	 * @param object the given object
	 * @throws ServletException the ServletException
	 * @throws ScriptException  the ScriptException
	 * 
	 */
	protected void process(Object object) throws ServletException, ScriptException {
		if (isServlet(object)) {
			reloadServlet((AbstractServlet) object);
		} else if (isFilter(object)) {
			reloadFilter((AbstractFilter) object);
		} else if (isListener(object)) {
			reload(object);
			if (object instanceof AbstractContextListener) {
				AbstractContextListener contextListener = (AbstractContextListener) object;
				contextListener.contextInitialized(new ServletContextEvent(context));
			}
		} else {
			reloadScripts(scriptManager.getFolder());
		}
	}

	/**
	 * 
	 * Reloads a servlet, filter or a listener into the web container
	 * 
	 * @param object the object to be reloaded
	 * 
	 */
	protected void reload(Object object) {
		DynamicInvocationHandler handler = handlers.get(object.getClass().getName());
		if (handler != null) {
			handler.setTarget(object);
		}
	}

	/**
	 * 
	 * Reloads a servlet
	 * 
	 * @param servlet the servlet to be reloaded
	 * @throws ServletException the ServletException
	 * 
	 */
	protected void reloadServlet(AbstractServlet servlet) throws ServletException {
		reload(servlet);
		DefaultServletConfig config = new DefaultServletConfig();
		config.setServletContext(context);
		Servlet annotation = servlet.getClass().getAnnotation(Servlet.class);
		Arrays.stream(annotation.initParams()).forEach(param -> config.addInitParameter(param.name(), param.value()));
		servlet.init(config);
	}

	/**
	 * 
	 * Reloads a filter
	 * 
	 * @param filter the filter to be reloaded
	 * @throws ServletException the ServletException
	 * 
	 */
	protected void reloadFilter(AbstractFilter filter) throws ServletException {
		reload(filter);
		DefaultFilterConfig config = new DefaultFilterConfig();
		config.setServletContext(context);
		Filter annotation = filter.getClass().getAnnotation(Filter.class);
		Arrays.stream(annotation.initParams()).forEach(param -> config.addInitParameter(param.name(), param.value()));
		filter.init(config);
	}

	/**
	 * 
	 * Reloads the servlets, filters or listeners
	 * 
	 * @param folder the scripts folder
	 * @throws ServletException the ServletException
	 * @throws ScriptException  the ScriptException
	 * 
	 */
	protected void reloadScripts(File folder) throws ServletException, ScriptException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				Object object = scriptManager.createObject(file);
				if (isServlet(object) || isFilter(object) || isListener(object)) {
					process(object);
				}
			} else {
				reloadScripts(file);
			}
		}
	}

	/**
	 * 
	 * Calls the destroy method of the ServletContextListener objects when the
	 * ServletContext is about to be shutdown
	 * 
	 */
	public void stop() {
		handlers.values().forEach(handler -> {
			Object target = handler.getTarget();
			if (target instanceof AbstractContextListener) {
				AbstractContextListener contextListener = (AbstractContextListener) target;
				contextListener.contextDestroyed(new ServletContextEvent(context));
			}
		});
		handlers.clear();
	}

	/**
	 * 
	 * Returns the map of dynamic invocation handlers
	 * 
	 * @return the map of dynamic invocation handlers
	 */
	public Map<String, DynamicInvocationHandler> getHandlers() {
		return Collections.unmodifiableMap(handlers);
	}

	/**
	 * 
	 * Returns the script manager
	 * 
	 * @return the script manager
	 */
	public ScriptManager getScriptManager() {
		return scriptManager;
	}

	/**
	 * 
	 * Checks if the given object is a servlet
	 * 
	 * @param object the given object
	 * 
	 * @return true if the given object is a servlet
	 */
	protected boolean isServlet(Object object) {
		return object.getClass().isAnnotationPresent(Servlet.class);
	}

	/**
	 * 
	 * Checks if the given object is a filter
	 * 
	 * @param object the given object
	 * 
	 * @return true if the given object is a filter
	 */
	protected boolean isFilter(Object object) {
		return object.getClass().isAnnotationPresent(Filter.class);
	}

	/**
	 * 
	 * Checks if the given object is a listener
	 * 
	 * @param object the given object
	 * 
	 * @return true if the given object is a listener
	 */
	protected boolean isListener(Object object) {
		Class<?> clazz = object.getClass();
		return clazz.isAnnotationPresent(ContextListener.class)
				|| clazz.isAnnotationPresent(ContextAttributeListener.class)
				|| clazz.isAnnotationPresent(RequestListener.class)
				|| clazz.isAnnotationPresent(RequestAttributeListener.class)
				|| clazz.isAnnotationPresent(SessionListener.class)
				|| clazz.isAnnotationPresent(SessionAttributeListener.class)
				|| clazz.isAnnotationPresent(SessionIdListener.class);
	}

}