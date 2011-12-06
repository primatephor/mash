package org.mash;

import junit.framework.TestCase;
import org.mash.config.Suite;
import org.mash.file.FileReaderException;
import org.mash.file.TextFileReader;
import org.mash.loader.JAXBSuiteMarshaller;
import org.mash.loader.JSONSuiteMarshaller;
import org.mash.loader.SuiteMarshallerException;

/**
 * @author teastlack
 * @since Dec 5, 2011 2:44:56 PM
 */
public class TestJSONMarshaller extends TestCase
{
    public void testBasicFunction() throws SuiteMarshallerException, FileReaderException
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents("org/mash/junit/suite/suite.xml");
        JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
        Suite suite = (Suite) marshaller.unmarshal(contents);
        assertEquals("The Suite", suite.getName());

        JSONSuiteMarshaller jsonMarshaller = new JSONSuiteMarshaller();
        assertEquals("{\"Suite\":{\"@name\":\"The Suite\"," +
                "\"Script\":[" +
                "{\"@file\":\"dir1\\/TestA.xml\"}," +
                "{\"@dir\":\"dir2\"}," +
                "{\"@name\":\"The Test\",\"Tag\":[\"login\",\"website\",\"myapp\"]," +
                    "\"Setup\":{\"@type\":\"org.mash.harness.db.DBSetupHarness\"," +
                        "\"Configuration\":{\"@name\":\"clean\",\"Value\":true}," +
                        "\"Parameter\":{\"@name\":\"loadfile\",\"@file\":\"db_load.xml\"," +
                            "\"Replace\":{\"@search\":\"$variable$\",\"Value\":\"sometext\"}}}," +
                    "\"Run\":{\"@type\":\"org.mash.harness.http.HttpRunHarness\"," +
                        "\"Configuration\":[" +
                            "{\"@property\":\"my.url\",\"@name\":\"url\"}," +
                            "{\"@name\":\"type\",\"Value\":\"POST\"}]," +
                        "\"Parameter\":[" +
                            "{\"@name\":\"username\",\"Value\":\"testuser\"}," +
                            "{\"@name\":\"password\",\"Value\":\"pass\"}]}," +
                    "\"Verify\":{\"@type\":\"org.mash.harness.http.HttpVerifyHarness\"," +
                        "\"Configuration\":[" +
                            "{\"@name\":\"status\",\"Value\":200}," +
                            "{\"@name\":\"title\",\"Value\":\"My Page Title\"}," +
                            "{\"@name\":\"contains\",\"Value\":\"Enter your order number\"}]," +
                        "\"Parameter\":{\"@name\":\"do_search\",\"Value\":1}}}]," +
                "\"Parallel\":{" +
                    "\"Script\":[{\"@file\":\"dir3\\/TestB.xml\"}," +
                    "{\"@file\":\"dir4\\/TestC.xml\"}]}}}", jsonMarshaller.marshal(suite));

        reader = new TextFileReader();
        contents = reader.getContents("org/mash/junit/suite/suite.json");
        Suite jsonSuite = (Suite) jsonMarshaller.unmarshal(contents);
        assertEquals("The Suite", jsonSuite.getName());
        //todo: json unmarshalling not fully working
        //assertEquals(4, jsonSuite.getScriptOrParallel().size());
    }
}
