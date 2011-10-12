package org.mash.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;

public class CommandExecutorHarness extends BaseHarness implements RunHarness {
	private static final Logger log = Logger.getLogger(CommandExecutorHarness.class.getName());
	
	private String command;
	private String commandHomeDir;
	private String output;
	private String currentWorkingDir;
	
	private RunResponse response;

	public void run(List<RunHarness> previous, List<SetupHarness> setups) {
		log.info("Running Command Executor Harness");						
		formatCommand();
		changeWorkingDirectory();
		output = runCommand(command);
	}
	
	public void formatCommand() {		
		if(null != commandHomeDir && commandHomeDir.trim().length() > 0) {
			String lastChar = commandHomeDir.substring(commandHomeDir.length()-1);
			if(lastChar.equals(System.getProperty("file.separator")) == false) {
				commandHomeDir = commandHomeDir + System.getProperty("file.separator");
			}
			command = commandHomeDir + command;
		}
	}
	
	public void changeWorkingDirectory() {		
		if(null != currentWorkingDir && currentWorkingDir.trim().length() > 0) {
			runCommand("cd " + currentWorkingDir);
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
		} catch(IOException e) {
			this.getErrors().add(new HarnessError(this, "IO Exception", "IO exception running command: " + com));	
		} catch(InterruptedException e) {
			this.getErrors().add(new HarnessError(this, "Interrupted Exception", "Command unable to finish: " + com));
		} catch(Exception e) {
			this.getErrors().add(new HarnessError(this, "Exception", "Command failed: " + com));
		}		
		return result;
	}		
	
	public RunResponse getResponse() {
		response = new CommandExecutorResponse(output);
		return response;
	}
	
    @HarnessParameter(name = "command")
    public void setCommand(String com) {
        this.command = com;
    }
    
    @HarnessParameter(name = "commandHomeDir")
    public void setCommandHomeDir(String com) {
        this.commandHomeDir = com;
    }
    
    @HarnessParameter(name = "currentWorkingDir")
    public void setCurrentWorkingDir(String com) {
        this.currentWorkingDir = com;
    }
}