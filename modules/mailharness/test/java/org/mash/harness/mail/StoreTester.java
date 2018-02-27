package org.mash.harness.mail;

import javax.mail.*;
import java.util.Properties;

public class StoreTester extends Store{

    public StoreTester() {
        super(Session.getInstance(new Properties()), null);
    }

    @Override
    public Folder getDefaultFolder() throws MessagingException {
        return null;
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        return null;
    }

    @Override
    public Folder getFolder(URLName url) throws MessagingException {
        return null;
    }
}
