package org.mash.harness.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.log4j.Logger;
import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;

/**
 * This harness copies a file
 * 
 * Configurations:
 * 
 * Parameters: 
 * <ul>
 * <li> 'sourceFileName' the name of the original file that should be copied.  Not used if 'targetFileContent' is provided.</li>
 * <li> 'targetFileNameBaseDir' base directory that should be prepended to the targetFileName.</li>
 * <li> 'targetFileName' the name of the output file that should be created.</li>
 * <li> 'targetFileContent' the desired content that should be written to the file system.  Takes priority over sourceFileName.</li>
 * </ul>
 *
 * @author dschultz
 * @since October 4, 2010
 */
public class FileCopyHarness extends BaseHarness implements RunHarness {
	private static final Logger log = Logger.getLogger(FileCopyHarness.class.getName());

	String sourceFileName;
	String targetFileNameBaseDir;
	String targetFileName;
	String targetFileContent;	
	boolean wasSuccessful;
	
	private RunResponse response;
	
	private RandomAccessFile inputFile;
	private RandomAccessFile outputFile;
	
	private int bufferSize = 1024;
	
	public void run(List<RunHarness> previous, List<SetupHarness> setups) {
		log.info("Running File Copy Harness");

		if(log.isDebugEnabled() && null != parameters) {
			String logParamString = "Parameters:";
			for(int i = 0; i < this.parameters.size(); i++) {
				logParamString += "\n\tParameter[" + i + "] name: " + parameters.get(i).getName() + "\t\tValue: " + parameters.get(i).getValue();			
			}		
			log.debug(logParamString);
		}
		run();		
	}
	
	public void run() {
		sourceFileName = getAndRemoveParamValue(this.parameters, "sourceFileName");
		targetFileName = getAndRemoveParamValue(this.parameters, "targetFileName");
		
		targetFileContent = getAndRemoveParamValue(this.parameters, "targetFileContent");
		targetFileNameBaseDir = getAndRemoveParamValue(this.parameters, "targetFileNameBaseDir");
		
		if(null != targetFileNameBaseDir && targetFileNameBaseDir.trim().length() > 0 ) {
			targetFileName = targetFileNameBaseDir + targetFileName; 
		}
		
		if( null != targetFileContent && targetFileContent.trim().length() > 0) {
			wasSuccessful = writeContent(targetFileContent, targetFileName);
		} else if(null != sourceFileName && sourceFileName.trim().length() > 0) {
			wasSuccessful = copyFile(sourceFileName, targetFileName); 
		} else {
			wasSuccessful = false;
		}
	}
	
	public boolean copyFile(String srcFile, String tgtFile) {
		boolean result = false;
		try {
			byte[] data = new byte[bufferSize];
			
			inputFile = new RandomAccessFile(sourceFileName, "r");
			inputFile.seek(0);
			
			outputFile = new RandomAccessFile(targetFileName, "rw");
			int bytesRead = inputFile.read(data);
			while(bytesRead >0 ) {
				outputFile.write(data, 0, bytesRead);
				bytesRead = inputFile.read(data);
			}
			
			File testFile = new File(targetFileName);
			if(testFile.exists()) {
				result = true;
			}
			
		} catch(FileNotFoundException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "File Not Found Exception."));
			result = false;
		} catch (IOException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "IO Exception."));
			result = false;
		} finally {			
			if(null != outputFile) {
				try {
					outputFile.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
			if(null != inputFile) {
				try {
					inputFile.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
			
		}
		return result;
	}
	
	public boolean writeContent(String content, String fileName) {
		boolean result = false;
		try {			
			outputFile = new RandomAccessFile(fileName, "rw");
			outputFile.seek(0);
			outputFile.writeBytes(content);
			File testFile = new File(fileName);
			if(testFile.exists()) {
				result = true;
			}			
		} catch(FileNotFoundException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "File Not Found Exception."));
			result = false;
		} catch (IOException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "IO Exception."));
			result = false;
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
		response = new FileCopyResponse(wasSuccessful, targetFileName);
		return response;
	}

    @HarnessParameter(name = "sourceFileName")
    public void setSourceFile(String path)
    {
        this.sourceFileName = path;
    }
    
    @HarnessParameter(name = "targetFileName")
    public void setTargetFile(String path)
    {
        this.targetFileName = path;
    }
	
}	
	
	
	
	
	
	
	
	
	
