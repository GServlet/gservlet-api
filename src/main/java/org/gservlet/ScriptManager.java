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

import static org.gservlet.Constants.SCRIPTS_FOLDER;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.BytecodeProcessor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.gservlet.annotation.ContextAttributeListener;
import org.gservlet.annotation.ContextListener;
import org.gservlet.annotation.Filter;
import org.gservlet.annotation.RequestAttributeListener;
import org.gservlet.annotation.RequestListener;
import org.gservlet.annotation.Servlet;
import org.gservlet.annotation.SessionAttributeListener;
import org.gservlet.annotation.SessionBindingListener;
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
 * An specific class to load Groovy scripts.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class ScriptManager {

	/**
	 * The groovy script engine object
	 */
	protected final GroovyScriptEngine engine;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(ScriptManager.class.getName());

	/**
	 * 
	 * Constructs a ScriptManager for the given folder
	 * 
	 * @param folder the folder object
	 * @throws MalformedURLException the MalformedURLException
	 * 
	 */
	public ScriptManager(File folder) throws MalformedURLException {
		engine = createScriptEngine(folder);
	}

	/**
	 * 
	 * Loads a script file
	 * 
	 * @param file the script file
	 * @return the instantiated object
	 * @throws ScriptException the ScriptException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Object loadScript(File file) throws ScriptException {
		try {
			String path = file.getAbsolutePath();
			int index = path.indexOf(SCRIPTS_FOLDER) + SCRIPTS_FOLDER.length() + 1;
			return engine.loadScriptByName(path.substring(index)).getConstructor().newInstance();
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

	/**
	 * 
	 * Constructs a GroovyScriptEngine for the given folder
	 * 
	 * @param folder the folder object
	 * @return the groovy script engine
	 * @throws MalformedURLException the MalformedURLException
	 * 
	 */
	protected GroovyScriptEngine createScriptEngine(File folder) throws MalformedURLException {
		URL[] urls = { folder.toURI().toURL() };
		GroovyScriptEngine scriptEngine = new GroovyScriptEngine(urls, this.getClass().getClassLoader());
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new LoaderClassPath(scriptEngine.getParentClassLoader()));
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setBytecodePostprocessor(createBytecodeProcessor(classPool));
		scriptEngine.setConfig(configuration);
		return scriptEngine;
	}

	/**
	 * 
	 * Construct a bytecodeProcessor for the given classPool
	 * 
	 * @param classPool the classPool object
	 * @return the bytecodeProcessor
	 */
	protected BytecodeProcessor createBytecodeProcessor(final ClassPool classPool) {
		return new BytecodeProcessor() {
			public byte[] processBytecode(String name, byte[] original) {
				ByteArrayInputStream stream = new ByteArrayInputStream(original);
				try {
					CtClass ctClass = classPool.makeClass(stream);
					ctClass.detach();
					for (Object annotation : ctClass.getAnnotations()) {
						processClass(classPool, ctClass, annotation.toString());
					}
					return ctClass.toBytecode();
				} catch (Exception e) {
					logger.log(Level.INFO, "exception during processBytecode method", e);
				}
				return original;
			}
		};
	}

	/**
	 * 
	 * Changes the bytecode of the given class based on the value of the annotation
	 * 
	 * @param classPool  the classPool object
	 * @param ctClass    the class
	 * @param annotation the annotation
	 * @throws IOException            the IOException
	 * @throws CannotCompileException the CannotCompileException
	 * @throws NotFoundException      the NotFoundException
	 */
	protected void processClass(ClassPool classPool, CtClass ctClass, String annotation)
			throws IOException, CannotCompileException, NotFoundException {
		if (annotation.indexOf(Servlet.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractServlet.class.getName()));
		} else if (annotation.indexOf(Filter.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractFilter.class.getName()));
		} else if (annotation.indexOf(ContextListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractContextListener.class.getName()));
		} else if (annotation.indexOf(RequestListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractRequestListener.class.getName()));
		} else if (annotation.indexOf(ContextAttributeListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractContextAttributeListener.class.getName()));
		} else if (annotation.indexOf(RequestAttributeListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractRequestAttributeListener.class.getName()));
		} else if (annotation.indexOf(SessionListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractSessionListener.class.getName()));
		} else if (annotation.indexOf(SessionAttributeListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractSessionAttributeListener.class.getName()));
		} else if (annotation.indexOf(SessionBindingListener.class.getName()) != -1) {
			ctClass.setSuperclass(classPool.get(AbstractSessionBindingListener.class.getName()));
		}
	}

}