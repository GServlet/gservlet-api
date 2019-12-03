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

/**
 * 
 * The FileWatcher class checks a folder for file changes and notifies its listeners accordingly.
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class FileWatcher implements Runnable {

	/**
	 * The list of listeners
	 */
	protected final List<FileListener> listeners;
	/**
	 * The folder to be watched
	 */
	protected final File folder;
	/**
	 * The logger object
	 */
	protected final Logger logger = Logger.getLogger(FileWatcher.class.getName());

	/**
	* 
	* Constructs a FileWatcher for the given folder
	* 
	* @param folder the folder object 
	*  
	*/
	public FileWatcher(File folder) {
		listeners = new ArrayList<>();
		this.folder = folder;
	}

	/**
	* 
	* Starts the watch process
	* 
	*  
	*/
	public void watch() {
		if (folder.exists()) {
			new Thread(this).start();
		}
	}

	/**
	* 
	* Used to create a thread for the watch process
	* 
	*  
	*/
	@Override
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(folder.getAbsolutePath());
			path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
			boolean poll = true;
			while (poll) {
				poll = pollEvents(watcher);
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "exception during watch", e);
		}
	}

	/**
	* 
	* Polls for file events
	* @param watchService the watch service
	*  
	*/
	private boolean pollEvents(WatchService watchService) {
		try {
			WatchKey key = watchService.take();
			for (WatchEvent<?> event : key.pollEvents()) {
				notifyListeners(event.kind(), event.context().toString());
			}
			return key.reset();
		} catch (InterruptedException e) {
			logger.log(Level.INFO, "exception during watch", e);
			Thread.currentThread().interrupt();
		}
		return false;
	}

	/**
	* 
	* Registers new listener
	* 
	* @param listener the file listener object
	* @return the file watcher instance
	* 
	*/
	public FileWatcher addListener(FileListener listener) {
		listeners.add(listener);
		return this;
	}

	/**
	* 
	* Unregisters a listener
	* 
	* @param listener the file listener object
	* @return the file watcher instance
	* 
	*/
	public FileWatcher removeListener(FileListener listener) {
		listeners.remove(listener);
		return this;
	}

	/**
	* 
	* Notifies the listeners of a file event
	* 
	* @param kind the kind of event
	* @param the name of the file
	* 
	*/
	private void notifyListeners(WatchEvent.Kind<?> kind, String file) {
		FileEvent event = new FileEvent(file);
		if (kind == ENTRY_CREATE) {
			for (FileListener listener : listeners) {
				listener.onCreated(event);
			}
		} else if (kind == ENTRY_DELETE) {
			for (FileListener listener : listeners) {
				listener.onDeleted(event);
			}
		}
	}

	/**
	* 
	* Returns a list of listeners
	* 
	* @return a list of listeners
	* 
	*/
	public List<FileListener> getListeners() {
		return listeners;
	}

}