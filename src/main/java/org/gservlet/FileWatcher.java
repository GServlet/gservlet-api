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

import static org.gservlet.Constants.RELOAD;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 
 * Checks a folder for file changes and notifies the file listeners accordingly
 * 
 * @author Mamadou Lamine Ba
 * 
 */
public class FileWatcher {

	/**
	 * The list of file listeners
	 */
	protected final List<FileListener> listeners = new ArrayList<>();
	/**
	 * The folder to be watched
	 */
	protected final File folder;

	/**
	 * The list of FileWatcher instances
	 */
	protected static final List<FileWatcher> instances = new ArrayList<>();

	/**
	 * The FileAlterationMonitor instance
	 */
	private final FileAlterationMonitor monitor = new FileAlterationMonitor(1000);

	/**
	 * 
	 * Constructs a FileWatcher for the given folder
	 * 
	 * @param folder the folder to watch
	 * 
	 */
	public FileWatcher(File folder) {
		this.folder = folder;
		instances.add(this);
	}

	/**
	 * 
	 * Starts the watch process
	 * 
	 */
	public void watch() {
		boolean reload = Boolean.parseBoolean(System.getenv(RELOAD));
		if (folder.exists() && reload) {
			try {
				FileAlterationObserver observer = new FileAlterationObserver(folder.getAbsolutePath());
				FileAlterationListener listener = new FileAlterationListenerAdaptor() {

					final Set<File> files = new HashSet<>();

					@Override
					public void onFileCreate(File file) {
						listeners.forEach(listener -> listener.onCreated(new FileEvent(file)));
					}

					@Override
					public void onFileDelete(File file) {
						listeners.forEach(listener -> listener.onDeleted(new FileEvent(file)));
					}

					@Override
					public void onFileChange(File file) {
						if (!files.contains(file)) {
							listeners.forEach(listener -> listener.onModified(new FileEvent(file)));
							files.add(file);
						}
					}

					@Override
					public void onStop(FileAlterationObserver observer) {
						files.clear();
					}

				};
				observer.addListener(listener);
				monitor.addObserver(observer);
				monitor.start();
			} catch (Exception e) {
				// the exception is ignored
			}
		}
	}

	/**
	 * 
	 * Stops the watch process
	 * 
	 */
	public void stop() {
		try {
			monitor.stop();
		} catch (Exception e) {
			// the exception is ignored
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
		this.listeners.addAll(listeners);
		return this;
	}

	/**
	 * 
	 * Returns an unmodifiable list of the FileWatcher instances
	 * 
	 * @return an unmodifiable list of the FileWatcher instances
	 * 
	 */
	public static List<FileWatcher> getInstances() {
		return Collections.unmodifiableList(instances);
	}

}