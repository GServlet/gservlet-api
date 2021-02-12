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
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static org.gservlet.Constants.RELOAD;
import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Checks a folder for file changes and notifies the file
 * listeners accordingly
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class FileWatcher implements Runnable {

	/**
	 * The list of file listeners
	 */
	protected List<FileListener> listeners = new ArrayList<>();
	/**
	 * The folder to be watched
	 */
	protected final File folder;

	/**
	 * The list of watch services
	 */
	protected static final List<WatchService> watchServices = new ArrayList<>();
	
	/**
	 * 
	 * Constructs a FileWatcher for the given folder
	 * 
	 * @param folder the folder object
	 * 
	 */
	public FileWatcher(File folder) {
		this.folder = folder;
	}

	/**
	 * 
	 * Starts the watch process
	 * 
	 */
	public void watch() {
		boolean reload = Boolean.parseBoolean(System.getenv(RELOAD));
		if (folder.exists() && reload) {
			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * 
	 * Used by a daemon thread to start the watch process
	 * 
	 */
	@Override
	public void run() {
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			Path path = Paths.get(folder.getAbsolutePath());
			path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
			watchServices.add(watchService);
			boolean poll = true;
			while (poll) {
				poll = pollEvents(watchService);
			}
		} catch (IOException | InterruptedException | ClosedWatchServiceException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 
	 * Polls for file events
	 * 
	 * @param watchService the watch service
	 * @return the reset flag
	 * @throws InterruptedException the InterruptedException 
	 * 
	 */
	private boolean pollEvents(WatchService watchService) throws InterruptedException {
		WatchKey key = watchService.take();
		Path path = (Path) key.watchable();
		for (WatchEvent<?> event : key.pollEvents()) {
			notifyFileListeners(event.kind(), path.resolve((Path) event.context()).toFile());
		}
		return key.reset();
	}

	/**
	 * 
	 * Notifies the file listeners of a file event
	 * 
	 * @param kind the watch event kind
	 * @param file the file upon which the event occurred upon
	 * 
	 */
	private void notifyFileListeners(WatchEvent.Kind<?> kind, File file) {
		FileEvent event = new FileEvent(file);
		if (kind == ENTRY_CREATE) {
			for (FileListener listener : listeners) {
				listener.onCreated(event);
			}
		} 
		else if (kind == ENTRY_MODIFY) {
			for (FileListener listener : listeners) {
				listener.onModified(event);
			}
		} 
		else if (kind == ENTRY_DELETE) {
			for (FileListener listener : listeners) {
				listener.onDeleted(event);
			}
		}
		if ((kind == ENTRY_CREATE || kind == ENTRY_MODIFY) && file.isDirectory()) {
			new FileWatcher(file).addFileListeners(listeners).watch();
		}
	}

	/**
	 * 
	 * Adds a new file listener
	 * 
	 * @param listener the file listener
	 * @return the file watcher
	 * 
	 */
	public FileWatcher addFileListener(FileListener listener) {
		listeners.add(listener);
		return this;
	}

	/**
	 * 
	 * Removes a file listener
	 * 
	 * @param listener the file listener
	 * @return the file watcher
	 * 
	 */
	public FileWatcher removeFileListener(FileListener listener) {
		listeners.remove(listener);
		return this;
	}

	/**
	 * 
	 * Returns the list of file listeners
	 * 
	 * @return the list of file listeners
	 * 
	 */
	public List<FileListener> getFileListeners() {
		return listeners;
	}

	/**
	 * 
	 * Adds a list of file listeners
	 * 
	 * @param listeners a list of file listeners
	 * @return the file watcher
	 * 
	 */
	public FileWatcher addFileListeners(List<FileListener> listeners) {
		this.listeners = listeners;
		return this;
	}

	/**
	 * 
	 * Returns an unmodifiable list of the watch services
	 * 
	 * @return an unmodifiable list of the watch services
	 * 
	 */
	public static List<WatchService> getWatchServices() {
		return Collections.unmodifiableList(watchServices);
	}
	
}