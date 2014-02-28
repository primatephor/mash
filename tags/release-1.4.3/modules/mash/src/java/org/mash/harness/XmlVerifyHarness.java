package org.mash.harness;

/**
 *
 * @author teastlack
 * @since Sep 30, 2009 8:42:41 PM
 *
 */
public class XmlVerifyHarness extends StandardVerifyHarness
{
    protected void verifyParameters(RunResponse response)
    {
        XmlResponse xmlResponse = new XmlResponse(response.getString());
        super.verifyParameters(xmlResponse);
    }
}
