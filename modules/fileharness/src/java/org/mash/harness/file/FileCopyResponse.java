package org.mash.harness.file;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessName;

@HarnessName(name = "copy_file")
public class FileCopyResponse implements RunResponse {

	private static final Logger log = Logger.getLogger(FileCopyResponse.class);
	private String fileCopiedName;

	public FileCopyResponse(String fileCopiedName) {		
		this.fileCopiedName = fileCopiedName;
	}

	public String getValue(String name) {		
		if (name.equals("fileCopiedName")) {
			return fileCopiedName;
		} else {
			log.debug("Something requested a value other than file copied name.  Returning null");
			return null;
		}
	}

	public Collection<String> getValues() {
		Collection<String> results = new ArrayList<String>();
		results.add(fileCopiedName);
		return results;
	}
	
	public Collection<String> getValues(String name) {
		Collection<String> results = new ArrayList<String>();
		if (name.equals("fileCopiedName")) {			
			results.add(fileCopiedName);
			return results;
		} else {
			log.debug("Something requested a value other than file copied name.  Returning empty collection");
			return results;
		}
	}
	
	public String getString() {
		return fileCopiedName;
	}
	
	public String getFileCopiedName() {
		return this.fileCopiedName;
	}


}