package org.mash.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;

public class CommandExecutorHarness extends BaseHarness implements RunHarness {
	private static final Logger log = Logger.getLogger(CommandExecutorHarness.class.getName());
	
	String command;
	String commandHomeDir;
	String output;
	private RunResponse response;
	private boolean successful = false;	

	public void run(List<RunHarness> previous, List<SetupHarness> setups) {
		log.info("Running Command Executor Harness");
				
		if (log.isDebugEnabled() && null != parameters) {
			String logParamString = "Parameters:";
			for (int i = 0; i < this.parameters.size(); i++) {
				logParamString += "\n\tParameter[" + i + "] name: " + parameters.get(i).getName() + "\t\tValue: " + parameters.get(i).getValue();
			}
			log.debug(logParamString);
		}
		formatCommand();
		output = runCommand(command);
	}
	
	public void formatCommand() {
		command = getAndRemoveParamValue(this.parameters, "command");
		commandHomeDir = getAndRemoveParamValue(this.parameters, "commandHomeDir");
		
		if(null != commandHomeDir && commandHomeDir.trim().length() > 0) {
			String lastChar = commandHomeDir.substring(commandHomeDir.length()-1);
			if(lastChar.equals(System.getProperty("file.separator")) == false) {
				commandHomeDir = commandHomeDir + System.getProperty("file.separator");
			}
			command = commandHomeDir + command;
		}
	}
	
	public String runCommand(String com) {
		String result = "";
		StringBuilder builder = new StringBuilder();
		try {			
			Process p = Runtime.getRuntime().exec(com);
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			builder.append(line);
			while(line != null) {
				line = reader.readLine();
				builder.append(System.getProperty("line.separator"));
				builder.append(line);
			}
			result = builder.toString();
			successful = true;
		} catch(IOException e) {
			this.getErrors().add(new HarnessError(this, "IO Exception", "IO exception running command" + com));			
			successful = false;
		} catch(InterruptedException e) {
			this.getErrors().add(new HarnessError(this, "Interrupted Exception", "Command unable to finish" + com));			
			successful = false;
		} catch(SecurityException e) {
			this.getErrors().add(new HarnessError(this, "Security Exception", "Command was not allowed" + com));			
			successful = false;
		}
		
		return result;
	}
	
	public String getAndRemoveParamValue(List<Parameter> params, String paramName) {
		String value = "";
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getName().equals(paramName)) {
				value = params.get(i).getValue();
				params.remove(i);
			}
		}
		return value;
	}
	
	public RunResponse getResponse() {
		response = new CommandExecutorResponse(successful, output);
		return response;
	}
}