package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.mash.harness.BaseHarness;
import org.mash.harness.HarnessError;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;

import java.util.List;

/**
 * Verify a small set of data against a file on the server
 *
 * Configurations:
 * <ul>
 * </ul>
 *
 * Parameters:
 * <ul>
 * <li> 'file_name' to look for </li>
 * <li> 'file_size' of file to check (must be used with file_name) </li>
 * <li> 'list_size' # of files expected </li>
 * </ul>
 *
 * @author
 * @since Sep 22, 2009 12:02:24 PM
 *
 */
@HarnessName(name = "list_ftp")
public class FTPVerifyListHarness extends BaseHarness implements VerifyHarness
{
    private static final Logger log = Logger.getLogger(FTPVerifyListHarness.class.getName());
    private String fileName;
    private String fileSize;
    private Integer listSize;

    public void verify(RunHarness run, List<SetupHarness> setup)
    {
        RunResponse response = run.getResponse();
        if (fileName != null)
        {
            if (fileSize == null)
            {
                getErrors().add(new HarnessError(this, "Size", "No file size specified while checking size"));
            }
            else
            {
                if (response instanceof ListRunResponse)
                {
                    ListRunResponse listResponse = (ListRunResponse) response;
                    FTPFile file = listResponse.getFiles().get(fileName);
                    if (file != null)
                    {
                        log.info("Found " + file.getName());
                        Long expected = new Long(fileSize);
                        if (expected != file.getSize())
                        {
                            getErrors().add(new HarnessError(this, "Size", "File size is '" +
                                                                           file.getSize() +
                                                                           "' but expected '" +
                                                                           expected + "'"));
                        }
                    }
                    else
                    {
                        getErrors().add(new HarnessError(this, "List", "File '" + fileName + "' Not Found"));
                    }
                }
            }
        }

        if (listSize != null)
        {
            if (response instanceof ListRunResponse)
            {
                ListRunResponse listResponse = (ListRunResponse) response;
                int actualSize = listResponse.getFiles().size();
                if (actualSize != listSize)
                {
                    getErrors().add(new HarnessError(this, "List", "Expected number of files:" + listSize +
                                                                   " doesn't equal actual number:" + actualSize));
                }
            }
            else
            {
                getErrors().add(new HarnessError(this, "Response", "The response is not a list operation response"));
            }
        }
    }

    @HarnessParameter(name = "file_name")
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @HarnessParameter(name = "file_size")
    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }

    @HarnessParameter(name = "list_size")
    public void setListSize(String listSize)
    {
        this.listSize = Integer.valueOf(listSize);
    }
}
