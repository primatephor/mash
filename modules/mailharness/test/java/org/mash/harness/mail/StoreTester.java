package org.mash.harness.mail;

import javax.mail.*;
import java.util.Properties;

/**
 * @since Jul/31/17
 */
public class StoreTester extends Store {
    Folder theFolder= null;

    public StoreTester() {
        super(Session.getDefaultInstance(new Properties()), null);
    }
    @Override
    public Folder getDefaultFolder() throws MessagingException {
        return theFolder;
    }

    @Override
    public Folder getFolder(String s) throws MessagingException {
        return theFolder;
    }

    @Override
    public Folder getFolder(URLName urlName) throws MessagingException {
        return theFolder;
    }
}
