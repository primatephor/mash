package org.mash.loader;

import org.mash.config.Suite;
import org.mash.file.TextFileReader;
import org.mash.harness.PropertyObjectFactory;

import java.io.File;
import java.net.URL;

/**
 * @author teastlack
 * @since Jul 3, 2009
 */
public class SuiteLoader
{

    private static SuiteMarshaller marshaller;

    private static SuiteMarshaller getMarshaller(String fileName) throws Exception
    {
        if (marshaller == null)
        {
            marshaller = PropertyObjectFactory.getInstance().buildMarshaller(fileName);
        }
        return marshaller;
    }

    public Suite loadSuite(String fileName) throws Exception
    {
        TextFileReader reader = new TextFileReader();
        String contents = reader.getContents(fileName);
        SuiteMarshaller marshaller = getMarshaller(fileName);
        Suite suite = (Suite) marshaller.unmarshal(contents);
        if (suite == null)
        {
            throw new NoClassDefFoundError("Unable to find system test " + fileName);
        }

        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource != null)
        {
            File suiteFile = new File(resource.getFile());
            if (suiteFile.exists())
            {
                suite.setPath(suiteFile);
            }
            else
            {
                suite.setPath(new File(fileName));
            }
        }
        else
        {
            suite.setPath(new File(fileName));
        }
        return suite;
    }
}
