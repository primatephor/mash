package org.mash.harness.mail;

import junit.framework.TestCase;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.MessagingException;

/**
 * @author teastlack
 * @since Jul 26, 2010 3:29:01 PM
 */
public class TestIMAPSetup extends TestCase
{
    public void testCleanAll() throws Exception
    {
        FolderTester folder = new FolderTester();
        folder.addMessage(buildMessage("subject1", "content1"));
        folder.addMessage(buildMessage("subject2", "content2"));
        MyIMAPSetup myIMAPSetup = new MyIMAPSetup(folder);
        myIMAPSetup.setup();
        assertEquals(false, folder.getMessage(1).isExpunged());
        myIMAPSetup.setAction("CLEAN");
        myIMAPSetup.setup();
        assertEquals(true, folder.getMessage(1).isExpunged());
        assertEquals(true, folder.getMessage(2).isExpunged());
    }


    public void testCleanRecipient() throws Exception
    {
        FolderTester folder = new FolderTester();
        folder.addMessage(buildMessage("subject1", "content1"));
        folder.addMessage(buildMessage("subject2", "content2"));
        folder.addMessage(buildMessage("subject3", "content3"));
        folder.getMessage(2).addRecipient(null, new AddressTester("rep3@somewhere.com"));

        MyIMAPSetup myIMAPSetup = new MyIMAPSetup(folder);
        myIMAPSetup.setup();
        myIMAPSetup.setAction("CLEAN");
        myIMAPSetup.setEmailAddress("rep3@somewhere.com");
        myIMAPSetup.setup();
        assertEquals(false, folder.getMessage(1).isExpunged());
        assertEquals(true, folder.getMessage(2).isExpunged());
        assertEquals(false, folder.getMessage(3).isExpunged());
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

    private class MyIMAPSetup extends IMAPSetupHarness
    {
        private FolderTester folder;

        private MyIMAPSetup(FolderTester folder)
        {
            this.folder = folder;
        }

        protected Folder getFolder() throws MessagingException
        {
            return folder;
        }
    }
}
