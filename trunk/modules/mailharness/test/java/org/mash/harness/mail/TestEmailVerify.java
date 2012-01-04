package org.mash.harness.mail;

import junit.framework.TestCase;
import org.mash.harness.HarnessContext;
import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;
import org.mash.harness.BaseHarness;

import javax.mail.Address;
import javax.mail.MessagingException;
import java.util.List;

/**
 *
 * @author teastlack
 * @since Jul 26, 2010 3:00:42 PM
 */
public class TestEmailVerify extends TestCase
{
    public void testCount() throws Exception
    {
        MessageTester message = buildMessage();
        MyRunHarness harness = new MyRunHarness(new EmailResponse(message, 20));
        EmailVerifyHarness emailVerifyHarness = new EmailVerifyHarness();
        emailVerifyHarness.setCount("20");
        emailVerifyHarness.verify(harness, null);
        assertEquals(0, emailVerifyHarness.getErrors().size());
        emailVerifyHarness.setCount("19");
        emailVerifyHarness.verify(harness, null);
        assertEquals(1, emailVerifyHarness.getErrors().size());
        assertEquals("Verify Message Count", emailVerifyHarness.getErrors().get(0).getValue());
        assertEquals("Expected count '19' doesn't equal actual '20'", emailVerifyHarness.getErrors().get(0).getDescription());
    }

    public void testSubject() throws Exception
    {
        MessageTester message = buildMessage();
        MyRunHarness harness = new MyRunHarness(new EmailResponse(message, 20));
        EmailVerifyHarness emailVerifyHarness = new EmailVerifyHarness();
        emailVerifyHarness.setSubject("subject");
        emailVerifyHarness.verify(harness, null);
        assertEquals(0, emailVerifyHarness.getErrors().size());
        emailVerifyHarness.setSubject("different subject");
        emailVerifyHarness.verify(harness, null);
        assertEquals(1, emailVerifyHarness.getErrors().size());
        assertEquals("Verify Message Subject", emailVerifyHarness.getErrors().get(0).getValue());
        assertEquals("Expected subject 'different subject' doesn't equal actual 'subject'", emailVerifyHarness.getErrors().get(0).getDescription());
    }

    public void testRecipient() throws Exception
    {
        MessageTester message = buildMessage();
        MyRunHarness harness = new MyRunHarness(new EmailResponse(message, 20));
        EmailVerifyHarness emailVerifyHarness = new EmailVerifyHarness();
        emailVerifyHarness.setRecipients("rep1@somewhere.com");
        emailVerifyHarness.verify(harness, null);
        assertEquals(0, emailVerifyHarness.getErrors().size());
        emailVerifyHarness.setRecipients("rep3@somewhere.com");
        emailVerifyHarness.setRecipients("rep4@somewhere.com");
        emailVerifyHarness.verify(harness, null);
        assertEquals(2, emailVerifyHarness.getErrors().size());
        assertEquals("Verify Message Recipient", emailVerifyHarness.getErrors().get(0).getValue());
        assertEquals("Expected recipient 'rep3@somewhere.com' not present", emailVerifyHarness.getErrors().get(0).getDescription());
        assertEquals("Expected recipient 'rep4@somewhere.com' not present", emailVerifyHarness.getErrors().get(1).getDescription());
    }

    public void testSender() throws Exception
    {
        MessageTester message = buildMessage();
        MyRunHarness harness = new MyRunHarness(new EmailResponse(message, 20));
        EmailVerifyHarness emailVerifyHarness = new EmailVerifyHarness();
        emailVerifyHarness.setSender("from@somewhere.com");
        emailVerifyHarness.verify(harness, null);
        assertEquals(0, emailVerifyHarness.getErrors().size());
        emailVerifyHarness.setSender("from1@somewhere.com");
        emailVerifyHarness.verify(harness, null);
        assertEquals(1, emailVerifyHarness.getErrors().size());
        assertEquals("Verify Message Sender", emailVerifyHarness.getErrors().get(0).getValue());
        assertEquals("Expected sender 'from1@somewhere.com' not present", emailVerifyHarness.getErrors().get(0).getDescription());
    }

    private MessageTester buildMessage()
            throws MessagingException
    {
        MessageTester message = new MessageTester();
        message.setText("content");
        message.setSubject("subject");
        Address[] from= new Address[1];
        from[0] = new AddressTester("from@somewhere.com");
        Address[] recipient= new Address[2];
        recipient[0] = new AddressTester("rep1@somewhere.com");
        recipient[1] = new AddressTester("rep2@somewhere.com");
        message.addFrom(from);
        message.setRecipients(null, recipient);
        return message;
    }

    private class MyRunHarness extends BaseHarness implements RunHarness
    {
        private RunResponse run;

        private MyRunHarness(RunResponse run)
        {
            this.run = run;
        }

        public void run(HarnessContext context)
        {
        }

        public RunResponse getResponse()
        {
            return run;
        }
    }
}
