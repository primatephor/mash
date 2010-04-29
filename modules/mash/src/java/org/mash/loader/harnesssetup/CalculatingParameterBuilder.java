package org.mash.loader.harnesssetup;

import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.loader.ParameterBuilder;

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
public class CalculatingParameterBuilder extends ParameterBuilder<Parameter>
{
    protected List<Parameter> getConfigParams(HarnessDefinition harnessDefinition)
    {
        return harnessDefinition.getParameter();
    }

    protected Parameter buildParameter()
    {
        return new Parameter();
    }
}
