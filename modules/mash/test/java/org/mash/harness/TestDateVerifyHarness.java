
package org.mash.harness;

import junit.framework.TestCase;
import org.mash.config.Parameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author
 * @since Sep 27, 2010 11:43:26 AM
 */
public class TestDateVerifyHarness extends TestCase
{
    public void testDateEqual()
    {
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse("2009/07/09 13:06:40");
        run.setResponse(response);
        harness.setFormat("yyyy/MM/dd HH:mm:ss");
        harness.getParameters().add(new Parameter("0", "2009/07/09 13:06:40"));
        harness.verify(run, null);
        assertEquals(0, harness.getErrors().size());
    }

    public void testXmlDateEqual() throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setLenient(false);
        System.out.println("DATE:"+format.parse("2009-07-09T13:06:40-7:00"));
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse("2009-07-09T13:06:40-07:00");
        run.setResponse(response);
        harness.setFormat("yyyy-MM-dd'T'HH:mm:ss");
        harness.getParameters().add(new Parameter("0", "2009-07-09T13:06:40-07:00"));
        harness.verify(run, null);
        assertEquals(0, harness.getErrors().size());
    }

    public void testDateNotEqual()
    {
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse("2009/07/09 13:06:40");
        run.setResponse(response);
        harness.setFormat("yyyy/MM/dd HH:mm:ss");
        harness.getParameters().add(new Parameter("0", "2009/07/09 13:06:41"));
        harness.verify(run, null);
        assertEquals(1, harness.getErrors().size());
        assertEquals("0 is out of range", harness.getErrors().get(0).getValue());
        assertEquals("Response date Thu Jul 09 13:06:40 PDT 2009, ms:1247170000000 " +
                     "is out of range of expected date Thu Jul 09 13:06:41 PDT 2009, ms:1247170001000", harness.getErrors().get(0).getDescription());
    }

    public void testDateRange()
    {
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse("2009/07/09 13:06:40");
        run.setResponse(response);
        harness.setFormat("yyyy/MM/dd HH:mm:ss");
        harness.setRangeMillis("1000");
        harness.getParameters().add(new Parameter("0", "2009/07/09 13:06:41"));
        harness.verify(run, null);
        assertEquals(0, harness.getErrors().size());

        harness.setRangeMillis("500");
        harness.verify(run, null);
        assertEquals(1, harness.getErrors().size());
        assertEquals("0 is out of range", harness.getErrors().get(0).getValue());
        assertEquals("Response date Thu Jul 09 13:06:40 PDT 2009, ms:1247170000000 " +
                     "is out of range of expected date Thu Jul 09 13:06:41 PDT 2009, ms:1247170001000", harness.getErrors().get(0).getDescription());
    }

    public void testDateNull()
    {
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse(null);
        run.setResponse(response);
        harness.setFormat("yyyy/MM/dd HH:mm:ss");
        harness.getParameters().add(new Parameter("0", null));
        harness.verify(run, null);
        assertEquals(0, harness.getErrors().size());
    }

    public void testDateNotNull()
    {
        DateVerifyHarness harness = new DateVerifyHarness();
        MyRun run = new MyRun();
        RawResponse response = new RawResponse("2009/07/09 13:06:40");
        run.setResponse(response);
        harness.setFormat("yyyy/MM/dd HH:mm:ss");
        harness.getParameters().add(new Parameter("0", null));
        harness.verify(run, null);
        assertEquals(1, harness.getErrors().size());
        assertEquals("Expected Response Not Null", harness.getErrors().get(0).getValue());
        assertEquals("Expected NULL date for 0, got 2009/07/09 13:06:40", harness.getErrors().get(0).getDescription());
    }

    private class MyRun extends BaseHarness implements RunHarness
    {
        private RunResponse response;
        @Override
        public void run(HarnessContext context)
        {
            //nada
        }

        @Override
        public RunResponse getResponse()
        {
            return this.response;
        }

        public void setResponse(RunResponse response)
        {
            this.response = response;
        }
    }
}
