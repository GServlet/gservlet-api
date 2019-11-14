package org.gservlet;

public interface FileListener {

	public void onCreated(String fileName);
	public void onDeleted(String fileName);
	
}