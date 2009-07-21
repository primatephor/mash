package org.mash.loader;

import org.mash.config.Configuration;
import org.mash.config.HarnessDefinition;
import org.mash.config.ScriptDefinition;
import org.mash.harness.Harness;
import org.mash.loader.accessor.DateAccessor;
import org.mash.loader.accessor.FileAccessor;
import org.mash.loader.accessor.PropertyAccessor;
import org.mash.loader.accessor.ReplaceAccessor;
import org.mash.loader.accessor.ValueAccessor;

import java.io.File;

/**
 * Building a harness entails actually creating an instance of that harness, and applying the configurations to those
 * harnesses.
 * <p/>
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
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public class HarnessBuilder
{
    private AccessorChain accessChain;

    public Harness buildHarness(HarnessDefinition harnessDefinitionDefinition,
                                ScriptDefinition definition) throws HarnessException
    {
        String classname = harnessDefinitionDefinition.getType();
        Harness harness;
        try
        {
            harness = (Harness) Class.forName(classname).newInstance();
            AccessorChain chain = buildChain(definition.getPath());
            for (Configuration configuration : harnessDefinitionDefinition.getConfiguration())
            {
                String content = chain.access(configuration);
                configuration.setValue(content);
            }
            harness.setConfiguration(harnessDefinitionDefinition.getConfiguration());
        }
        catch (Exception e)
        {
            throw new HarnessException("Problem building harness", e);
        }
        return harness;
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
