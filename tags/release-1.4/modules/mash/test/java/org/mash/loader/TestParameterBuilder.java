package org.mash.loader;

import junit.framework.TestCase;
import org.mash.config.Configuration;
import org.mash.config.Date;
import org.mash.config.HarnessDefinition;
import org.mash.config.Parameter;
import org.mash.config.Script;
import org.mash.harness.AnnotatedDBSetupHarness;
import org.mash.harness.Harness;
import org.mash.loader.harnesssetup.AnnotatedHarness;
import org.mash.loader.harnesssetup.CalculatingConfigBuilder;
import org.mash.loader.harnesssetup.CalculatingParameterBuilder;

import java.util.Calendar;
import java.util.List;

/**
 * @author teastlack
 * @since Jul 9, 2009 4:24:20 PM
 */
public class TestParameterBuilder extends TestCase
{
    public void testDateReplace() throws Exception
    {
        String contents = "<ns1:Script name=\"Sample Fastrack Test\" xmlns:ns1=\"http://code.google.com/p/mash/schema/V1\">\n" +
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

        List<Parameter> appliedParams = new CalculatingParameterBuilder().applyParameters(null, null, (HarnessDefinition) result.getHarnesses().get(0));
        assertEquals(1, appliedParams.size());
        String paramValue = appliedParams.get(0).getValue();
        assertEquals("replace 2009-07-03 13:06:40.0 and 2009-07-04 13:06:40.0 and 2009-07-03 13:06:40.0 again", paramValue);
    }


    public void testAnnotation() throws Exception
    {
        String contents = "<ns1:Script name=\"Sample Fastrack Test\" xmlns:ns1=\"http://code.google.com/p/mash/schema/V1\">\n" +
                          "    <Tag>fasttrack</Tag>\n" +
                          "    <Tag>website</Tag>\n" +
                          "    <Setup type=\"org.mash.harness.AnnotatedDBSetupHarness\">\n" +
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
        HarnessDefinition harnessDefinition = (HarnessDefinition) result.getHarnesses().get(0);
        Date date1 = harnessDefinition.getParameter().get(0).getReplace().get(0).getDate();
        harnessDefinition.getParameter().get(0).getReplace().get(0).setDate(new MyDate(date1, new java.util.Date(1247170000000l)));
        Date date2 = harnessDefinition.getParameter().get(0).getReplace().get(1).getDate();
        harnessDefinition.getParameter().get(0).getReplace().get(1).setDate(new MyDate(date2, new java.util.Date(1247170000000l)));

        Harness toCheck = new HarnessBuilder().buildHarness(harnessDefinition);
        AnnotatedDBSetupHarness db = (AnnotatedDBSetupHarness) ((AnnotatedHarness) toCheck).getWrap();
        List<Parameter> appliedParams = new CalculatingParameterBuilder().applyParameters(null, null, harnessDefinition);
        toCheck.setParameters(appliedParams);
        assertEquals("replace 2009-07-03 13:06:40.0 and 2009-07-04 13:06:40.0 and 2009-07-03 13:06:40.0 again", db.getLoad());
    }

    public void testNameConfig() throws Exception
    {
        String contents = "<ns1:Script name=\"Sample Fastrack Test\" xmlns:ns1=\"http://code.google.com/p/mash/schema/V1\">\n" +
                          "    <Tag>fasttrack</Tag>\n" +
                          "    <Tag>website</Tag>\n" +
                          "    <Setup type=\"org.mash.harness.DBSetupHarness\">\n" +
                          "        <Configuration name=\"type\">DELETE</Configuration>\n" +
                          "        <Configuration name=\"url\" property=\"jdbc.url\"/>\n" +
                          "        <Configuration name=\"user\">mydbuser</Configuration>\n" +
                          "        <Configuration name=\"password\">dbpass</Configuration>\n" +
                          "        <Configuration>" +
                          "             <ParamName>" +
                          "                 <Value>this_will_be_{replaced}</Value>" +
                          "                 <Replace search=\"{replaced}\"><Value>named</Value></Replace>" +
                          "             </ParamName>" +
                          "        </Configuration>\n" +
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
        String name = ((HarnessDefinition) result.getHarnesses().get(0)).getConfiguration().get(4).getName();

        assertEquals(null, name);
        Harness toCheck = new HarnessBuilder().buildHarness((HarnessDefinition) result.getHarnesses().get(0));
        List<Configuration> appliedParams = new CalculatingConfigBuilder().applyParameters(null, null, toCheck.getDefinition());
        assertEquals(5, appliedParams.size());
        String configName = appliedParams.get(4).getName();
        assertEquals("this_will_be_named", configName);
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
