package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
				map.put("file.created", event.getFile().getName());
			}

			@Override
			public void onDeleted(FileEvent event) {
				map.put("file.deleted", event.getFile().getName());
			}

		};
		watcher.addListener(listener).watch();
		assertEquals(1, watcher.getListeners().size());
		wait(2000);
		File file = new File(folder + "/test.txt");
		PrintWriter printWriter = new PrintWriter(new FileWriter(file));
		printWriter.print("Some String");
		printWriter.close();
		wait(2000);
		file.delete();
		wait(2000);
		assertEquals(file.getName(), map.get("file.created"));
		assertEquals(file.getName(), map.get("file.deleted"));
		watcher.removeListener(listener);
		assertEquals(0, watcher.getListeners().size());
	}

	public void wait(int time) throws InterruptedException {
		Thread.sleep(time);
	}

}