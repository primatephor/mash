package org.mash.util;

import java.util.List;

import org.apache.log4j.Logger;

import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.VerifyHarness;

import org.mash.loader.HarnessConfiguration;


public class CommandExecutorVerifyHarness extends BaseHarness implements VerifyHarness {
	
	private static final Logger log = Logger.getLogger(CommandExecutorVerifyHarness.class.getName());
	
	// configuration value
	private boolean configSuccess;
	private String configOutput;
	
	public void verify(RunHarness run, List<SetupHarness> setup ) {
		log.info("Verifying Command Execution");
		if(run.getResponse() != null) {
			if(run.getResponse() instanceof CommandExecutorResponse) {
				CommandExecutorResponse response = (CommandExecutorResponse) run.getResponse();				
				
				String responseOutput = response.getValue("output");
				verifyOutput(responseOutput);
								
				String responseSuccess = response.getValue("wasSuccess");
				verifySuccessfulRun(responseSuccess);				
				
			} else {
				log.warn("Not verifying response, not a CommandExecutorResponse:" + run.getResponse().getClass().getName());
                getErrors().add(new HarnessError(this, "Response", "Not verifying response, not a CommandExecutorResponse:" + run.getResponse().getClass().getName()));
			}
		}
	}
	
	public void verifyOutput(String out) {
		if(null != out && null != configOutput) {
			if(configOutput.equals(out) == false) {
				getErrors().add(new HarnessError(this, "Command Executor", "Command output did not match desired output."));
			}
		} else if(null != configOutput && null == out ) {
			getErrors().add(new HarnessError(this, "Command Executor", "Desired output was provided, but no execution output was detected."));
		}
	}
	
	public void verifySuccessfulRun(String run) {
		if(null == run) {
			getErrors().add(new HarnessError(this, "Command Executor","Desired success was provided, but no execution success was detected."));
		} else if(run.equals(Boolean.toString(configSuccess)) == false) {
			getErrors().add(new HarnessError(this, "Command Executor","Command success did not match desired."));
		}		
	}
	
    @HarnessConfiguration(name = "succeeded")
    public void setSuccess(String succeeded)
    {
        this.configSuccess = Boolean.parseBoolean(succeeded);
    }
    
    @HarnessConfiguration(name = "output")
    public void setOutput(String output)
    {
        this.configOutput = output;
    }
}
