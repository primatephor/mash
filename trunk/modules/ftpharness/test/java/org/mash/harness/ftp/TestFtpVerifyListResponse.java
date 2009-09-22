package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.apache.commons.net.ftp.FTPFile;
import org.mash.config.Parameter;
import org.mash.harness.SetupHarness;
import org.mash.harness.StandardVerifyHarness;

import java.util.ArrayList;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 12:20:09 PM
 *
 */
public class TestFtpVerifyListResponse extends TestCase
{
    public void testPresent()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[1] = new FTPFile();
        files[1].setName("file2");
        ListRunResponse response = new ListRunResponse(files);

        StandardVerifyHarness verify = new StandardVerifyHarness();
        Parameter param = new Parameter("file1", "file1");
        verify.getParameters().add(param);
        param = new Parameter("file3", "file3");
        verify.getParameters().add(param);
        verify.verify(new BogusFTPRun(response), new ArrayList<SetupHarness>());
        assertEquals("Expected 'file3' does not equal 'null'", verify.getErrors().get(0).getValue());
    }

    public void testPresentList()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[1] = new FTPFile();
        files[1].setName("file2");
        ListRunResponse response = new ListRunResponse(files);

        FtpVerifyListResponse verify = new FtpVerifyListResponse();
        verify.setFileName("file3");
        verify.verify(new BogusFTPRun(response), new ArrayList<SetupHarness>());
        assertEquals("File 'file3' Not Found", verify.getErrors().get(0).getValue());

        verify = new FtpVerifyListResponse();
        verify.setFileName("file1");
        verify.verify(new BogusFTPRun(response), new ArrayList<SetupHarness>());
        assertEquals(0, verify.getErrors().size());
    }

    public void testFileSize()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[0].setSize(1000);

        files[1] = new FTPFile();
        files[1].setName("file2");
        files[1].setSize(2000);
        ListRunResponse response = new ListRunResponse(files);

        FtpVerifyListResponse verify = new FtpVerifyListResponse();
        verify.setFileName("file1");
        verify.setFileSize("200");
        verify.verify(new BogusFTPRun(response), new ArrayList<SetupHarness>());
        assertEquals("File size is '1000' but expected '200'", verify.getErrors().get(0).getValue());

        verify = new FtpVerifyListResponse();
        verify.setFileName("file2");
        verify.setFileSize("2000");
        verify.verify(new BogusFTPRun(response), new ArrayList<SetupHarness>());
        assertEquals(0, verify.getErrors().size());
    }

}
