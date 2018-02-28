package org.mash.harness.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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
public class FileDeleteHarness extends BaseHarness implements RunHarness
{
    private static final Logger log = LogManager.getLogger(FileDeleteHarness.class.getName());
    private String fileName;
    private String folderName;

    private RunResponse response;

    boolean successful = true;
    boolean deleteOnlyContent = false;

    public void run(HarnessContext context)
    {
        log.info("Running File Delete Harness");

        if (null != fileName)
        {
            if (deleteOnlyContent)
            {
                successful = deleteFileContent(fileName);
            }
            else
            {
                successful = deleteFile(fileName);
            }
        }
        if (null != folderName && successful)
        {
            File deleteMe = new File(folderName);
            if (deleteOnlyContent)
            {
                successful = this.deleteFolderContents(deleteMe);
            }
            else
            {
                successful = this.deleteFolder(deleteMe);
            }
        }
    }

    public boolean deleteFileContent(String fName)
    {
        boolean result = true;
        try
        {
            RandomAccessFile badFile = new RandomAccessFile(fName, "rw");
            badFile.setLength(0);
            badFile.close();
        }
        catch (FileNotFoundException e)
        {
            this.getErrors()
                    .add(new HarnessError(this, "File Not Found Exception", "Couldn't find file to delete: " + fName));
            result = false;
        }
        catch (IOException e)
        {
            this.getErrors().add(new HarnessError(this, "IO Exception", "Couldn't delete content of file: " + fName));
            result = false;
        }
        return result;
    }

    public boolean deleteFile(String fName)
    {
        File tFile = new File(fName);
        return deleteFile(tFile);
    }

    public boolean deleteFile(File badFile)
    {
        return badFile.delete();
    }

    public boolean deleteFolderContents(File dir)
    {
        boolean result = false;
        if (dir.isDirectory())
        {
            String[] content = dir.list();
            for (int i = 0; i < content.length; i++)
            {
                File tempFile = new File(dir, content[i]);
                if (tempFile.isFile())
                {
                    deleteFile(tempFile);
                }
                if (tempFile.isDirectory())
                {
                    deleteFolder(tempFile);
                }
            }
        }
        else if (dir.isFile())
        {
            result = deleteFile(dir);
        }
        return result;
    }

    public boolean deleteFolder(File dir)
    {
        if (dir.isDirectory())
        {
            String[] content = dir.list();
            for (int i = 0; i < content.length; i++)
            {
                boolean success = deleteFolder(new File(dir, content[i]));
                if (!success)
                {
                    this.getErrors().add(new HarnessError(this, "Folder Deletion Exception",
                                                          "Couldn't delete content of file: " + content[i]));
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public RunResponse getResponse()
    {
        response = new RawResponse(Boolean.toString(successful));
        return response;
    }

    @HarnessConfiguration(name = "fileName")
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @HarnessConfiguration(name = "folderName")
    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    @HarnessConfiguration(name = "justDeleteContent")
    public void setDeleteOnlyContent(String deleteContent)
    {
        this.deleteOnlyContent = Boolean.parseBoolean(deleteContent);
    }
}