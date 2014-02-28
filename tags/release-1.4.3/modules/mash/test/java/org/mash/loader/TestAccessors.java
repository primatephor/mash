package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.BaseParameter;
import org.mash.config.Date;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.Replace;
import org.mash.config.Response;
import org.mash.harness.HttpRunHarness;
import org.mash.harness.RunHarness;
import org.mash.loader.accessor.DateAccessor;
import org.mash.loader.accessor.FileAccessor;
import org.mash.loader.accessor.PropertyAccessor;
import org.mash.loader.accessor.ReplaceAccessor;
import org.mash.loader.accessor.ResponseAccessor;
import org.mash.loader.accessor.ValueAccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author teastlack
 * @since Jul 10, 2009 11:10:21 AM
 *
 */
public class TestAccessors extends TestCase
{
    public void testValue()
    {
        BaseParameter param = new BaseParameter("name", "value");
        assertEquals("value", new ValueAccessor().accessContent(param, null));
        assertEquals("currentvalue", new ValueAccessor().accessContent(param, "current"));
        assertEquals("", new ValueAccessor().accessContent(new BaseParameter(), null));
    }

    public void testProperty()
    {
        System.setProperty("my.property", "TheProperty");
        BaseParameter param = new BaseParameter("name", "value");
        param.setProperty("my.property");
        assertEquals("TheProperty", new PropertyAccessor().accessContent(param, null));
        assertEquals("ThePropertyvalue", new PropertyAccessor().accessContent(param, param.getValue()));
        assertEquals("", new PropertyAccessor().accessContent(new BaseParameter(), null));
    }

    public void testDate() throws Exception
    {
        BaseParameter param = new BaseParameter("name", "value");
        param.setDate(new MyDate(new java.util.Date(1247170000000l)));
        assertEquals("7/9/09 1:06 PM", new DateAccessor().accessContent(param, null));
        assertEquals("7/9/09 1:06 PM", new DateAccessor().accessContent(param, param.getValue()));
        assertEquals("", new DateAccessor().accessContent(new BaseParameter(), null));
    }

    public void testFile() throws Exception
    {
        BaseParameter param = new BaseParameter("name", "value", "org/mash/loader/sample_load.txt");
        assertEquals("A simple file for testing {REPLACE}ing stuff inside of it.",
                     new FileAccessor(new File(".")).accessContent(param, null).trim());
        assertEquals("A simple file for testing {REPLACE}ing stuff inside of it.",
                     new FileAccessor(new File(".")).accessContent(param, param.getValue()).trim());
        assertEquals("", new FileAccessor(new File(".")).accessContent(new BaseParameter(), null));
        assertEquals("bogus", new FileAccessor(new File(".")).accessContent(new BaseParameter(), "bogus"));
    }

    public void testResponse() throws Exception
    {
        BaseParameter param = new BaseParameter("name", "value");
        Response response = new Response("httprun", "someparam");
        param.setResponse(response);
        HttpRunHarness run = new HttpRunHarness();
        HarnessDefinition definition = new HarnessDefinition();
        definition.setName("httprun");
        run.setDefinition(definition);
        List<RunHarness> runs = new ArrayList<RunHarness>();
        runs.add(run);
        run.run(null);
        ResponseAccessor accessor = new ResponseAccessor(runs);
        assertEquals("somevalue", accessor.accessContent(param, null));
        assertEquals("somevalue", accessor.accessContent(param, param.getValue()));
        assertEquals("", accessor.accessContent(new BaseParameter(), null));
        assertEquals("bogus", accessor.accessContent(new BaseParameter(), "bogus"));
    }

    public void testReplace() throws Exception
    {
        Parameter param = new Parameter("name", "value");
        Replace replace = new Replace("{REPLACE}", "see");
        param.getReplace().add(replace);

        AccessorChain chain = new AccessorChain();
        chain.add(new ValueAccessor());
        chain.add(new PropertyAccessor());
        ReplaceAccessor accessor = new ReplaceAccessor(chain);
        assertEquals("", accessor.accessContent(param, null));
        assertEquals("value", accessor.accessContent(param, param.getValue()));
        assertEquals("A simple file for testing seeing stuff inside of it.",
                     accessor.accessContent(param, "A simple file for testing {REPLACE}ing stuff inside of it."));
    }

    public void testAccessorChain() throws Exception
    {
        System.setProperty("my.property", "/com/some/prop/");

        Parameter param = new Parameter("name", "value");
        param.setProperty("my.property");
        param.setDate(new MyDate(new java.util.Date(1247170000000l)));
        param.setFile("org/mash/loader/sample_load.txt");
        Response response = new Response("httprun", "someparam");
        param.setResponse(response);
        Replace replace = new Replace("{REPLACE}", "see");
        param.getReplace().add(replace);

        AccessorChain chain = new AccessorChain();
        //value
        chain.add(new ValueAccessor());
        assertEquals("value", chain.access(param));
        //property
        chain.add(new PropertyAccessor());
        assertEquals("/com/some/prop/value", chain.access(param));
        //date
        chain.add(new DateAccessor());
        assertEquals("7/9/09 1:06 PM", chain.access(param));
        //run response
        HttpRunHarness run = new HttpRunHarness();
        HarnessDefinition definition = new HarnessDefinition();
        definition.setName("httprun");
        run.setDefinition(definition);
        List<RunHarness> runs = new ArrayList<RunHarness>();
        runs.add(run);
        run.run(null);
        ResponseAccessor accessor = new ResponseAccessor(runs);
        chain.add(accessor);
        assertEquals("somevalue", chain.access(param));
        //file
        chain.add(new FileAccessor(new File(".")));
        assertEquals("A simple file for testing {REPLACE}ing stuff inside of it.", chain.access(param).trim());

        AccessorChain replaceChain = new AccessorChain();
        replaceChain.add(new ValueAccessor());
        replaceChain.add(new PropertyAccessor());
        //same path as the file accessor above
        replaceChain.add(new FileAccessor(new File(".")));
        chain.add(new ReplaceAccessor(replaceChain));

        assertEquals("A simple file for testing seeing stuff inside of it.", chain.access(param).trim());
    }

    private class MyDate extends Date
    {
        private MyDate(java.util.Date toSet)
        {
            super.theDate = Calendar.getInstance();
            super.theDate.setTime(toSet);
        }
    }
}
