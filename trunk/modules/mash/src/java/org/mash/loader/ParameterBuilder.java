package org.mash.loader;

import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.ScriptDefinition;
import org.mash.harness.RunHarness;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 12:14:59 PM
 *
 */
public interface ParameterBuilder
{
    List<Parameter> applyParameters(List<RunHarness> previousRun,
                                    ScriptDefinition scriptDefinition,
                                    HarnessDefinition harnessDefinition) throws Exception;
}
