package org.mash.harness;

import org.mash.config.ScriptDefinition;

import java.util.List;

/**
 * @author: teastlack
 * @since: Jun 13, 2011
 */
public interface HarnessRunner extends Runner
{
    List<HarnessError> run(ScriptDefinition definition, Harness harness, List<RunHarness> previousRuns);
}
