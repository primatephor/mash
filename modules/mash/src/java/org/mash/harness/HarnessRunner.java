
package org.mash.harness;

import org.mash.config.ScriptDefinition;

import java.util.List;

/**
 * @author
 * @since Jun 13, 2011
 */
public interface HarnessRunner
{
    List<HarnessError> run(ScriptDefinition definition, Harness harness, HarnessContext context);
}
