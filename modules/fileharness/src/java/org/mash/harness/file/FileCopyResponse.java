package org.mash.harness.file;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.mash.harness.RunResponse;

public class FileCopyResponse implements RunResponse {

	private static final Logger log = Logger.getLogger(FileCopyResponse.class);
	boolean fileCopiedString;
	String fileCopiedName;

	public FileCopyResponse(boolean copied, String fileCopiedName) {
		this.fileCopiedString = copied;		
		this.fileCopiedName = fileCopiedName;
	}

	public String getValue(String name) {		
		if (name.equals("fileCopied")) {
			return Boolean.toString(fileCopiedString);
		} else {
			log.debug("Something requested a value other than file copied.  Returning null");
			return null;
		}
	}

	public Collection<String> getValues() {
		Collection<String> results = new ArrayList<String>();
		results.add(Boolean.toString(fileCopiedString));
		return results;
	}
	
	public Collection<String> getValues(String name) {
		Collection<String> results = new ArrayList<String>();
		if (name.equals("fileCopied")) {			
			results.add(Boolean.toString(fileCopiedString));
			return results;
		} else {
			log.debug("Something requested a value other than file copied.  Returning empty collection");
			return results;
		}
	}
	
	public String getString() {
		return Boolean.toString(this.fileCopiedString);
	}
	
	public String getFileCopiedName() {
		return this.fileCopiedName;
	}


}