package com.java.main;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class JavaWatchServiceDemo {
	private Path path = null;
	private WatchService watchService = null;

	private void initialize() {
		path = Paths.get("E:\\test"); // get the directory which needs to be watched.
		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, ENTRY_CREATE, ENTRY_DELETE,ENTRY_MODIFY); 
			// register the watch service on the path.
			// ENTRY_CREATE  -  register file create event,
			// ENTRY_DELETE  -  register the delete event,
			// ENTRY_MODIFY  -  register the file modified event
		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		}
	}

	/**
	 * Once it added to the watch list it will start to monitor the changes on
	 * the directory
	 */
	private void doMonitor() {
		WatchKey key = null;
		while (true) { // important - create an indefinite loop to watch the file system changes.
			try {
				key = watchService.take();
				for (WatchEvent<?> event : key.pollEvents()) {
					Kind<?> kind = event.kind();
					System.out.println("contents count: " + path.getNameCount());
					System.out.println("Event on " + path.toString()+event.context().toString() + " is " + kind.name());
				}
			} catch (InterruptedException e) {
				System.out.println("InterruptedException: " + e.getMessage());
			}
			boolean reset = key.reset();
			if (!reset)
				break;
		}
	}

	public static void main(String[] args) {
		JavaWatchServiceDemo watchservicedemo = new JavaWatchServiceDemo();
		watchservicedemo.initialize();
		watchservicedemo.doMonitor();
	}
}