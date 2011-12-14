package org.mash.harness.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Flags;
import javax.mail.Message;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author teastlack
 * @since Jul 26, 2010 3:35:35 PM
 */
public class FolderTester extends Folder
{
    private String name;
    private boolean open = false;
    private List<Message> messages = new ArrayList<Message>();

    public FolderTester()
    {
        super(null);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }
    public String getFullName()
    {
        throw new UnsupportedOperationException("Method getFullName not yet implemented");
    }

    public Folder getParent() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getParent not yet implemented");
    }

    public boolean exists() throws MessagingException
    {
        throw new UnsupportedOperationException("Method exists not yet implemented");
    }

    public Folder[] list(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method list not yet implemented");
    }

    public char getSeparator() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getSeparator not yet implemented");
    }

    public int getType() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getType not yet implemented");
    }

    public boolean create(int i) throws MessagingException
    {
        throw new UnsupportedOperationException("Method create not yet implemented");
    }

    public boolean hasNewMessages() throws MessagingException
    {
        throw new UnsupportedOperationException("Method hasNewMessages not yet implemented");
    }

    public Folder getFolder(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method getFolder not yet implemented");
    }

    public boolean delete(boolean b) throws MessagingException
    {
        throw new UnsupportedOperationException("Method delete not yet implemented");
    }

    public boolean renameTo(Folder folder) throws MessagingException
    {
        throw new UnsupportedOperationException("Method renameTo not yet implemented");
    }

    public void open(int i) throws MessagingException
    {
        open = true;
    }

    public void close(boolean b) throws MessagingException
    {
        open = false;
    }

    public boolean isOpen()
    {
        return open;
    }

    public Flags getPermanentFlags()
    {
        throw new UnsupportedOperationException("Method getPermanentFlags not yet implemented");
    }

    public int getMessageCount() throws MessagingException
    {
        return messages.size();
    }

    public Message getMessage(int i) throws MessagingException
    {
        return messages.get(i-1);
    }

    public void appendMessages(Message[] messages) throws MessagingException
    {
        throw new UnsupportedOperationException("Method appendMessages not yet implemented");
    }

    public Message[] expunge() throws MessagingException
    {
        List<Message> results = new ArrayList<Message>();
        for (Message message : messages)
        {
            if(message.isExpunged())
            {
                results.add(message);
            }
        }
        Message[] msgs = new Message[results.size()];
        results.toArray(msgs);
        return msgs;
    }
}
