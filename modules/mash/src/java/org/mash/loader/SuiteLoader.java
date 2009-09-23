package org.mash.loader;

import org.mash.config.Suite;
import org.mash.file.TextFileReader;

import java.io.File;

/**
 * @author: teastlack
 * @since: Jul 3, 2009
 */
public class SuiteLoader
{
    private static String SUITE_MARSHALLER = System.getProperty("suite.marshaller", "org.mash.loader.JAXBSuiteMarshaller");
    private static SuiteMarshaller marshaller;

    private static SuiteMarshaller getMarshaller() throws Exception
    {
        if (marshaller == null)
        {
            marshaller = (SuiteMarshaller) Class.forName(SUITE_MARSHALLER).newInstance();
        }
        return marshaller;
    }

    public Suite loadSuite(String fileName) throws Exception
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents(fileName);
        SuiteMarshaller marshaller = getMarshaller();
        Suite suite = (Suite) marshaller.unmarshal(contents);
        if (suite == null)
        {
            throw new NoClassDefFoundError("Unable to find system test " + fileName);
        }

        File suiteFile = new File(getClass().getClassLoader().getResource(fileName).getFile());
        if (suiteFile.exists())
        {
            suite.setPath(suiteFile);
        }
        else
        {
            suite.setPath(new File(fileName));
        }
        return suite;
    }
}
