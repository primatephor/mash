package org.mash.harness.file;

import org.mash.harness.RunResponse;
import org.mash.tool.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author dschultz
 * @since Oct 06, 2010
 * 
 */
public class FileListResponse implements RunResponse {
	private Map<String, File> files;

	public FileListResponse(String[] filesNames) {
		if (filesNames != null) {
			for (String fileName : filesNames) {
				this.getFiles().put(fileName, new File(fileName));
			}
		}
	}
	
	public FileListResponse(File[] files) {
		if (files != null) {
			for (File file : files) {
				this.getFiles().put(file.getName(), file);
			}
		}
	}

	public String getValue(String name) {
		String result = null;
		if (this.getFiles().get(name) != null) {
			result = this.getFiles().get(name).getName();
		}
		return result;
	}

	public Collection<String> getValues(String name) {
		return getValues();
	}

	public String getString() {
		StringBuffer result = new StringBuffer();
		for (String filename : getValues()) {
			result.append(filename).append("\n");
		}
		return result.toString();
	}

	public Collection<String> getValues() {
		List<String> result = new ArrayList<String>();
		for (String fileName : getFiles().keySet()) {
			result.add(fileName);
		}
		Collections.sort(result, new FileNameComparator());
		return result;
	}

	public Map<String, File> getFiles() {
		if (files == null) {
			files = new HashMap<String, File>();
		}
		return files;
	}

	private class FileNameComparator implements Comparator<String> {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	}

	@Override
	public String toString() {
		return StringUtil.toString(getValues());
	}
}
