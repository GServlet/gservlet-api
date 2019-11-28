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
		final Map<String, String> map = new HashMap<String, String>();
		FileWatcher watcher = new FileWatcher(folder);
		watcher.addListener(new FileAdapter() {
			@Override
			public void onCreated(String name) {
				map.put("file.created", name);
			}

			@Override
			public void onDeleted(String name) {
				map.put("file.deleted", name);
			}

		}).watch();
		assertEquals(1, watcher.getListeners().size());
		Thread.sleep(2000);
		File file = new File("src/test/resources/test.txt");
		PrintWriter printWriter = new PrintWriter(new FileWriter(file));
		printWriter.print("Some String");
		printWriter.close();
		Thread.sleep(2000);
		file.delete();
		Thread.sleep(2000);
		assertEquals(file.getName(), map.get("file.created"));
		assertEquals(file.getName(), map.get("file.deleted"));
	}

}