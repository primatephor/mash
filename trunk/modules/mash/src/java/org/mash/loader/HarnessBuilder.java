package org.mash.loader;

import org.mash.config.HarnessDefinition;
import org.mash.config.ScriptDefinition;
import org.mash.harness.Harness;
import org.mash.harness.RunHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.SetupHarness;
import org.mash.harness.TeardownHarness;
import org.mash.harness.VerifyHarness;
import org.mash.loader.harnesssetup.AnnotatedHarness;

import java.io.File;
import java.util.List;

/**
 * Building a harness entails actually creating an instance of that harness, and applying the configurations to those
 * harnesses.
 * <p/>
 *
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HarnessBuilder
{
    private ScriptDefinitionLoader scriptDefinitionLoader;

    public HarnessBuilder()
    {
        scriptDefinitionLoader = new ScriptDefinitionLoader();
    }

    public Harness buildHarness(HarnessDefinition harnessDefinition) throws HarnessException
    {
        String classname = harnessDefinition.getType();
        Harness harness;
        try
        {
            harness = (Harness) Class.forName(classname).newInstance();
            if (harness instanceof SetupHarness)
            {
                harness = new SetupAnnotatedHarness(harness);
            }
            else if (harness instanceof RunHarness)
            {
                harness = new RunAnnotatedHarness(harness);
            }
            else if (harness instanceof VerifyHarness)
            {
                harness = new VerifyAnnotatedHarness(harness);
            }
            else if (harness instanceof TeardownHarness)
            {
                harness = new TeardownAnnotatedHarness(harness);
            }
            harness.setDefinition(harnessDefinition);
        }
        catch (Exception e)
        {
            throw new HarnessException("Problem building harness", e);
        }
        return harness;
    }

    public ScriptDefinition buildScriptDefinition(ScriptDefinition scriptDefinition, File currentPath) throws HarnessException
    {
        //only works with file definitions, don't run whole directories
        String filename = scriptDefinition.getFile();
        ScriptDefinition fileDef;
        try
        {
            fileDef = scriptDefinitionLoader.pullFile(filename, currentPath);
        }
        catch (Exception e)
        {
            throw new HarnessException("Unable to load script at " + filename, e);
        }
        return fileDef;
    }

    private class SetupAnnotatedHarness extends AnnotatedHarness implements SetupHarness
    {
        private SetupAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void setup() throws Exception
        {
            ((SetupHarness) getWrap()).setup();
        }
    }

    private class RunAnnotatedHarness extends AnnotatedHarness implements RunHarness
    {
        private RunAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void run(List<RunHarness> previous, List<SetupHarness> setups)
        {
            ((RunHarness) getWrap()).run(previous, setups);
        }

        public RunResponse getResponse()
        {
            return ((RunHarness) getWrap()).getResponse();
        }
    }

    private class VerifyAnnotatedHarness extends AnnotatedHarness implements VerifyHarness
    {
        private VerifyAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void verify(RunHarness run, List<SetupHarness> setup)
        {
            ((VerifyHarness) getWrap()).verify(run, setup);
        }
    }

    private class TeardownAnnotatedHarness extends AnnotatedHarness implements TeardownHarness
    {
        private TeardownAnnotatedHarness(Harness wrap)
        {
            super(wrap);
        }

        public void teardown(List<SetupHarness> setups)
        {
            ((TeardownHarness) getWrap()).teardown(setups);
        }
    }
}
