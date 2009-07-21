package org.mash.junit;

import junit.framework.Test;
import org.mash.config.ScriptDefinition;

/**
 * @author: teastlack
 * @since: Jul 3, 2009
 */
public class TestCaseFactory
{
    public Test createTestCase(ScriptDefinition script) throws Exception
    {
        return new StandardTestCase(script);
    }
}
