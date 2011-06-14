/*
 * Copyright (c) 2011 Ensenda, Inc. All Rights Reserved.
 * This code  is the  sole  property  of  Ensenda Inc.,
 * and is protected  by  copyright  under the  laws of the United
 * States. This program is confidential, proprietary, and a trade
 * secret, not to be disclosed without written authorization from
 * Ensenda Inc.  Any  use, duplication, or  disclosure
 * of  this  program  by other than Ensenda Inc. and its
 * assigned licensees is strictly forbidden by law.
 */

package org.mash.harness;

import org.mash.config.ScriptDefinition;

import java.util.List;

/**
 * @author: teastlack
 * @since: Jun 13, 2011
 */
public interface HarnessRunner extends Runner
{
    List<HarnessError> run(ScriptDefinition definition, Harness harness, List<RunHarness> previousRuns);
}
