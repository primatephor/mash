package org.mash.harness.file;

import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RawResponse;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Perform a file get.
 * 
 * Configurations:
 * <p/>
 * Parameters:
 * <ul>
 * <li>'path' is the directory to check</li>
 * <li>'file_name' is the filname to get</li>
 * <li>'output_file' is the path for get and put operations to retrieve the file or save it to</li>
 * <li>'file_index' the index (0-x) of the listed files to retrieve</li>
 * <li>'file_contents' retrieve the file with these contents (EXPENSIVE!)</li>
 * </ul>
 * 
 * 
 * @author dschultz
 * @since Oct 5, 2011 9:49:20 AM
 * 
 */
@HarnessName(name = "get_file")
public class FileGetHarness extends BaseHarness implements RunHarness {
	private static final Logger log = Logger.getLogger(FileGetHarness.class.getName());

	private String output_file;

	private Integer fileIndex;
	private List<String> fileContents;
	private String filename;
	private String path;

	private RunResponse response;

	public void run(HarnessContext context) {
		log.info("Running File List Harness");
				
		if (filename != null) {
			log.info("Getting file: " + filename);
			response = retrieve(filename, context);
		} else if (path != null) {
			if (fileContents != null) {
				log.info("Looking for file in path: " + path + " containing: " + fileContents);
				response = retrieveResponseContainment(path, context);
			} else if (fileIndex != null) {
				log.info("Retriving file in: " + path + " at index: " + fileIndex);
				response = retrieveIndexedResponse(path, context);
			}
		}
	}

	protected RunResponse retrieve(String path, HarnessContext context) {
		RunResponse result = null;
		RandomAccessFile inFile;
		try {
			inFile = new RandomAccessFile(path, "r");

			StringBuilder builder = new StringBuilder();
			String line = inFile.readLine();
			while (line != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
				line = inFile.readLine();
			}
			if (output_file == null) {
				result = new RawResponse(builder.toString());
			} else {
				FileCopyHarness copyHarness = new FileCopyHarness();
				copyHarness.setTargetFile(output_file);
				copyHarness.setTargetFile(path);
				copyHarness.run(context);
				result = copyHarness.getResponse();
			}
			inFile.close();
		} catch (FileNotFoundException e) {
			this.getErrors().add(new HarnessError(this.getClass().getName(), "File not found exception: ", e.getMessage()));
		} catch (IOException e) {
			this.getErrors().add(new HarnessError(this.getClass().getName(), "IO exception: ", e.getMessage()));
		}
		return result;
	}

	private RunResponse retrieveResponseContainment(String path, HarnessContext context) {
		RunResponse response = null;
		FileListHarness listHarness = new FileListHarness();
		listHarness.setPath(path);
		FileListResponse ls = (FileListResponse) listHarness.run();
		Map<String, File> files = ls.getFiles();

		log.info("Checking " + files.size() + " files");
		for (File file : files.values()) {
			String filename = path + "/" + file.getName();
			log.info("Retrieving file " + filename);
			RunResponse result = retrieve(filename, context);
			String toCheck = result.getString();

			boolean allContentFound = true;
			for (String fileContent : fileContents) {
				if (!toCheck.contains(fileContent)) {
					allContentFound = false;
					break;
				}
			}
			if (allContentFound) {
				log.info("Found " + fileContents);
				log.debug("Response:" + toCheck);
				response = result;
			}
		}
		return response;
	}

	private RunResponse retrieveIndexedResponse(String path, HarnessContext context) {
		RunResponse response = null;
		FileListHarness listHarness = new FileListHarness();
		listHarness.setPath(path);
		FileListResponse ls = (FileListResponse) listHarness.run();
		Map<String, File> files = ls.getFiles();
		List<File> fileList = new ArrayList<File>(files.values());
		Collections.sort(fileList, new FileDateComparator());
		if (fileList.size() <= fileIndex) {
			getErrors().add(new HarnessError(this,"List","Number of conversations found ("+ fileList.size()+ ") "+ "is less than desired index (out of bounds) "
									+ fileIndex));
		} else {
			File toRetrieve = fileList.get(fileIndex);
			String filename = path + "/" + toRetrieve.getName();
			log.info("Retrieving file " + filename);
			response = retrieve(filename, context);
		}
		return response;
	}

	@HarnessParameter(name = "output_file")
	public void setOutput_file(String output_file) {
		this.output_file = output_file;
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

	public void setFileContents(List<String> fileContents) {
		this.fileContents = fileContents;
	}

	@HarnessParameter(name = "file_name")
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@HarnessParameter(name = "path")
	public void setPath(String path) {
		this.path = path;
	}

	public RunResponse getResponse() {
		return response;
	}

	private class FileDateComparator implements Comparator<File> {
		public int compare(File o1, File o2) {
			Long o1ModTime = o1.lastModified();
			Long o2ModTime = o2.lastModified();
			return o1ModTime.compareTo(o2ModTime);
		}
	}
}
