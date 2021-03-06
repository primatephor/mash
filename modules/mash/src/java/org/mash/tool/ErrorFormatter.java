package org.mash.tool;

import org.mash.config.ScriptDefinition;
import org.mash.harness.HarnessError;

import java.util.List;

/**
 *  Date: Jul 7, 2009 Time: 2:26:44 PM
 */
public class ErrorFormatter
{
    public String format(List<HarnessError> harnessErrors, ScriptDefinition script)
    {
        StringBuffer buffer = new StringBuffer();
        if (harnessErrors != null && harnessErrors.size() > 0)
        {
            buffer.append("Errors found during verification");
            if (script != null && script.getName() != null)
            {
                buffer.append(" of script '").append(script.getName()).append("'");
            }
            buffer.append("\n");
            for (HarnessError error : harnessErrors)
            {
                buffer.append("Harness:")
                        .append(error.getHarnessName())
                        .append(", Error:").append(error.getValue());
                if (error.getDescription() != null)
                {
                    buffer.append(", Description:").append(error.getDescription());
                }
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }
}
