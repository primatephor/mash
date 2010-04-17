package org.mash.harness;

import junit.framework.TestCase;
import org.mash.config.Parameter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author teastlack
 * @since Sep 22, 2009 4:15:36 PM
 *
 */
public class TestOGNLResponse extends TestCase
{
    public void testValueByName()
    {
        Parameter param = new Parameter("myparam", "somevalue");
        OGNLResponse response = new OGNLResponse(param);
        assertEquals("myparam", response.getValue("name"));
        assertEquals("somevalue", response.getValue("value"));
    }

    public void testToString()
    {
        Parameter param = new Parameter("myparam", "somevalue");
        OGNLResponse response = new OGNLResponse(param);
        assertEquals("myparam:somevalue", response.getString());
    }

    public void testValues()
    {
        Map<String, String> stuff = new HashMap<String, String>();
        stuff.put("one", "value1");
        stuff.put("two", "value2");
        OGNLResponse response = new OGNLResponse(stuff);
        Collection<String> values = response.getValues("values");
        assertTrue("value1 not in list", values.contains("value1"));
        assertTrue("value2 not in list", values.contains("value2"));
    }
}
