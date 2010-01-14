package org.mash.loader.harnesssetup;

import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.ScriptDefinition;
import org.mash.harness.Harness;
import org.mash.loader.AccessorChain;
import org.mash.loader.ConfigurationBuilder;
import org.mash.loader.accessor.DateAccessor;
import org.mash.loader.accessor.FileAccessor;
import org.mash.loader.accessor.PropertyAccessor;
import org.mash.loader.accessor.ReplaceAccessor;
import org.mash.loader.accessor.ValueAccessor;

import java.io.File;
import java.util.ArrayList;
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
public class CalculatingConfigBuilder implements ConfigurationBuilder
{
    private AccessorChain accessChain;

    public List<Configuration> applyConfiguration(ScriptDefinition definition,
                                                  Harness harness) throws Exception
    {
        HarnessDefinition harnessDefinition = harness.getDefinition();
        AccessorChain chain = buildChain(definition.getPath());
        List<Configuration> appliedConfigurations = new ArrayList<Configuration>();
        for (Configuration configuration : harnessDefinition.getConfiguration())
        {
            String content = chain.access(configuration);
            Configuration toAdd = new Configuration();
            toAdd.setName(configuration.getName());
            toAdd.setFile(configuration.getFile());
            toAdd.setValue(content);
            appliedConfigurations.add(toAdd);
        }
        return appliedConfigurations;
    }

    protected AccessorChain buildChain(File path)
    {
        if (accessChain == null)
        {
            accessChain = new AccessorChain();
            accessChain.add(new ValueAccessor());
            accessChain.add(new PropertyAccessor());
            accessChain.add(new DateAccessor());
            accessChain.add(new FileAccessor(path));

            AccessorChain replaceChain = new AccessorChain();
            replaceChain.add(new ValueAccessor());
            replaceChain.add(new PropertyAccessor());
            replaceChain.add(new DateAccessor());
            replaceChain.add(new FileAccessor(path));
            accessChain.add(new ReplaceAccessor(replaceChain));
        }
        return accessChain;
    }
}
