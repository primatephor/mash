package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.mash.harness.RunResponse;

/**
 *
 * @author
 * @since Sep 30, 2009 8:05:20 PM
 *
 */
public class TestRawOperation extends TestCase
{
    public void testCommand() throws Exception
    {
        String[] ls = new String[2];
        ls[0] = "something.txt";
        ls[1] = "anotherthing.txt";
        BogusRawFTPClient rawFTPClient = new BogusRawFTPClient(ls);
        FTPRunHarness rawOperation = new FTPRunHarness();
        rawOperation.setFtpParams("cd to/this/dir");
        RunResponse response = rawOperation.runOperation(rawFTPClient);
        assertEquals("cd", rawFTPClient.getCommand());
        assertEquals("to/this/dir", rawFTPClient.getArgs());
        assertEquals(2, response.getValues().size());
        assertEquals("something.txt", response.getValue("0"));
    }
}
