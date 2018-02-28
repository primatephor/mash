package org.mash.harness.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessName;
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
@HarnessName(name = "copy_file")
public class FileCopyHarness extends BaseHarness implements RunHarness {
	private static final Logger log = LogManager.getLogger(FileCopyHarness.class.getName());
	private String sourceFileName;
	private String targetFileNameBaseDir;
	private String targetFileName;
	private String targetFileContent;	
	
	private RunResponse response;
	
	private RandomAccessFile inputFile;
	private RandomAccessFile outputFile;
	
	private int bufferSize = 1024;
	private Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");

	public void run(HarnessContext context) {
		log.info("Running File Copy Harness");
		try
		{
			if(null != targetFileNameBaseDir && targetFileNameBaseDir.trim().length() > 0 )
			{
                targetFileNameBaseDir = targetFileNameBaseDir.trim();
                if(!targetFileNameBaseDir.endsWith(File.separator))
                {
                    targetFileNameBaseDir = targetFileNameBaseDir+File.separator;
                }

                log.info("Creating directory "+targetFileNameBaseDir);
                Files.createDirectories(Paths.get(targetFileNameBaseDir), PosixFilePermissions.asFileAttribute(perms));
                Files.setPosixFilePermissions(Paths.get(targetFileNameBaseDir), perms);
                targetFileName = targetFileNameBaseDir + targetFileName;
            }

			if( null != targetFileContent && targetFileContent.trim().length() > 0)
			{
                writeContent(targetFileContent, targetFileName);
            } else if(null != sourceFileName && sourceFileName.trim().length() > 0)
            {
                copyFile(sourceFileName, targetFileName);
            }
		}
		catch (IOException e)
		{
			getErrors().add(new HarnessError(this, "File Copy Harness", "Problem copying file: " + e.getMessage()));
		}
	}		
	
	public void copyFile(String srcFile, String tgtFile) {
		try {
			byte[] data = new byte[bufferSize];

			log.info("Copying file "+srcFile+" to "+tgtFile);
			inputFile = new RandomAccessFile(srcFile, "r");
			inputFile.seek(0);
			
			outputFile = new RandomAccessFile(tgtFile, "rw");
			int bytesRead = inputFile.read(data);
			while(bytesRead >0 ) {
				outputFile.write(data, 0, bytesRead);
				bytesRead = inputFile.read(data);
			}
			
			File testFile = new File(tgtFile);
			if(!testFile.exists())
			{
				throw new Exception("Output file not created");
			}
			Files.setPosixFilePermissions(Paths.get(tgtFile), perms);

		} catch(FileNotFoundException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "File Not Found Exception: " + e.getMessage()));
		} catch (IOException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "IO Exception: " + e.getMessage()));
		} catch (Exception e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "Exception: " + e.getMessage()));
		} finally {			
			if(null != outputFile) {
				try {
					outputFile.close();
				} catch (IOException e) {					
					log.warn("Problem closing output file: " + e.getMessage());
				}
			}
			if(null != inputFile) {
				try {
					inputFile.close();
				} catch (IOException e) {					
					log.warn("Problem closing input file: "+e.getMessage());
				}
			}			
		}
	}
	
	public void writeContent(String content, String fileName) {
		try {			
			outputFile = new RandomAccessFile(fileName, "rw");
			outputFile.seek(0);
			outputFile.writeBytes(content);
			File testFile = new File(fileName);
			if(testFile.exists() == false) {
				throw new Exception("Output file not created");
			}			
		} catch(FileNotFoundException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "File Not Found Exception: " + e.getMessage()));
		} catch (IOException e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "IO Exception: " + e.getMessage()));
		} catch (Exception e) {
			getErrors().add(new HarnessError(this, "File Copy Harness", "Exception: " + e.getMessage()));
		}		
	}
	
	public RunResponse getResponse() {
		response = new FileCopyResponse(targetFileName);
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

    @HarnessParameter(name = "targetFileContent")
    public void setTargetFileContent(String path)
    {
        this.targetFileContent = path;
    }

    @HarnessParameter(name = "targetFileNameBaseDir")
    public void setTargetFileNameBaseDir(String path)
    {
        this.targetFileNameBaseDir = path;
    }
    

}	
	
