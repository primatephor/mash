/*******************************************************************************
 * Copyright (c) 2010 Ensenda, Inc. All Rights Reserved.
 * This code  is the  sole  property  of  Ensenda Inc.,
 * and is protected  by  copyright  under the  laws of the United
 * States. This program is confidential, proprietary, and a trade
 * secret, not to be disclosed without written authorization from
 * Ensenda Inc.  Any  use, duplication, or  disclosure
 * of  this  program  by other than Ensenda Inc. and its
 * assigned licensees is strictly forbidden by law.
 ******************************************************************************/

package org.mash.harness;

import org.mash.loader.HarnessConfiguration;

/**
 *
 * @author
 * @since Sep 27, 2010 11:01:51 AM
 */
public class XmlDateVerifyHarness extends DateVerifyHarness
{
    protected void verifyParameters(RunResponse response)
    {
        XmlResponse xmlResponse = new XmlResponse(response.getString());
        super.verifyParameters(xmlResponse);
    }

    @HarnessConfiguration(name = "format")
    public void setFormat(String format)
    {
        super.setFormat(format);
    }

    @HarnessConfiguration(name = "range_millis")
    public void setRangeMillis(String rangeMillis)
    {
        super.setRangeMillis(rangeMillis);
    }
}
