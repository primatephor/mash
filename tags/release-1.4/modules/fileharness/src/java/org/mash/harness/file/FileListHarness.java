package org.mash.harness.file;


import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.io.File;
import java.util.List;

/**
 * Perform a file list.
 *
 * Configurations:
 * <p/>
 * <p/>
 * Parameters are applied to the request, and the request is invoked.  There are special parameters:
 * <ul>
 * <li> 'path' is the path to run a list on</li>
 * </ul>
 *
 * @author dschultz
 * @since Oct 6, 2011
 *
 */
@HarnessName(name = "list_file")
public class FileListHarness extends BaseHarness implements RunHarness {

    private static final Logger log = Logger.getLogger(FileListHarness.class.getName());
    private String path;
    
	private RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups) {
    	log.info("Running File List Harness");		
		response = run();
    }
    
    public RunResponse run() {
    	File dir = new File(path);
    	String[] contentFiles = null;
    	if(dir.isDirectory()) {
    		contentFiles = dir.list();    		
    		for(int i = 0; i < contentFiles.length; i++) {
    			contentFiles[i] = path + System.getProperty("file.separator") + contentFiles[i];    			
    		}
    	}
    	
    	FileListResponse myResponse = new FileListResponse(contentFiles);
    	return myResponse;
    }
    
	public RunResponse getResponse() {		
		return response;
	}

    @HarnessParameter(name = "path")
    public void setPath(String path)
    {
        this.path = path;
    }
}
