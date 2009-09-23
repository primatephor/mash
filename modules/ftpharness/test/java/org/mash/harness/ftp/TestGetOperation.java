package org.mash.harness.ftp;

import junit.framework.TestCase;
import org.mash.file.TextFileReader;
import org.mash.harness.RunResponse;
import org.mash.harness.ftp.operations.GetOperation;

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
        BogusFTPClient client = new BogusFTPClient();
        client.setPath("org/mash/harness/ftp/operations_data/somedata.txt");
        GetOperation operation = new GetOperation();
        RunResponse response = operation.operate(client, "unnecessary");
        assertEquals("Here is some data to pretend to ftp", response.getString());
    }

    public void testGetFileRef() throws Exception
    {
        BogusFTPClient client = new BogusFTPClient();
        client.setPath("org/mash/harness/ftp/operations_data/somedata.txt");
        GetOperation operation = new GetOperation("/tmp/output_somedata.txt");
        RunResponse response = operation.operate(client, "unnecessary");
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
