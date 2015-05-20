package org.mash.harness.mail;

import junit.framework.TestCase;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Provider;
import javax.mail.Message;
import javax.mail.Address;
import javax.mail.Flags;
import java.util.Properties;

/**
 * @author
 * @since Jul 26, 2010 11:29:45 AM
 */
public class SampleRemoteConnection extends TestCase
{
    public void sampleConnect() throws Exception
    {
        Session mailSession = Session.getDefaultInstance(new Properties());
        //STORE,imap,com.sun.mail.imap.IMAPStore,Sun Microsystems, Inc
        Provider provider = new Provider(Provider.Type.STORE,
                                         "imap",
                                         "com.sun.mail.imap.IMAPStore",
                                         "Sun Microsystems, Inc",
                                         "");
        Store store = mailSession.getStore(provider);
        store.connect("192.168.25.63", "email", "password");

        Folder[] folders = store.getPersonalNamespaces();
        for (Folder folder : folders)
        {
            System.out.println("Personal:" + folder.getFullName());
        }

        folders = store.getSharedNamespaces();
        for (Folder folder : folders)
        {
            System.out.println("Shared:" + folder.getFullName());
        }

        Folder folder = store.getFolder("INBOX");
        System.out.println("FOLDER:" + folder.getName());
        int count = folder.getMessageCount();
        System.out.println("Message count:" + count);
        folder.open(Folder.READ_WRITE);
        Message message = folder.getMessage(1);

        System.out.println("MSG number:" + message.getMessageNumber());
        for (Address address : message.getAllRecipients())
        {
            System.out.println("MSG recipient:" + address.toString());
        }
        System.out.println("MSG subject:" + message.getSubject());
        System.out.println("MSG content:" + message.getContent());

        System.out.println("DELETED msgs:" + folder.getDeletedMessageCount());
        message.setFlag(Flags.Flag.DELETED, true);
        System.out.println("DELETED msgs:" + folder.getDeletedMessageCount());
        Message[] deletedMsgs = folder.expunge();
        for (Message deletedMsg : deletedMsgs)
        {
            System.out.println("Deleted msg:"+deletedMsg.getMessageNumber());
        }
        System.out.println("Message count:" + count);
        folder.close(false);
    }
}
