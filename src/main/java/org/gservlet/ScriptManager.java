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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.groovy.control.BytecodeProcessor;
import org.codehaus.groovy.control.CompilerConfiguration;
import groovy.util.GroovyScriptEngine;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

public class ScriptManager {

	protected final GroovyScriptEngine engine;
	protected final Logger logger = Logger.getLogger(ScriptManager.class.getName());

	public ScriptManager(File folder) throws Exception {
		engine = createScriptEngine(folder);
	}

	public Object loadScript(String name) throws Exception {
		return engine.loadScriptByName(name).newInstance();
	}

	protected GroovyScriptEngine createScriptEngine(File folder) throws Exception {
		URL[] urls = { folder.toURI().toURL(),
				ScriptManager.class.getClassLoader().getResource(Constants.SCRIPTS_FOLDER) };
		GroovyScriptEngine engine = new GroovyScriptEngine(urls, this.getClass().getClassLoader());
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new LoaderClassPath(engine.getParentClassLoader()));
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setBytecodePostprocessor(createBytecodeProcessor(classPool));
		engine.setConfig(configuration);
		return engine;
	}

	protected BytecodeProcessor createBytecodeProcessor(final ClassPool classPool) {
		return new BytecodeProcessor() {
			public byte[] processBytecode(String name, byte[] original) {
				ByteArrayInputStream stream = new ByteArrayInputStream(original);
				try {
					CtClass clazz = classPool.makeClass(stream);
					clazz.detach();
					Object[] annotations = clazz.getAnnotations();
					for (Object annotation : annotations) {
						String value = annotation.toString();
						if (value.indexOf("Servlet") != -1) {
							clazz.setSuperclass(classPool.get(HttpServlet.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("Filter") != -1) {
							clazz.setSuperclass(classPool.get(Filter.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("ContextListener") != -1) {
							clazz.setSuperclass(classPool.get(ServletContextListener.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("RequestListener") != -1) {
							clazz.setSuperclass(classPool.get(ServletRequestListener.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("ContextAttributeListener") != -1) {
							clazz.setSuperclass(classPool.get(ServletContextAttributeListener.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("RequestAttributeListener") != -1) {
							clazz.setSuperclass(classPool.get(ServletRequestAttributeListener.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("SessionListener") != -1) {
							clazz.setSuperclass(classPool.get(HttpSessionListener.class.getName()));
							return clazz.toBytecode();
						} else if (value.indexOf("SessionAttributeListener") != -1) {
							clazz.setSuperclass(classPool.get(HttpSessionAttributeListener.class.getName()));
							return clazz.toBytecode();
						}
					}
				} catch (Exception e) {
					logger.log(Level.INFO, "exception during processBytecode method", e);
				}
				return original;
			}
		};
	}
}