package org.mash.harness.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;

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

    private boolean removeFolder = false;
    private boolean force = false;

    public void run(HarnessContext context) {
        log.info("Running File Delete Harness");

        if (null != fileName) {
            log.info("Deleting file " + fileName);
            if(!deleteFile(fileName)){
                addError("File Delete Harness", "Unable to delete file "+fileName);
            }
        }
        if (null != folderName && !hasErrors()) {
            File deleteMe = new File(folderName);
            log.info("Deleting folder " + folderName);
            if(!this.deleteFolder(deleteMe)){
                addError("File Delete Harness", "Unable to delete contents of folder "+folderName);
            }
            else{
                if(removeFolder){
                    if(!deleteFile(deleteMe)){
                        addError("File Delete Harness", "Unable to delete contents of folder "+folderName);
                    }
                }
            }
        }

        //reporting
        if(folderName == null && fileName == null){
            addError("File Delete Harness", "No file or folder was specified to delete");
        }
    }

    private boolean deleteFile(String fName) {
        File tFile = new File(fName);
        return deleteFile(tFile);
    }

    private boolean deleteFile(File toRemove) {
        boolean result = false;
        Path toRemovePath = toRemove.toPath();
        try {
            String os = System.getProperty("os.name");
            if(force && !os.equalsIgnoreCase("windows"))
            {
                Files.setPosixFilePermissions(toRemovePath, PosixFilePermissions.fromString("rwxrwxrwx"));
            }
            Files.delete(toRemovePath);
            result = true;
        } catch (IOException e) {
            addError("File Delete Harness", e);
            //dump some output
            boolean isReg = Files.isRegularFile(toRemovePath);
            boolean isHid = false;
            try {
                 isHid = Files.isHidden(toRemovePath);
            } catch (IOException e1) {
                log.error("error getting hidden file information");
            }
            boolean isRead = Files.isReadable(toRemovePath);
            boolean isExec = Files.isExecutable(toRemovePath);
            boolean isWrite = Files.isWritable(toRemovePath);
            log.error("Properties of file "+toRemove.getPath()+
                    "\n regular? "+isReg+", hidden? "+isHid+
                    ", isReadable? "+isRead+", isExec? "+isExec+", isWritable? "+isWrite);
        }
        return result;
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
        return new RawResponse(Boolean.toString(!hasErrors()));
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

    @HarnessConfiguration(name = "force")
    public void setForce(String forceDelete)
    {
        this.force = Boolean.parseBoolean(forceDelete);
    }

}