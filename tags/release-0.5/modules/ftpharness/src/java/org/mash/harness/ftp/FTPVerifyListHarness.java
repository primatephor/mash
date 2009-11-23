package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;

import java.util.List;

/**
 * Verify a small set of data against a file on the server
 *
 * Configurations:
 * <ul>
 * <li> 'file_name' to look for </li>
 * </ul>
 *
 * Parameters:
 * <ul>
 * <li> 'size' of file to check </li>
 * </ul>
 *
 * @author teastlack
 * @since Sep 22, 2009 12:02:24 PM
 *
 */
public class FTPVerifyListHarness extends BaseHarness implements VerifyHarness
{
    private static final Logger log = Logger.getLogger(FTPVerifyListHarness.class.getName());
    private String fileName;
    private String fileSize;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        if (fileName == null || fileName.length() == 0)
        {
            getErrors().add(new HarnessError(this.getClass().getName(), "No filename specified"));
        }
        else
        {
            RunResponse response = run.getResponse();
            if (response instanceof ListRunResponse)
            {
                ListRunResponse listResponse = (ListRunResponse) response;
                FTPFile file = listResponse.getFiles().get(fileName);
                if (file != null)
                {
                    log.info("Found " + file.getName());
                    if (fileSize != null && fileSize.length() > 0)
                    {
                        Long expected = new Long(fileSize);
                        if (expected != file.getSize())
                        {
                            getErrors().add(new HarnessError(this.getClass().getName(), "File size is '" +
                                                                                        file.getSize() +
                                                                                        "' but expected '" +
                                                                                        expected + "'"));
                        }
                    }
                }
                else
                {
                    getErrors().add(new HarnessError(this.getClass().getName(), "File '" + fileName + "' Not Found"));
                }
            }
        }
    }

    @HarnessConfiguration(name = "file_name")
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @HarnessParameter(name = "size")
    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }
}