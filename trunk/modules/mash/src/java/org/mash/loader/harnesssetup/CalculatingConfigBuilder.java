package org.mash.loader.harnesssetup;

import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.loader.ParameterBuilder;

import java.util.List;

/**
 * Applied configurations retrieve the specified properties and appends the value in the definition.
 * <p/>
 * Configuration values are populated according to the accessors defined in the loader.accessor package.  View the
 * accessors javadoc there for more information.  Since there are no responses for configuration, responses are ignored.
 * The order of access is:
 * <ul>
 * <li>ValueAccessor</li>
 * <li>PropertyAccessor</li>
 * <li>DateAccessor</li>
 * <li>FileAccessor</li>
 * <li>ReplaceAccessor (can replace data in any of the above)</li>
 * </ul>
 *
 * To find values to replace, the replace also makes use of these accessors.  They are:
 * <ul>
 * <li>ValueAccessor</li>
 * <li>PropertyAccessor</li>
 * <li>DateAccessor</li>
 * <li>FileAccessor</li>
 * </ul>
 * <p/>
 *
 * @author teastlack
 * @since Sep 17, 2009 12:16:40 PM
 *
 */
public class CalculatingConfigBuilder extends ParameterBuilder<Configuration>
{
    protected List<Configuration> getConfigParams(HarnessDefinition harnessDefinition)
    {
        return harnessDefinition.getConfiguration();
    }

    protected Configuration buildParameter()
    {
        return new Configuration();
    }
}
