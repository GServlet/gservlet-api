package org.gservlet;

import java.util.EventObject;

/**
* 
* Events of this kind indicate change events for a file.
* 
* @author Mamadou Lamine Ba
* 
*/

@SuppressWarnings("serial")
public class FileEvent extends EventObject {

	/**
	* 
	* Construct a FileEvent for the given file name.
	* 
	* @param fileName the file name 
	*  
	*/
	public FileEvent(String fileName) {
		super(fileName);
	}

}
