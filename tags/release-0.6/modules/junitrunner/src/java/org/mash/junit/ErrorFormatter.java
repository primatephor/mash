package org.mash.junit;

import org.mash.harness.HarnessError;

import java.util.List;

/**
 * User: teastlack Date: Jul 7, 2009 Time: 2:26:44 PM
 */
public class ErrorFormatter
{
    public String format(List<HarnessError> harnessErrors)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Errors found during verification:");
        for (HarnessError error : harnessErrors)
        {
            buffer.append(error.getHarnessName()).append(":").append(error.getValue());
            if (error.getDescription() != null)
            {
                buffer.append(" ").append(error.getDescription());
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
