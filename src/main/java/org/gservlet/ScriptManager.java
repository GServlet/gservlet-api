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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.codehaus.groovy.control.BytecodeProcessor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.gservlet.annotation.ContextAttributeListener;
import org.gservlet.annotation.ContextListener;
import org.gservlet.annotation.Filter;
import org.gservlet.annotation.RequestAttributeListener;
import org.gservlet.annotation.RequestListener;
import org.gservlet.annotation.Servlet;
import org.gservlet.annotation.SessionActivationListener;
import org.gservlet.annotation.SessionAttributeListener;
import org.gservlet.annotation.SessionBindingListener;
import org.gservlet.annotation.SessionIdListener;
import org.gservlet.annotation.SessionListener;
import groovy.util.GroovyScriptEngine;
import groovy.util.ScriptException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * 
 * An specific class to load Groovy scripts
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class ScriptManager {

	/**
	 * The scripts folder object
	 */
	protected final File folder;

	/**
	 * The GroovyScriptEngine object
	 */
	protected final GroovyScriptEngine engine;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * The list of script listeners
	 */
	protected final List<ScriptListener> listeners = new ArrayList<>();

	/**
	 * 
	 * Constructs a ScriptManager for the given folder
	 * 
	 * @param folder the scripts folder object
	 * @throws ScriptException the ScriptException
	 * 
	 */
	public ScriptManager(File folder) throws ScriptException {
		try {
			this.folder = folder;
			engine = createScriptEngine();
		} catch (MalformedURLException e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * 
	 * Creates an object from a groovy script file
	 * 
	 * @param script the groovy script file
	 * @return the instantiated object
	 * @throws ScriptException the ScriptException
	 * 
	 */
	public Object createObject(File script) throws ScriptException {
		try {
			Class<?> clazz = loadClass(script);
			if (!clazz.isInterface() && isClassSupported(clazz)) {
				Object object = clazz.getConstructor().newInstance();
				listeners.forEach(listener -> listener.onCreated(object));
				return object;
			} else if (!clazz.isInterface()
					&& Stream.of(clazz.getConstructors()).anyMatch(c -> c.getParameterCount() == 0)) {
				return clazz.getConstructor().newInstance();
			}
			return new Object();
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * 
	 * Loads a class from a groovy script file
	 * 
	 * @param file the groovy script file
	 * @return the loaded class
	 * @throws ScriptException the ScriptException
	 * 
	 */
	public Class<?> loadClass(File file) throws ScriptException {
		try {
			String name = file.getAbsolutePath().substring(folder.getAbsolutePath().length() + 1);
			return engine.loadScriptByName(name);
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * 
	 * Creates a GroovyScriptEngine for the given folder
	 * 
	 * @return the groovy script engine
	 * @throws MalformedURLException the MalformedURLException
	 * 
	 */
	protected GroovyScriptEngine createScriptEngine() throws MalformedURLException {
		URL[] urls = { folder.toURI().toURL() };
		GroovyScriptEngine scriptEngine = new GroovyScriptEngine(urls, this.getClass().getClassLoader());
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new LoaderClassPath(scriptEngine.getParentClassLoader()));
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setBytecodePostprocessor(createBytecodeProcessor());
		configuration.setTargetBytecode(CompilerConfiguration.JDK8);
		scriptEngine.setConfig(configuration);
		return scriptEngine;
	}

	/**
	 * 
	 * Creates a BytecodeProcessor for the given ClassPool instance
	 * 
	 * @return the BytecodeProcessor object
	 */
	protected BytecodeProcessor createBytecodeProcessor() {
		return (String name, byte[] original) -> {
			ByteArrayInputStream stream = new ByteArrayInputStream(original);
			try {
				ClassPool classPool = ClassPool.getDefault();
				CtClass ctClass = classPool.makeClass(stream);
				ctClass.detach();
				processClass(ctClass);
				return ctClass.toBytecode();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "exception when processing bytecode", e);
			}
			return original;
		};
	}

	/**
	 * 
	 * Makes the given class extend a superclass based on the declared annotation
	 * 
	 * @param ctClass the class
	 * @throws CannotCompileException the CannotCompileException
	 * @throws NotFoundException      the NotFoundException
	 */
	private void processClass(CtClass ctClass) throws CannotCompileException, NotFoundException {
		ClassPool classPool = ClassPool.getDefault();
		if (ctClass.hasAnnotation(Servlet.class)) {
			ctClass.setSuperclass(classPool.get(AbstractServlet.class.getName()));
		} else if (ctClass.hasAnnotation(Filter.class)) {
			ctClass.setSuperclass(classPool.get(AbstractFilter.class.getName()));
		} else if (ctClass.hasAnnotation(ContextListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractContextListener.class.getName()));
		} else if (ctClass.hasAnnotation(RequestListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractRequestListener.class.getName()));
		} else if (ctClass.hasAnnotation(ContextAttributeListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractContextAttributeListener.class.getName()));
		} else if (ctClass.hasAnnotation(RequestAttributeListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractRequestAttributeListener.class.getName()));
		} else if (ctClass.hasAnnotation(SessionListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractSessionListener.class.getName()));
		} else if (ctClass.hasAnnotation(SessionAttributeListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractSessionAttributeListener.class.getName()));
		} else if (ctClass.hasAnnotation(SessionBindingListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractSessionBindingListener.class.getName()));
		} else if (ctClass.hasAnnotation(SessionActivationListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractSessionActivationListener.class.getName()));
		} else if (ctClass.hasAnnotation(SessionIdListener.class)) {
			ctClass.setSuperclass(classPool.get(AbstractSessionIdListener.class.getName()));
		}
	}

	/**
	 * 
	 * Checks if the declared annotation on the given class is supported
	 * 
	 * @param clazz the given class
	 * @return true if the declared annotation on the given class is supported
	 * 
	 */

	protected boolean isClassSupported(Class<?> clazz) {
		return clazz.getAnnotation(Servlet.class) != null || clazz.getAnnotation(Filter.class) != null
				|| clazz.getAnnotation(ContextListener.class) != null
				|| clazz.getAnnotation(RequestListener.class) != null
				|| clazz.getAnnotation(ContextAttributeListener.class) != null
				|| clazz.getAnnotation(RequestAttributeListener.class) != null
				|| clazz.getAnnotation(SessionListener.class) != null
				|| clazz.getAnnotation(SessionAttributeListener.class) != null
				|| clazz.getAnnotation(SessionBindingListener.class) != null
				|| clazz.getAnnotation(SessionActivationListener.class) != null
				|| clazz.getAnnotation(SessionIdListener.class) != null;
	}

	/**
	 * Registers a new script listener
	 * 
	 * @param listener the ScriptListener object
	 */
	public void addScriptListener(ScriptListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Registers a list of script listeners
	 * 
	 * @param listeners the list of script listeners
	 * 
	 */
	public void addScriptListeners(List<ScriptListener> listeners) {
		this.listeners.addAll(listeners);
	}
	
	/**
	 * 
	 * Returns the list of script listeners
	 * 
	 * @return the list of script listeners
	 */
	public List<ScriptListener> getScriptListeners() {
		return listeners;
	}

}