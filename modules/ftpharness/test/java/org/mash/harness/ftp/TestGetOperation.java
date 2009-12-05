package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.mash.file.TextFileReader;
import org.mash.harness.RunResponse;

import java.io.File;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 4:34:19 PM
 *
 */
public class TestGetOperation extends TestCase
{
    public void testGetFile() throws Exception
    {
        BogusFTPFileClient client = new BogusFTPFileClient();
        client.setPath("org/mash/harness/ftp/operations_data/somedata.txt");
        GetHarness operation = new GetHarness();
        RunResponse response = operation.retrieve(client, "unnecessary");
        assertEquals("Here is some data to pretend to ftp", response.getString());
    }

    public void testGetFileRef() throws Exception
    {
        BogusFTPFileClient client = new BogusFTPFileClient();
        client.setPath("org/mash/harness/ftp/operations_data/somedata.txt");
        GetHarness operation = new GetHarness();
        operation.setOutput_file("/tmp/output_somedata.txt");
        RunResponse response = operation.retrieve(client, "unnecessary");
        assertEquals("/tmp/output_somedata.txt", response.getString());
        assertEquals("/tmp/output_somedata.txt", response.getValue("absolutePath"));
        assertEquals("35", response.getValue("length"));
        String contents = new TextFileReader().getContents("/tmp/output_somedata.txt");
        assertEquals("Here is some data to pretend to ftp", contents.trim());

        //cleanup
        File file = new File("/tmp/output_somedata.txt");
        file.delete();
    }
}
