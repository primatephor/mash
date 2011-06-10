package org.mash.harness.http;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.TestCase;
import org.mash.config.Configuration;
import org.mash.config.Parameter;
import org.mash.config.Run;
import org.mash.config.Script;
import org.mash.config.ScriptDefinition;
import org.mash.config.Verify;
import org.mash.junit.StandardTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class TestHttpHarness extends TestCase
{
    public void testGet() throws Exception
    {
        HttpClient client = new HttpClient(new StandardRequestFactory(), "get");

        //http://www.google.com/search
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", "System Test");
        params.put("ie", "utf-8");
        params.put("oe", "utf-8");
        params.put("aq", "t");
        params.put("rls", "org.mozilla:en-US:official");
        params.put("client", "firefox-a");

        client.submit("http://www.google.com/search", params);
        HtmlPage response = (HtmlPage) client.getWebResponse();
        assertEquals("System Test - Google Search", response.getTitleText());
    }

    public void testRunAndVerify() throws Throwable
    {
        ScriptDefinition definition = new Script();

        //call first page
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(new Configuration("url", "http://www.google.com/search"));
        configs.add(new Configuration("type", "GET"));
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("q", "System Test"));
        params.add(new Parameter("ie", "utf-8"));
        params.add(new Parameter("oe", "utf-8"));
        params.add(new Parameter("aq", "t"));
        params.add(new Parameter("rls", "org.mozilla:en-US:official"));
        params.add(new Parameter("client", "firefox-a"));
        Run runHarness = new Run();
        runHarness.getParameter().addAll(params);
        runHarness.getConfiguration().addAll(configs);
        runHarness.setName("search1");
        runHarness.setType("org.mash.harness.http.HttpRunHarness");
        definition.getHarnesses().add(runHarness);

        //verify page
        configs = new ArrayList<Configuration>();
        configs.add(new Configuration("title", "System Test - Google Search"));
        configs.add(new Configuration("status", "200"));
        configs.add(new Configuration("contains", "/search?q=System+Test"));
        configs.add(new Configuration("contains", "Searches related to <em>System Test</em>"));
        params = new ArrayList<Parameter>();
        //<input type=hidden name=client value="firefox-a">
        params.add(new Parameter("client", "firefox-a"));
        Verify verifyHarness = new Verify();
        verifyHarness.getParameter().addAll(params);
        verifyHarness.getConfiguration().addAll(configs);
        verifyHarness.setName("the First Verify");
        verifyHarness.setType("org.mash.harness.http.HttpVerifyHarness");
        definition.getHarnesses().add(verifyHarness);

        //run2
        //http://www.google.com/search?q=System+Test&hl=en&client=firefox-a&rls=com.ubuntu:en-US:unofficial&hs=ln0&start=10&sa=N
        configs = new ArrayList<Configuration>();
        configs.add(new Configuration("url", "http://www.google.com/search"));
        configs.add(new Configuration("type", "GET"));
        params = new ArrayList<Parameter>();
        params.add(new Parameter("q", "System Test"));
        params.add(new Parameter("ie", "utf-8"));
        params.add(new Parameter("oe", "utf-8"));
        params.add(new Parameter("aq", "t"));
        params.add(new Parameter("rls", "org.mozilla:en-US:official"));
        params.add(new Parameter("client", "firefox-a"));
        params.add(new Parameter("sa", "N"));
        params.add(new Parameter("start", "10"));
        runHarness = new Run();
        runHarness.getParameter().addAll(params);
        runHarness.getConfiguration().addAll(configs);
        runHarness.setName("search2");
        runHarness.setType("org.mash.harness.http.HttpRunHarness");
        definition.getHarnesses().add(runHarness);

        //verify page
        configs = new ArrayList<Configuration>();
        configs.add(new Configuration("title", "System Test - Google Search"));
        configs.add(new Configuration("status", "200"));
        //verify second page
        configs.add(new Configuration("contains", "/search?q=System+Test"));
        configs.add(new Configuration("contains", "start=100"));
        configs.add(new Configuration("contains", "Searches related to <em>System Test</em>"));
        params = new ArrayList<Parameter>();
        //<input type=hidden name=client value="firefox-a">
        params.add(new Parameter("client", "firefox-a"));
        verifyHarness = new Verify();
        verifyHarness.getParameter().addAll(params);
        verifyHarness.getConfiguration().addAll(configs);
        verifyHarness.setName("the Second Verify");
        verifyHarness.setType("org.mash.harness.http.HttpVerifyHarness");
        definition.getHarnesses().add(verifyHarness);

        StandardTestCase standardTestCase = new StandardTestCase(definition);
        standardTestCase.runBare();
    }

        public void testXPath() throws Throwable
    {
        ScriptDefinition definition = new Script();

        //call first page
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(new Configuration("url", "http://www.google.com/search"));
        configs.add(new Configuration("type", "GET"));
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("q", "System Test"));
        params.add(new Parameter("ie", "utf-8"));
        params.add(new Parameter("oe", "utf-8"));
        params.add(new Parameter("aq", "t"));
        params.add(new Parameter("rls", "org.mozilla:en-US:official"));
        params.add(new Parameter("client", "firefox-a"));
        Run runHarness = new Run();
        runHarness.getParameter().addAll(params);
        runHarness.getConfiguration().addAll(configs);
        runHarness.setName("search1");
        runHarness.setType("org.mash.harness.http.HttpRunHarness");
        definition.getHarnesses().add(runHarness);

        //verify page
        configs = new ArrayList<Configuration>();
        configs.add(new Configuration("title", "System Test - Google Search"));
        configs.add(new Configuration("status", "200"));
        configs.add(new Configuration("contains", "/search?q=System+Test"));
        configs.add(new Configuration("contains", "start=10"));
        configs.add(new Configuration("contains", "Searches related to <em>System Test</em>"));
        params = new ArrayList<Parameter>();
        //<input type=hidden name=client value="firefox-a">
        params.add(new Parameter("//title[1]", "System Test - Google Search"));
        Verify verifyHarness = new Verify();
        verifyHarness.getParameter().addAll(params);
        verifyHarness.getConfiguration().addAll(configs);
        verifyHarness.setName("the First Verify");
        verifyHarness.setType("org.mash.harness.http.HttpVerifyHarness");
        definition.getHarnesses().add(verifyHarness);

        StandardTestCase standardTestCase = new StandardTestCase(definition);
        standardTestCase.runBare();
    }

    public void testRunAndError() throws Exception
    {
        ScriptDefinition definition = new Script();

        //call first page
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(new Configuration("url", "/search"));
        configs.add(new Configuration("type", "GET"));
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter("q", "System Test"));
        params.add(new Parameter("ie", "utf-8"));
        params.add(new Parameter("oe", "utf-8"));
        params.add(new Parameter("aq", "t"));
        params.add(new Parameter("rls", "org.mozilla:en-US:official"));
        params.add(new Parameter("client", "firefox-a"));
        Run runHarness = new Run();
        runHarness.getParameter().addAll(params);
        runHarness.getConfiguration().addAll(configs);
        runHarness.setName("search1");
        runHarness.setType("org.mash.harness.http.HttpRunHarness");
        definition.getHarnesses().add(runHarness);

        StandardTestCase standardTestCase = new StandardTestCase(definition);

        boolean expectedError = false;
        try
        {
            standardTestCase.runBare();
        }
        catch (Throwable throwable)
        {
            assertEquals("Errors found during verification\n" +
                         "Harness:search1, Error:Unexpected error sending to /search, Description:java.lang.IllegalArgumentException:URI is not absolute", throwable.getMessage().trim());
            expectedError = true;
        }
        assertTrue("No expected error found!", expectedError);
    }

}
