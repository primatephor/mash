package org.mash.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.RunResponse;

public class CommandExecutorResponse implements RunResponse {
	private static final Logger log = LogManager.getLogger(CommandExecutorResponse.class.getName());
	String output;

	public CommandExecutorResponse(String output) {
		this.output = output;		
	}
	
	public String getValue(String name) {		
		if (name.equals("output")) {
			return output;
		} else {
			log.warn("Something requested a value other than output.  Returning null");
			return null;
		}
	}
	
	public Collection<String> getValues() {
		Collection<String> results = new ArrayList<String>();
		results.add(getValue("output"));
		results.add(getValue("wasSuccess"));
		return results;
	}
	
	public Collection<String> getValues(String name) {
		Collection<String> results = new ArrayList<String>();
		if (name.equals("output")) {			
			results.add(getValue("output"));
			return results;
		} else if(name.equals("wasSuccess")) {
			results.add(getValue("wasSuccess"));
			return results;
		} else {
			log.warn("Something requested a value other than file output or wasSuccess.  Returning empty collection");
			return results;
		}
	}
	
	public String getString() {
		return this.output;
	}
}
