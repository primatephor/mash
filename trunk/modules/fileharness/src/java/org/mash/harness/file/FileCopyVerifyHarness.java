package org.mash.harness.file;

import java.util.List;

import org.apache.log4j.Logger;

import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.VerifyHarness;

import org.mash.loader.HarnessConfiguration;


public class FileCopyVerifyHarness extends BaseHarness implements VerifyHarness {
	
	private static final Logger log = Logger.getLogger(FileCopyVerifyHarness.class.getName());
	
	// configuration value
	private String copied;

	
	public void verify(RunHarness run, List<SetupHarness> setup ) {
		log.info("Verifying File Copy");
		if(run.getResponse() != null) {
			if(run.getResponse() instanceof FileCopyResponse) {
				FileCopyResponse response = (FileCopyResponse) run.getResponse();				
				String compareResult = response.getString();
				if(copied.equals(compareResult) == false) {
					getErrors().add(new HarnessError(this, "File Copy", "Copy file does not exist."));
				}
			} else {
				log.warn("Not verifying response, not a FileCopyResponse:" + run.getResponse().getClass().getName());
                getErrors().add(new HarnessError(this, "Response", "Not verifying response, not a FileCopyResponse:" + run.getResponse().getClass().getName()));
			}
		}
	}
	
    @HarnessConfiguration(name = "copied")
    public void setCopied(String copied)
    {
        this.copied = copied;
    }
}







