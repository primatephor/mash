package org.mash.loader.accessor;

import org.apache.log4j.Logger;
import org.mash.config.BaseParameter;
import org.mash.config.Parameter;
import org.mash.config.ScriptDefinition;
import org.mash.loader.AccessorChain;
import org.mash.loader.ContentAccessor;

/**
 * @author teastlack
 * @since Jun 13, 2011 4:31:03 PM
 */
public class ScriptParameterAccessor implements ContentAccessor
{
    private static final Logger log = Logger.getLogger(ScriptParameterAccessor.class.getName());
    private ScriptDefinition scriptDefinition;
    private AccessorChain accessorChain;

    public ScriptParameterAccessor(ScriptDefinition scriptDefinition, AccessorChain accessorChain)
    {
        this.scriptDefinition = scriptDefinition;
        this.accessorChain = accessorChain;
    }

    @Override
    public String accessContent(BaseParameter parameter, String currentContent) throws Exception
    {
        StringBuffer result = new StringBuffer();
        if (parameter.getScriptParameter() != null && parameter.getScriptParameter().length() > 0)
        {
            for (Parameter scriptParameter : scriptDefinition.getParameter())
            {
                if (parameter.getScriptParameter().equals(scriptParameter.getName()))
                {
                    String value = accessorChain.access(scriptParameter);
                    if (value != null)
                    {
                        log.debug("Setting " + scriptParameter.getName() + " to " + value);
                        result.append(value);
                    }
                }
            }
        }
        else if (currentContent != null)
        {
            result.append(currentContent);
        }
        return result.toString();

    }
}
