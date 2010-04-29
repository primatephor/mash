package org.mash.loader;

import org.apache.log4j.Logger;
import org.mash.config.HarnessDefinition;
import org.mash.config.ScriptDefinition;
import org.mash.config.BaseParameter;
import org.mash.harness.RunHarness;
import org.mash.loader.accessor.DateAccessor;
import org.mash.loader.accessor.FileAccessor;
import org.mash.loader.accessor.PropertyAccessor;
import org.mash.loader.accessor.ReplaceAccessor;
import org.mash.loader.accessor.ResponseAccessor;
import org.mash.loader.accessor.ValueAccessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Before a test harness is invoked, the definition parameters are populated.  Some from retrieving data files, some
 * from previous run results.
 * <p/>
 * Parameter values are populated according to the accessors defined in the loader.accessor package.  View the accessors
 * javadoc there for more information.  The order of access is:
 * <ul>
 * <li>ValueAccessor</li>
 * <li>PropertyAccessor</li>
 * <li>DateAccessor</li>
 * <li>ResponseAccessor</li>
 * <li>FileAccessor</li>
 * <li>ReplaceAccessor (can replace data in any of the above)</li>
 * </ul>
 *
 * To find values to replace, the replace also makes use of these accessors.  They are:
 * <ul>
 * <li>ValueAccessor</li>
 * <li>PropertyAccessor</li>
 * <li>DateAccessor</li>
 * <li>ResponseAccessor</li>
 * <li>FileAccessor</li>
 * </ul>
 * <p/>
 *
 * @author: teastlack
 * @since: Jul 4, 2009
 */
public abstract class ParameterBuilder<T extends BaseParameter>
{
    private static final Logger log = Logger.getLogger(ParameterBuilder.class.getName());
    private AccessorChain accessChain;

    public List<T> applyParameters(List<RunHarness> previousRun,
                                   ScriptDefinition scriptDefinition,
                                   HarnessDefinition harnessDefinition) throws Exception
    {
        File path = null;
        if (scriptDefinition != null)
        {
            path = scriptDefinition.getPath();
        }
        AccessorChain chain = buildChain(previousRun, path);
        List<T> appliedParameters = new ArrayList<T>();
        List<T> currentConfig = getConfigParams(harnessDefinition);
        for (T parameter : currentConfig)
        {
            String name = parameter.getName();
            if ((name == null || name.trim().length() == 0) &&
                parameter.getParamName() != null)
            {
                name = chain.access(parameter.getParamName());
            }
            log.info("Calculating parameter " + name);
            String contents = chain.access(parameter);
            if (contents == null)
            {
                log.warn("Parameter '" + parameter.getName() + "' has no content!");
            }
            T toAdd = buildParameter();
            toAdd.setName(name);
            toAdd.setFile(parameter.getFile());
            toAdd.setValue(contents);
            appliedParameters.add(toAdd);
        }

        return appliedParameters;
    }

    protected abstract T buildParameter();

    protected abstract List<T> getConfigParams(HarnessDefinition harnessDefinition);

    protected AccessorChain buildChain(List<RunHarness> previousRun, File path)
    {
        if (accessChain == null)
        {
            accessChain = new AccessorChain();
            accessChain.add(new ValueAccessor());
            accessChain.add(new PropertyAccessor());
            accessChain.add(new DateAccessor());
            if (previousRun != null)
            {
                accessChain.add(new ResponseAccessor(previousRun));
            }
            accessChain.add(new FileAccessor(path));

            AccessorChain replaceChain = new AccessorChain();
            replaceChain.add(new ValueAccessor());
            replaceChain.add(new PropertyAccessor());
            replaceChain.add(new DateAccessor());
            if (previousRun != null)
            {
                replaceChain.add(new ResponseAccessor(previousRun));
            }
            replaceChain.add(new FileAccessor(path));
            accessChain.add(new ReplaceAccessor(replaceChain));
        }
        return accessChain;
    }
}
