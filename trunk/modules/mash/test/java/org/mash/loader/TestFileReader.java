package org.mash.loader;

import junit.framework.TestCase;

/**
 * User: teastlack Date: Jul 1, 2009 Time: 11:32:31 AM
 */
public class TestFileReader extends TestCase
{
    public void testBase() throws FileReaderException
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents("org/mash/junit/suite/simple.txt");
        assertEquals("This is a simple file", contents.trim());
    }

    public void testXML() throws FileReaderException
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents("org/mash/junit/suite/suite.xml");
        assertEquals("<ns1:Suite name=\"The Suite\" xmlns:ns1=\"http://code.google.com/p/mash/schema/V1\">\n" +
                     "    <Script file=\"dir1/TestA.xml\"/>\n" +
                     "    <Script dir=\"dir2\"/>\n" +
                     "    <Parallel>\n" +
                     "        <Script file=\"dir3/TestB.xml\"/>\n" +
                     "        <Script file=\"dir4/TestC.xml\"/>\n" +
                     "    </Parallel>\n" +
                     "    <Script name=\"The Test\">\n" +
                     "        <Tag>login</Tag>\n" +
                     "        <Tag>website</Tag>\n" +
                     "        <Tag>myapp</Tag>\n" +
                     "        <Setup type=\"org.mash.harness.db.DBSetupHarness\">\n" +
                     "            <Configuration name=\"clean\">\n" +
                     "                <Value>true</Value>\n" +
                     "            </Configuration>\n" +
                     "            <Parameter name=\"loadfile\" file=\"db_load.xml\">\n" +
                     "                <Replace search=\"$variable$\">\n" +
                     "                    <Value>sometext</Value>\n" +
                     "                </Replace>\n" +
                     "            </Parameter>\n" +
                     "        </Setup>\n" +
                     "        <Run type=\"org.mash.harness.http.HttpRunHarness\">\n" +
                     "            <Configuration name=\"url\" property=\"my.url\"/>\n" +
                     "            <Configuration name=\"type\">\n" +
                     "                <Value>POST</Value>\n" +
                     "            </Configuration>\n" +
                     "            <Parameter name=\"username\">\n" +
                     "                <Value>testuser</Value>\n" +
                     "            </Parameter>\n" +
                     "            <Parameter name=\"password\">\n" +
                     "                <Value>pass</Value>\n" +
                     "            </Parameter>\n" +
                     "        </Run>\n" +
                     "        <Verify type=\"org.mash.harness.http.HttpVerifyHarness\">\n" +
                     "            <Configuration name=\"status\">\n" +
                     "                <Value>200</Value>\n" +
                     "            </Configuration>\n" +
                     "            <Configuration name=\"title\">\n" +
                     "                <Value>My Page Title</Value>\n" +
                     "            </Configuration>\n" +
                     "            <Configuration name=\"contains\">\n" +
                     "                <Value>Enter your order number</Value>\n" +
                     "            </Configuration>\n" +
                     "            <Parameter name=\"do_search\">\n" +
                     "                <Value>1</Value>\n" +
                     "            </Parameter>\n" +
                     "        </Verify>\n" +
                     "    </Script>\n" +
                     "</ns1:Suite>", contents.trim());
    }
}
