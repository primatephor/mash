package org.mash.harness;

/**
 * A setup harness is invoked by the framework before running and verifying tests.  Specific implementations may expose
 * some underlying information for use by specific run or verify implementations.
 * <p/>
 * For example, an web run harness may get an http setup harness, allowing access to specific web related setup
 * information.
 * <p/>
 * User: teastlack Date: Jun 30, 2009 Time: 3:53:40 PM
 */
public interface SetupHarness extends Harness
{
    /**
     * Setup is invoked by the framework to perform the implementation specific setup. For a db, this means cleaning and
     * installing data, or for a filesystem, perhaps it means cleaning a directory or rebuilding a structure.
     */
    void setup() throws Exception;
}
