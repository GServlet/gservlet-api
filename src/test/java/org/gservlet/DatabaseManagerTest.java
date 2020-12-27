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

import static org.gservlet.Constants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DatabaseManagerTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void loadConfiguration() throws Exception {
		File folder = new File("src/test/resources");
		assertEquals(true, folder.exists());
		ServletContext context = mock(ServletContext.class);
		when(context.getAttribute(DATASOURCE)).thenReturn(new BasicDataSource());
		DatabaseManager databaseManager = new DatabaseManager(context);
		File file = new File(folder + "/" + APP_CONFIG_FILE);
		Properties configuration = new Properties();
		FileReader reader = new FileReader(file);
		configuration.load(reader);
		reader.close();
		assertTrue(databaseManager.isConfigurationValid(configuration));
		final Map<Object, Object> map = new HashMap<Object, Object>();
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(context).setAttribute(anyString(), any());
		databaseManager.setupDataSource(configuration);
		assertEquals(map.get(DATASOURCE).getClass(), BasicDataSource.class);
		BasicDataSource dataSource = new BasicDataSource();
		databaseManager.setupDataSource(dataSource);
		assertEquals(map.get(DATASOURCE), dataSource);
		databaseManager.shutDown();
	}

}