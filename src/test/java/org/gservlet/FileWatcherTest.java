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

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class FileWatcherTest {

	@Test
	public void test() throws IOException, InterruptedException {
		File folder = new File("src/test/resources");
		final Map<String, String> map = new HashMap<>();
		FileWatcher watcher = new FileWatcher(folder);
		FileListener listener = new FileAdapter() {
			@Override
			public void onCreated(FileEvent event) {
				super.onCreated(event);
				map.put("file.created", event.getFile().getName());
			}

			@Override
			public void onDeleted(FileEvent event) {
				super.onDeleted(event);
				map.put("file.deleted", event.getFile().getName());
			}
			
			@Override
			public void onModified(FileEvent event) {
				super.onModified(event);
				map.put("file.modified", event.getFile().getName());
			}
		};
		watcher.addFileListener(listener).watch();
		assertEquals(1, watcher.getFileListeners().size());
		wait(2000);
		File file = new File(folder + "/test.txt");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("Some String");
		}
		wait(2000);
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("Another String");
		}
		wait(2000);
		file.delete();
		wait(2000);
		assertEquals(file.getName(), map.get("file.created"));
		assertEquals(file.getName(), map.get("file.modified"));
		assertEquals(file.getName(), map.get("file.deleted"));
		watcher.removeFileListener(listener);
		assertEquals(0, watcher.getFileListeners().size());
		List<FileListener> listeners = new ArrayList<>();
		listeners.add(listener);
		watcher.addFileListeners(listeners);
		assertEquals(1, watcher.getFileListeners().size());
		assertNotNull(FileWatcher.getInstances());
		assertTrue(FileWatcher.getInstances().size() >= 1);
	}

	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}