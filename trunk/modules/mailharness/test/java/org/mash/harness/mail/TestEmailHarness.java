package org.mash.harness.mail;

import junit.framework.TestCase;

import javax.mail.MessagingException;
import javax.mail.Address;
import javax.mail.Folder;

/**
 * @author teastlack
 * @since Jul 26, 2010 3:59:50 PM
 */
public class TestEmailHarness extends TestCase
{
    public void testGetNumber() throws Exception
    {
        FolderTester folder = new FolderTester();
        folder.addMessage(buildMessage("subject1", "content1"));
        folder.addMessage(buildMessage("subject2", "content2"));
        MyEmailHarness myEmailHarness = new MyEmailHarness(folder);
        myEmailHarness.setMessageNumber("1");
        myEmailHarness.run(null, null);
        EmailResponse response = (EmailResponse) myEmailHarness.getResponse();
        assertEquals("subject1", response.getSubject());
        myEmailHarness.setMessageNumber("2");
        myEmailHarness.run(null, null);
        response = (EmailResponse) myEmailHarness.getResponse();
        assertEquals("subject2", response.getSubject());
    }

    public void testRecipient() throws Exception
    {
        FolderTester folder = new FolderTester();
        folder.addMessage(buildMessage("subject1", "content1"));
        folder.addMessage(buildMessage("subject2", "content2"));
        folder.addMessage(buildMessage("subject3", "content3"));
        folder.addMessage(buildMessage("subject4", "content4"));
        folder.getMessage(2).addRecipient(null, new AddressTester("rep3@somewhere.com"));
        folder.getMessage(4).addRecipient(null, new AddressTester("rep3@somewhere.com"));
        MyEmailHarness myEmailHarness = new MyEmailHarness(folder);
        myEmailHarness.setMessageNumber("1");
        myEmailHarness.setAddress("rep3@somewhere.com");
        myEmailHarness.run(null, null);
        EmailResponse response = (EmailResponse) myEmailHarness.getResponse();
        assertEquals("subject2", response.getSubject());
        myEmailHarness.setMessageNumber("2");
        myEmailHarness.run(null, null);
        response = (EmailResponse) myEmailHarness.getResponse();
        assertEquals("subject4", response.getSubject());
    }

    private MessageTester buildMessage(String subject, String content)
            throws MessagingException
    {
        MessageTester message = new MessageTester();
        message.setText(content);
        message.setSubject(subject);
        Address[] from= new Address[1];
        from[0] = new AddressTester("from@somewhere.com");
        Address[] recipient= new Address[2];
        recipient[0] = new AddressTester("rep1@somewhere.com");
        recipient[1] = new AddressTester("rep2@somewhere.com");
        message.addFrom(from);
        message.setRecipients(null, recipient);
        return message;
    }

    private class MyEmailHarness extends GetIMAPEmail
    {
        private FolderTester folder;

        private MyEmailHarness(FolderTester folder)
        {
            this.folder = folder;
        }

        protected Folder connect() throws MessagingException
        {
            return folder;
        }
    }

}
