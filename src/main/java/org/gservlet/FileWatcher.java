package org.gservlet;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

public class FileWatcher {

	protected final List<FileListener> listeners;

	public FileWatcher() {
		this.listeners = new ArrayList<FileListener>();
	}

	@SuppressWarnings("unchecked")
	public void watch(final File folder) {
		if (folder.exists()) {
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
							} catch (InterruptedException ex) {
								return;
							}
							for (WatchEvent<?> event : key.pollEvents()) {
								WatchEvent.Kind<?> kind = event.kind();
								WatchEvent<Path> ev = (WatchEvent<Path>) event;
								String file = ev.context().toString();
								if (kind == OVERFLOW) {
									continue;
								} else if (kind == ENTRY_CREATE) {
									for (FileListener listener : listeners) {
										listener.onCreated(file);
									}
								} else if (kind == ENTRY_DELETE) {
									for (FileListener listener : listeners) {
										listener.onDeleted(file);
									}
								}
							}
							if (!key.reset()) {
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public FileWatcher addListener(FileListener listener) {
		listeners.add(listener);
		return this;
	}

	public FileWatcher removeListener(FileListener listener) {
		listeners.remove(listener);
		return this;
	}

}