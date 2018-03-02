package org.mash.harness.file;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;
import org.mash.harness.RawResponse;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessName;

@HarnessName(name = "delete_file")
public class FileDeleteHarness extends BaseHarness implements RunHarness {
    private static final Logger log = LogManager.getLogger(FileDeleteHarness.class.getName());
    private String fileName;
    private String folderName;

    private RunResponse response;

    boolean successful = true;
    boolean removeFolder = false;

    public void run(HarnessContext context) {
        log.info("Running File Delete Harness");

        if (null != fileName) {
            log.info("Deleting file " + fileName);
            successful = deleteFile(fileName);
        }
        if (null != folderName && successful) {
            File deleteMe = new File(folderName);
            log.info("Deleting folder " + folderName);
            successful = this.deleteFolder(deleteMe);

            if(removeFolder && successful){
                successful = deleteFile(deleteMe);
            }
        }

        //reporting
        if(folderName == null && fileName == null){
            log.error("No file or folder was specified to delete");
            successful = false;
        }
        log.info("Deletion status:"+successful);
    }


    private boolean deleteFile(String fName) {
        File tFile = new File(fName);
        return deleteFile(tFile);
    }

    private boolean deleteFile(File badFile) {
        return badFile.delete();
    }

    public boolean deleteFolderRecursive(File dir) {
        boolean result = true;
        if (dir.isDirectory()) {
            String[] content = dir.list();
            if (content != null) {
                log.info("Deleting " + content.length + " files");
                for (int i = 0; i < content.length && result; i++) {
                    String aContent = content[i];
                    File tempFile = new File(dir, aContent);
                    if (tempFile.isFile()) {
                        result = deleteFile(tempFile);
                    }
                    if (tempFile.isDirectory()) {
                        result = deleteFolder(tempFile);
                    }
                }
            } else {
                log.info("No contents to delete");
            }
        } else if (dir.isFile()) {
            result = deleteFile(dir);
        }
        return result;
    }

    public boolean deleteFolder(File dir) {
        boolean success = false;
        if (dir.isDirectory()) {
            success = deleteFolderRecursive(dir);
            if (!success) {
                log.warn("Unable to delete directory "+dir.getAbsolutePath());
                this.getErrors().add(new HarnessError(this, "Folder Deletion Exception",
                        "Couldn't delete directory: " + dir));
                return false;
            }
            else{
                log.debug("Removed directory "+dir.getAbsolutePath());
            }
        } else {
            log.info(dir.getAbsoluteFile()+" is not a directory");
        }
        return success;
    }

    public RunResponse getResponse() {
        response = new RawResponse(Boolean.toString(successful));
        return response;
    }

    @HarnessConfiguration(name = "fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @HarnessConfiguration(name = "folderName")
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @HarnessConfiguration(name = "removeFolder")
    public void setRemoveFolder(String deleteContent)
    {
        this.removeFolder = Boolean.parseBoolean(deleteContent);
    }
}