package org.mash.loader;

import org.mash.config.Configuration;
import org.mash.config.ScriptDefinition;
import org.mash.harness.Harness;

import java.util.List;

/**
 *
 * @author teastlack
 * @since Sep 17, 2009 12:20:09 PM
 *
 */
public interface ConfigurationBuilder
{
    List<Configuration> applyConfiguration(ScriptDefinition definition,
                                           Harness harness) throws Exception;
}
