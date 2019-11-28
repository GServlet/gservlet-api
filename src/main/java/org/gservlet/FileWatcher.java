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

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWatcher {

	protected final List<FileListener> listeners;
	protected final Logger logger = Logger.getLogger(FileWatcher.class.getName());

	public FileWatcher() {
		listeners = new ArrayList<>();
	}

	public void watch(File folder) {
		if (folder.exists()) {
			start(folder);
		}
	}

	private void start(final File folder) {
		new Thread(new Runnable() {
			public void run() {
				try {
					WatchService watcher = FileSystems.getDefault().newWatchService();
					Path path = Paths.get(folder.getAbsolutePath());
					path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
					WatchKey key;
					while (true) {
						try {
							key = watcher.take();
							for (WatchEvent<?> event : key.pollEvents()) {
								WatchEvent.Kind<?> kind = event.kind();
								String file = event.context().toString();
								if (kind == OVERFLOW) {
									continue;
								} else {
									notifyListeners(kind, file);
								}
							}
							if (!key.reset()) {
								break;
							}
						} catch (InterruptedException e) {
							logger.log(Level.INFO, "exception during watch", e);
							Thread.currentThread().interrupt();
						}
					}
				} catch (IOException e) {
					logger.log(Level.INFO, "exception during watch", e);
				}
			}
		}).start();
	}

	public FileWatcher addListener(FileListener listener) {
		listeners.add(listener);
		return this;
	}

	public FileWatcher removeListener(FileListener listener) {
		listeners.remove(listener);
		return this;
	}

	private void notifyListeners(WatchEvent.Kind<?> kind, String file) {
		if (kind == ENTRY_CREATE) {
			for (FileListener listener : listeners) {
				listener.onCreated(file);
			}
		} else if (kind == ENTRY_DELETE) {
			for (FileListener listener : listeners) {
				listener.onDeleted(file);
			}
		}
	}

	public List<FileListener> getListeners() {
		return listeners;
	}

}