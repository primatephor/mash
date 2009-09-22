package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.apache.commons.net.ftp.FTPFile;

import java.util.Collection;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 12:25:52 PM
 *
 */
public class TestListRunResponse extends TestCase
{
    public void testSingleFilename()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[1] = new FTPFile();
        files[1].setName("file2");
        ListRunResponse response = new ListRunResponse(files);
        assertEquals("file1", response.getValue("file1"));
    }

    public void testFilenameList()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[1] = new FTPFile();
        files[1].setName("file2");
        ListRunResponse response = new ListRunResponse(files);
        Collection<String> names = response.getValues();
        assertTrue("file1 not present", names.contains("file1"));
        assertTrue("file2 not present", names.contains("file2"));
    }

    public void testFilenameString()
    {
        FTPFile[] files = new FTPFile[2];
        files[0] = new FTPFile();
        files[0].setName("file1");
        files[1] = new FTPFile();
        files[1].setName("file2");
        ListRunResponse response = new ListRunResponse(files);
        String list = response.getString();
        assertEquals("file1\nfile2\n", list);
    }
}
