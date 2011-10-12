package org.mash.harness.file;

import org.apache.log4j.Logger;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.wait.PollingWaitHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Poll a directory until a file is present.  Basically just run a 'ls' command on an ftp server
 * at the specified path.  If there are files (or file), then polling is done.
 * <p/>
 * Configurations:
 * <ul>
 * <li> 'timeout' optional time in milliseconds to stop polling (default timeout = 1 minute) </li>
 * <li> 'polltime' optional time in milliseconds to poll remote server (default poll time is 5 seconds) </li>
 * </ul>
 * <p/>
 * Parameters:
 * <ul>
 * <li> 'path' of file or directory to check </li>
 * <li> 'size' number of files to wait for, default is 1 </li>
 * <li> 'file_size' expected total size of all files</li>
 * <li> 'file_index' files will be sorted by size.  Gets the file at the given position</li>
 * <li> 'file_contents' poll until a file is found with the defined contents</li>
 * <li> 'file_name' poll until a file is found with the given name</li>
 * </ul>
 *		
	@HarnessParameter(name = "file_name")
	
 *
 *
 *
 * @author dschultz
 * @since Oct 06, 2011
 */
public class FileWaitHarness extends PollingWaitHarness
{
    private static final Logger log = Logger.getLogger(FileWaitHarness.class.getName());

    private String path;

    //for lists
    private Long fileSize;
    private Integer size = 1;

    //for contents
    private Integer fileIndex;
    private List<String> fileContents;
    private String filename;

    private RunHarness run;

	public boolean poll(List<RunHarness> previous, List<SetupHarness> setups) {
		log.debug("Polling in FileWaitHarness");
		run = buildRunHarness();
		run.run(previous, setups);
		boolean result = false;
		RunResponse runResponse = getResponse();
		if (runResponse != null) {
			if (runResponse instanceof FileListResponse) {
				FileListResponse listResponse = (FileListResponse) runResponse;
				if (fileSize != null) {
					log.debug("File size is not null");
					long totalSize = 0;
					for (File file : listResponse.getFiles().values()) {
						totalSize += file.length();
					}
					log.info("Found " + listResponse.getFiles().size() + " files, waiting for " + size);
					log.info("Found " + totalSize + " bytes, waiting for " + fileSize);
					result = listResponse.getFiles().size() >= size && totalSize >= fileSize;
				} else {
					log.debug("File size is null");
					log.info("Found " + listResponse.getFiles().size() + " files, waiting for " + size);
					result = listResponse.getFiles().size() >= size;
				}
			} else {
				// it's not null, so something matching was found
				result = true;
			}
		} else {
			log.debug("No response found in list");
		}
		return result;
	}

	protected RunHarness buildRunHarness() {
		if (run == null) {
			if (isGetHarness()) {
				FileGetHarness getHarness = new FileGetHarness();
				getHarness.setPath(path);
				getHarness.setFileContents(fileContents);
				if (fileIndex != null) {
					getHarness.setFileIndex(String.valueOf(fileIndex));
				}
				getHarness.setFilename(filename);
				run = getHarness;
			} else {
				FileListHarness listHarness = new FileListHarness();
				listHarness.setPath(path);
				run = listHarness;
			}
		}
		return run;
	}

	public boolean isGetHarness() {
		return fileContents != null || fileIndex != null || filename != null;
	}

	protected HarnessError buildPollingFailureError() {
		HarnessError result;
		StringBuilder errorData = new StringBuilder();
		errorData.append("Timed out");
		if (isGetHarness()) {
			errorData.append(" retrieving file");
		} else {
			errorData.append(" listing files");
		}
		if (path != null) {
			errorData.append(" on path ").append(path);
		}
		if (filename != null) {
			errorData.append(" with filename ").append(filename);
		}
		if (fileIndex != null) {
			errorData.append(" with index ").append(fileIndex);
		}
		if (fileContents != null && fileContents.size() > 0) {
			errorData.append(" containing data '");
			for (int i = 0; i < fileContents.size(); i++) {
				errorData.append(fileContents.get(i));
				if (i + 1 == fileContents.size()) {
					errorData.append(",");
				}
			}
			errorData.append("'");
		}
		result = new HarnessError(this, "File Polling Wait", errorData.toString());
		return result;
	}

	public RunResponse getResponse() {
		RunResponse response = null;
		if (run != null) {
			response = run.getResponse();
		}
		return response;
	}

	@HarnessConfiguration(name = "timeout")
	public void setTimeoutMillis(String timeoutMillis) {
		super.setTimeoutMillis(timeoutMillis);
	}

	@HarnessConfiguration(name = "polltime")
	public void setPollMillis(String pollMillis) {
		super.setPollMillis(pollMillis);
	}

	@HarnessParameter(name = "path")
	public void setPath(String path) {
		this.path = path;
	}

	@HarnessParameter(name = "size")
	public void setSize(String size) {
		this.size = Integer.valueOf(size);
	}

	@HarnessParameter(name = "file_size")
	public void setFileSize(String fileSize) {
		this.fileSize = Long.valueOf(fileSize);
	}
	
	@HarnessParameter(name = "file_index")
	public void setFileIndex(String fileIndex) {
		this.fileIndex = Integer.valueOf(fileIndex);
	}

	@HarnessParameter(name = "file_contents")
	public void setFileContents(String fileContents) {
		if (this.fileContents == null) {
			this.fileContents = new ArrayList<String>();
		}
		this.fileContents.add(fileContents);
	}

	@HarnessParameter(name = "file_name")
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
