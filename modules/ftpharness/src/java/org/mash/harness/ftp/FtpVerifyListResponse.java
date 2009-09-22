package org.mash.harness.ftp;

import org.apache.commons.net.ftp.FTPFile;
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
 *
 * @author teastlack
 * @since Sep 22, 2009 12:02:24 PM
 *
 */
public class FtpVerifyListResponse extends BaseHarness implements VerifyHarness
{
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

    @HarnessConfiguration(name = "fileName")
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
