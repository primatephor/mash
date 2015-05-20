package org.mash.harness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains and manages all the information about previous runs, setups, etc
 *
 * @author
 * @since 1/4/12 2:36 PM
 */
public class HarnessContext
{
    private List<SetupHarness> setupHarnesses;
    private RunHarness lastRun;
    private List<RunHarness> previousRuns;

    private Set<SetupHarness> uniqueSetupHarnesses = new HashSet<SetupHarness>();
    private Set<RunHarness> uniqueRunHarnesses = new HashSet<RunHarness>();

    public List<RunHarness> getPreviousRuns()
    {
        if (previousRuns == null)
        {
            previousRuns = new ArrayList<RunHarness>();
        }
        return previousRuns;
    }

    public List<SetupHarness> getSetupHarnesses()
    {
        if (setupHarnesses == null)
        {
            setupHarnesses = new ArrayList<SetupHarness>();
        }
        return setupHarnesses;
    }

    public RunHarness getLastRun()
    {
        return lastRun;
    }

    public void add(RunHarness lastRun)
    {
        if (lastRun != null)
        {
            if (!uniqueRunHarnesses.contains(lastRun))
            {
                getPreviousRuns().add(lastRun);
            }
            this.lastRun = lastRun;
        }
    }

    public void add(SetupHarness setupHarness)
    {
        if (setupHarness != null && !uniqueSetupHarnesses.contains(setupHarness))
        {
            getSetupHarnesses().add(setupHarness);
        }
    }
}
