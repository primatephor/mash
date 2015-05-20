package org.mash.junit;

import org.mash.harness.Harness;
import org.mash.harness.StandardScriptRunner;

import java.util.List;

/**
 *  Date: Jul 7, 2009 Time: 4:02:30 PM
 */
public class MyScriptRunner extends StandardScriptRunner
{
    public List<Harness> getHarnesses()
    {
        return super.harnesses;
    }
}