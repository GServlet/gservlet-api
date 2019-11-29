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
import org.gservlet.annotation.SessionListener;
import groovy.util.GroovyScriptEngine;
import groovy.util.ScriptException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class ScriptManager {

	protected final GroovyScriptEngine engine;
	protected final Logger logger = Logger.getLogger(ScriptManager.class.getName());

	public ScriptManager(File folder) throws MalformedURLException {
		engine = createScriptEngine(folder);
	}

	public Object loadScript(String name) throws ScriptException {
		try {
			return engine.loadScriptByName(name).newInstance();
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

	protected GroovyScriptEngine createScriptEngine(File folder) throws MalformedURLException {
		URL[] urls = { folder.toURI().toURL(),
				ScriptManager.class.getClassLoader().getResource(Constants.SCRIPTS_FOLDER) };
		GroovyScriptEngine gse = new GroovyScriptEngine(urls, this.getClass().getClassLoader());
		ClassPool classPool = ClassPool.getDefault();
		classPool.insertClassPath(new LoaderClassPath(gse.getParentClassLoader()));
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.setBytecodePostprocessor(createBytecodeProcessor(classPool));
		gse.setConfig(configuration);
		return gse;
	}

	protected BytecodeProcessor createBytecodeProcessor(final ClassPool classPool) {
		return new BytecodeProcessor() {
			public byte[] processBytecode(String name, byte[] original) {
				ByteArrayInputStream stream = new ByteArrayInputStream(original);
				try {
					CtClass clazz = classPool.makeClass(stream);
					clazz.detach();
					for (Object annotation : clazz.getAnnotations()) {
						byte[] transformed = processClass(classPool, clazz, annotation.toString());
						if(transformed!=null) {
							return transformed;
						}
					}
				} catch (Exception e) {
					logger.log(Level.INFO, "exception during processBytecode method", e);
				}
				return original;
			}
		};
	}

	protected byte[] processClass(ClassPool classPool, CtClass clazz, String annotation)
			throws IOException, CannotCompileException, NotFoundException {
		if (annotation.indexOf(Servlet.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractServlet.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(Filter.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractFilter.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(ContextListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractContextListener.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(RequestListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractRequestListener.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(ContextAttributeListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractContextAttributeListener.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(RequestAttributeListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractRequestAttributeListener.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(SessionListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractSessionListener.class.getName()));
			return clazz.toBytecode();
		} else if (annotation.indexOf(SessionAttributeListener.class.getName()) != -1) {
			clazz.setSuperclass(classPool.get(AbstractSessionAttributeListener.class.getName()));
			return clazz.toBytecode();
		}
		return null;
	}
}