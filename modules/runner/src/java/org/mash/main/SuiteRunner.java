package org.mash.main;

import org.apache.log4j.Logger;
import org.mash.config.ScriptDefinition;
import org.mash.config.Suite;
import org.mash.harness.HarnessContext;
import org.mash.harness.PropertyObjectFactory;
import org.mash.harness.ScriptRunner;
import org.mash.loader.ScriptDefinitionLoader;
import org.mash.loader.SuiteLoader;
import org.mash.tool.ErrorFormatter;
import org.mash.tool.ErrorHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * To specify the test suite file, set the system property "suite.file".
 * To specify the list of tags, supply the system proptery "suite.tags" as a comma separated list of tags.
 * <p/>
 * To change the marshaller (you can build your own, no need for xml) set the system property "suite.marshaller"
 *
 * @author teastlack
 * @since Sep 7, 2010 6:36:32 PM
 */
public class SuiteRunner
{
    private static final Logger log = Logger.getLogger(SuiteRunner.class.getName());

    protected static String TAGS = System.getProperty("suite.tags", "");

    private ErrorHandler handler;

    public SuiteRunner() throws Exception
    {
        handler = new ErrorHandler((ErrorFormatter) PropertyObjectFactory.getInstance().buildFormatter());
    }

    public static void main(String[] args) throws Exception
    {
        if(args.length < 1)
        {
            System.out.println("Args: <suite file name>");
            System.out.println("Please insert a suite filename");
            System.exit(0);
        }

        String fileName = args[0];
        log.info("Running scripts from context " + fileName);
        SuiteRunner runner = new SuiteRunner();
        runner.run(fileName);
    }

    public void run(String suiteFileName) throws Exception
    {
        Suite suite = new SuiteLoader().loadSuite(suiteFileName);
        List<ScriptDefinition> scripts = buildScripts(suite, getTags());

        for (ScriptDefinition script : scripts)
        {
            ScriptRunner scriptRunner = PropertyObjectFactory.getInstance().buildRunner();
            handler.handleErrors(scriptRunner.run(script, new HarnessContext()), script);
        }
        
        if(handler.isError())
        {
            for (String error : handler.getFormattedErrors())
            {
                log.error(error);
            }
        }
        else
        {
            log.info("No errors");
        }
    }

    public List<ScriptDefinition> buildScripts(Suite suite, List<String> tags)
    {
        List<ScriptDefinition> results = new ArrayList<ScriptDefinition>();
        for (Object definition : suite.getScriptOrParallel())
        {
            if (definition instanceof ScriptDefinition)
            {
                results.addAll(buildScripts(suite, (ScriptDefinition) definition, tags));
            }
        }
        return results;
    }

    private Collection<? extends ScriptDefinition> buildScripts(Suite suite,
                                                                ScriptDefinition scriptDefinition,
                                                                List<String> tags)
    {
        List<ScriptDefinition> result = new ArrayList<ScriptDefinition>();
        try
        {
            //load the definition outright, as it's a fully defined test
            if (scriptDefinition.getFile() == null &&
                scriptDefinition.getDir() == null)
            {
                result.addAll(new ScriptDefinitionLoader(tags).pullSubDefinitions(scriptDefinition, suite.getPath()));
            }
            else
            {
                //load the directory of tests
                List<ScriptDefinition> scripts = new ScriptDefinitionLoader(tags).pullSubDefinitions(scriptDefinition,
                                                                                                     suite.getPath());
                scripts = sort(scripts);
                result.addAll(scripts);
            }
        }
        catch (Exception e)
        {
            log.error("Unexpected error building standard tests", e);
        }

        return result;
    }

    private List<ScriptDefinition> sort(List<ScriptDefinition> scripts)
    {
        List<ScriptDefinition> ordered = new ArrayList<ScriptDefinition>();
        List<ScriptDefinition> named = new ArrayList<ScriptDefinition>();
        for (ScriptDefinition script : scripts)
        {
            if(script.getOrder() != null && script.getOrder().intValue() > 0)
            {
                ordered.add(script);
            }
            else
            {
                named.add(script);
            }
        }
        Collections.sort(ordered, new OrderComparater());
        Collections.sort(named, new NameComparater());

        //order numbers take precedence
        ordered.addAll(named);
        return ordered;
    }

    public List<String> getTags()
    {
        List<String> tags = new ArrayList<String>();
        if (TAGS != null && TAGS.length() > 0)
        {
            log.info("Running tests with tags:" + TAGS);
            String[] tagArray = TAGS.split(",");
            if (tagArray != null && tagArray.length > 0)
            {
                tags.addAll(Arrays.asList(tagArray));
            }
        }
        return tags;
    }

    private class OrderComparater implements Comparator<ScriptDefinition>
    {
        @Override
        public int compare(ScriptDefinition o1, ScriptDefinition o2)
        {
            return o1.getOrder().compareTo(o2.getOrder());
        }
    }

    private class NameComparater implements Comparator<ScriptDefinition>
    {
        @Override
        public int compare(ScriptDefinition o1, ScriptDefinition o2)
        {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
