package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.operations.RawOperation;

/**
 *
 * @author teastlack
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
        RawOperation rawOperation = new RawOperation();
        RunResponse response = rawOperation.operate(rawFTPClient, "cd to/this/dir");
        assertEquals("cd", rawFTPClient.getCommand());
        assertEquals("to/this/dir", rawFTPClient.getArgs());
        assertEquals(2, response.getValues().size());
        assertEquals("something.txt", response.getValue("0"));
    }
}
