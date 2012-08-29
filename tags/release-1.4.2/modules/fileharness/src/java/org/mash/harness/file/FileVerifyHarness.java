package org.mash.harness.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.mash.config.Parameter;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RawResponse;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.HarnessName;

@HarnessName(name = "file")
public class FileVerifyHarness extends BaseHarness implements VerifyHarness {
	private static final Logger log = Logger.getLogger(FileVerifyHarness.class.getName());
	
	private String expected;
	private boolean StringsMatch = false;
	
	public void verify(RunHarness run, List<SetupHarness> setup ) {
		log.info("Running File Verify Harness");
		
		if(run != null && run.getResponse() != null) {
			expected = getAndRemoveParamValue(this.parameters, "expected");
			if(run.getResponse() instanceof FileListResponse) {
				log.debug("Working with a FileListResponse");
				FileListResponse response = (FileListResponse) run.getResponse();
				ArrayList<String> fileNames = (ArrayList<String>) response.getValues();
				for(int i = 0; i < fileNames.size(); i++) {					
					String fileName = fileNames.get(i);
					log.debug("File Verify Harness checking file: " + fileName);
					String myFileContent = getFileContent(fileName);
					StringsMatch = stringsMatch(expected, myFileContent, getParameterValues(parameters));
					if(StringsMatch) {
						log.debug("File Verify Harness found a matching file: " + fileName);
						break;
					}
				}
			
			} else if( run.getResponse() instanceof FileCopyResponse) {
				log.debug("Working with a FileCopyResponse");
				FileCopyResponse response = (FileCopyResponse) run.getResponse();
				
				String myFileContent = getFileContent(response.getFileCopiedName());
				StringsMatch = stringsMatch(expected, myFileContent, getParameterValues(parameters)); 
			
			} else if( run.getResponse() instanceof RawResponse) {
				log.debug("Working with a RawResponse");
				RawResponse response = (RawResponse) run.getResponse();
				StringsMatch = stringsMatch(expected, response.getString(), getParameterValues(parameters)); 
			} else {
				getErrors().add(new HarnessError(this, "File Verify Harness", "Verifying something that didn't involve files:" + run.getResponse().getClass().getName()));
			}
		}
		if(StringsMatch == false) {
			getErrors().add(new HarnessError(this, "File Verify Harness", "File not verified:" + run.getResponse().getClass().getName()));
		}
		
	}
	
	public String getFileContent(String fileName) {
		String result = null;
		
		try {
			StringBuilder builder = new StringBuilder();
			RandomAccessFile inputFile;			
			
			inputFile = new RandomAccessFile(fileName, "r");
			inputFile.seek(0);
			
			String curLine = inputFile.readLine();
			
			while(curLine != null) {				
				builder.append(curLine);
				builder.append(System.getProperty("line.separator"));
				curLine = inputFile.readLine();
			}
			
			result = builder.toString();
		} catch (FileNotFoundException e) {
			getErrors().add(new HarnessError(this, "File Verify Harness", "File not found:" + fileName));
		} catch (IOException e) {
			getErrors().add(new HarnessError(this, "File Verify Harness", "IO Exception:" + e.getMessage()));
		}				
		return result;
	}
	
	private ArrayList<Integer> getDifferencesIndices(String expected, String given) {
		ArrayList<Integer> diffIndices = new ArrayList<Integer>();
		
		for (int charCounter = 0; charCounter < expected.length(); charCounter++) {
			char givenChar = given.charAt(charCounter);
			char expectedChar = expected.charAt(charCounter);
			if (givenChar != expectedChar) {
				diffIndices.add(charCounter);
				log.debug("Strings differ at these indices: " + charCounter);
			}
		}
		return diffIndices;
	}
	
	private ArrayList<String>  getDifferencesList(String expected, ArrayList<Integer> list) {
		int prevIndex = -1;
		int currIndex = -1;
		ArrayList<String> foundExceptions = new ArrayList<String>();;	
		StringBuilder currentException = new StringBuilder();
		
		for (int i = 0; i < list.size(); i++) {
			currIndex = list.get(i);

			if (prevIndex < 0 || currIndex == prevIndex + 1) {
				currentException.append(expected.charAt(currIndex));
			} else {
				foundExceptions.add(currentException.toString());
				currentException = new StringBuilder();
				currentException.append(expected.charAt(currIndex));
			}
			prevIndex = currIndex;
		}
		if (currentException.length() > 0) {
			foundExceptions.add(currentException.toString());
		}
		return foundExceptions;
	}
	
	private boolean areFoundDifferencesAccepted(ArrayList<String> allowed, ArrayList<String> found) {
		boolean result = true;
		for (int i = 0; i < found.size(); i++) {
			log.info("Difference " + i + " found: " + found.get(i));
			if ((allowed.contains(found.get(i)) == false)) {
				return false;
			}
		}
		return result;
	}
	
	public boolean stringsMatch(String expected, String given, ArrayList<String> allowedExceptions) {
		log.debug("Expected:\n" + expected);
		log.debug("Given:\n" + given);
		
		expected = expected.trim();
		given = given.trim();
		
		boolean result = true;
		if (null == given && null == expected) {
			log.debug("Given two nulls.");
			result = true;
		} else if (null == given || null == expected) {
			log.debug("Given one null.");
			result = false;
		} else if (null == allowedExceptions || allowedExceptions.size() == 0) {
			log.debug("Not given any allowed exceptions, just using String.equals() method.");
			result = given.trim().equals(expected.trim());
		} else if(given.trim().length() != expected.trim().length()) {
			log.debug("Just comparing lengths.  Given Length: " + given.trim().length() + "\tExpected Length: " + expected.trim().length());
			result = false;
		} else {
			log.debug("Doing a full comparison with allowed differences.");
			expected = expected.trim();
			given = given.trim();
			ArrayList<Integer> diffIndices = getDifferencesIndices(expected, given);
			ArrayList<String> foundExceptions = getDifferencesList(expected, diffIndices);
			result = areFoundDifferencesAccepted(allowedExceptions, foundExceptions);
		}
		return result;
	}
	
	public ArrayList<String> getParameterValues(List<Parameter> params) {
		ArrayList<String> values = new ArrayList<String>();		
		for(int i = 0; i < params.size(); i++) {
			values.add(params.get(i).getValue());
		}		
		return values;
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
	
}


