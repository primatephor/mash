package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author teastlack
 * @since Oct 29, 2009 10:48:27 AM
 *
 */
public class TestFTPWaitHarness extends TestCase
{
    private static final Logger log = Logger.getLogger(TestFTPWaitHarness.class.getName());

    private int pollCount = 0;
    private int count = 0;
    private FTPFile[] responseFiles;

    public void testSimple()
    {
        MyFTPWaitHarness wait = new MyFTPWaitHarness();
        //should response after 3 polls
        pollCount = 2;
        //return one file
        responseFiles = new FTPFile[1];
        responseFiles[0] = new FTPFile();
        responseFiles[0].setName("theFile");

        long start = new Date().getTime();
        wait.run(null, null);
        long end = new Date().getTime();
        long difference = end - start;
        //more than 10 seconds, 1st poll has no wait and increases count
        assertTrue("not enough time has passeed:"+difference, difference > 10000);
        //less than 15 seconds
        assertTrue("too much time has passeed:"+difference, difference < 15000);
    }

    public void testTimeout()
    {
        MyFTPWaitHarness wait = new MyFTPWaitHarness();
        wait.setPath("theFile");
        wait.setTimeoutMillis("30000");
        //should still poll 2 times
        wait.setPollMillis("20000");
        //should response after 3 polls
        pollCount = 2;
        //return one file
        responseFiles = new FTPFile[1];
        responseFiles[0] = new FTPFile();
        responseFiles[0].setName("theFile");

        long start = new Date().getTime();
        wait.run(null, null);
        long end = new Date().getTime();
        long difference = end - start;
        //more than 10 seconds, 1st poll has no wait and increases count
        assertTrue("not enough time has passeed:"+difference, difference > 29500);
        //less than 15 seconds
        assertTrue("too much time has passeed:"+difference, difference < 35000);
        assertEquals(1, wait.getErrors().size());
        assertEquals("Timed out before found theFile", wait.getErrors().get(0).getValue());
    }

    private class MyFTPWaitHarness extends FTPWaitHarness
    {
        protected ListHarness buildRunHarness()
        {
            return new MyFTPRunHarness();
        }
    }

    private class MyFTPRunHarness extends ListHarness
    {
        protected FTPClient buildClient()
        {
            return new MyClient();
        }
    }

    private class MyClient extends FTPClient
    {
        public FTPFile[] listFiles(String s) throws IOException
        {
            return listFiles();
        }

        public FTPFile[] listFiles() throws IOException
        {
            FTPFile[] results = null;
            log.debug("Count:"+count);
            if(count < pollCount)
            {
                count ++;
            }
            else
            {
                results = responseFiles;
            }
            return results;
        }

        public void connect(String url) throws IOException
        {
            //don't try to connect, we're overriding this
        }

        public int getReplyCode()
        {
            return 220;
        }

        public boolean login(String s, String s1) throws IOException
        {
            return true;
        }

        public boolean logout() throws IOException
        {
            return true;
        }

        public boolean isConnected()
        {
            return false;
        }

        public boolean setFileType(int i) throws IOException
        {
            return true;
        }
    }
}
