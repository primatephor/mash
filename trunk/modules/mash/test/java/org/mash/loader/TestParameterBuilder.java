package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Date;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.Script;

import java.util.Calendar;
import java.util.List;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 4:24:20 PM
 *
 */
public class TestParameterBuilder extends TestCase
{
    public void testDateReplace() throws Exception
    {
        String contents = "<ns1:Script name=\"Sample Fastrack Test\" xmlns:ns1=\"http://www.mash.org/schema/V1\">\n" +
                          "    <Tag>fasttrack</Tag>\n" +
                          "    <Tag>website</Tag>\n" +
                          "    <Setup type=\"org.mash.harness.DBSetupHarness\">\n" +
                          "        <Configuration name=\"type\">DELETE</Configuration>\n" +
                          "        <Configuration name=\"url\" property=\"jdbc.url\"/>\n" +
                          "        <Configuration name=\"user\">mydbuser</Configuration>\n" +
                          "        <Configuration name=\"password\">dbpass</Configuration>\n" +
                          "        <Configuration name=\"driver\">net.sourceforge.jtds.jdbc.Driver</Configuration>\n" +
                          "        <Parameter name=\"loadfile\">\n" +
                          "<Value>replace {accepted.date} and {another.date} and {accepted.date} again</Value>" +
                          "            <Replace search=\"{accepted.date}\"><Date format=\"yyyy-MM-dd kk:mm:ss.S\" dayOffset=\"-6\"/></Replace>\n" +
                          "            <Replace search=\"{another.date}\"><Date format=\"yyyy-MM-dd kk:mm:ss.S\" dayOffset=\"-5\"/></Replace>\n" +
                          "        </Parameter>\n" +
                          "    </Setup>\n" +
                          "</ns1:Script>";

        JAXBSuiteMarshaller marshaller = new JAXBSuiteMarshaller();
        Script result = (Script) marshaller.unmarshal(contents);
        //replace the dates with our date, so we can validate
        Date date1 = ((HarnessDefinition) result.getHarnesses().get(0)).getParameter().get(0).getReplace().get(0).getDate();
        ((HarnessDefinition) result.getHarnesses().get(0)).getParameter().get(0).getReplace().get(0).setDate(new MyDate(date1, new java.util.Date(1247170000000l)));
        Date date2 = ((HarnessDefinition) result.getHarnesses().get(0)).getParameter().get(0).getReplace().get(1).getDate();
        ((HarnessDefinition) result.getHarnesses().get(0)).getParameter().get(0).getReplace().get(1).setDate(new MyDate(date2, new java.util.Date(1247170000000l)));

        List<Parameter> appliedParams = new ParameterBuilder().applyParameters(null, null, (HarnessDefinition) result.getHarnesses().get(0));
        assertEquals(1, appliedParams.size());
        String paramValue = appliedParams.get(0).getValue();
        assertEquals("replace 2009-07-03 13:06:40.0 and 2009-07-04 13:06:40.0 and 2009-07-03 13:06:40.0 again", paramValue);
    }

    private class MyDate extends Date
    {
        private MyDate(Date parent, java.util.Date toSet)
        {
            super.theDate = Calendar.getInstance();
            super.theDate.setTime(toSet);
            this.setFormat(parent.getFormat());
            this.setSecOffset(parent.getSecOffset());
            this.setMinOffset(parent.getMinOffset());
            this.setHourOffset(parent.getHourOffset());
            this.setDayOffset(parent.getDayOffset());
            this.setMonthOffset(parent.getMonthOffset());
            this.setYearOffset(parent.getYearOffset());
            this.setZone(parent.getZone());
            this.setValue(parent.getValue());
        }
    }

}
