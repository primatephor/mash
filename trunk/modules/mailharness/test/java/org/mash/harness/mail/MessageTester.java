package org.mash.harness.mail;

import javax.mail.Message;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.activation.DataHandler;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author teastlack
 * @since Jul 26, 2010 3:03:09 PM
 */
public class MessageTester extends Message
{
    Address[] from;
    List<Address> recipient = new ArrayList<Address>();
    String subject;
    String content;

    public void setFlag(Flags.Flag flag, boolean b) throws MessagingException
    {
        setExpunged(b);
    }

    public Address[] getFrom() throws MessagingException
    {
        return from;
    }

    public void setFrom() throws MessagingException
    {
        throw new UnsupportedOperationException("Method setFrom not yet implemented");
    }

    public Address[] getAllRecipients() throws MessagingException
    {
        Address[] results = new Address[recipient.size()];
        recipient.toArray(results);
        return results;
    }

    public void setFrom(Address address) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setFrom not yet implemented");
    }

    public void addFrom(Address[] addresses) throws MessagingException
    {
        this.from = addresses;
    }

    public void addRecipient(RecipientType recipientType, Address address) throws MessagingException
    {
        recipient.add(address);
    }

    public Address[] getRecipients(RecipientType recipientType) throws MessagingException
    {
        Address[] results = new Address[recipient.size()];
        recipient.toArray(results);
        return results;
    }

    public void setRecipients(RecipientType recipientType, Address[] addresses) throws MessagingException
    {
        recipient.addAll(Arrays.asList(addresses));
    }

    public void addRecipients(RecipientType recipientType, Address[] addresses) throws MessagingException
    {
        throw new UnsupportedOperationException("Method addRecipients not yet implemented");
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public Date getSentDate() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getSentDate not yet implemented");
    }

    public void setSentDate(Date date) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setSentDate not yet implemented");
    }

    public Date getReceivedDate() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getReceivedDate not yet implemented");
    }

    public Flags getFlags() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getFlags not yet implemented");
    }

    public void setFlags(Flags flags, boolean b) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setFlags not yet implemented");
    }

    public Message reply(boolean b) throws MessagingException
    {
        throw new UnsupportedOperationException("Method reply not yet implemented");
    }

    public void saveChanges() throws MessagingException
    {
        throw new UnsupportedOperationException("Method saveChanges not yet implemented");
    }

    public int getSize() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getSize not yet implemented");
    }

    public int getLineCount() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getLineCount not yet implemented");
    }

    public String getContentType() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getContentType not yet implemented");
    }

    public boolean isMimeType(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method isMimeType not yet implemented");
    }

    public String getDisposition() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getDisposition not yet implemented");
    }

    public void setDisposition(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setDisposition not yet implemented");
    }

    public String getDescription() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getDescription not yet implemented");
    }

    public void setDescription(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setDescription not yet implemented");
    }

    public String getFileName() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getFileName not yet implemented");
    }

    public void setFileName(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setFileName not yet implemented");
    }

    public InputStream getInputStream() throws IOException, MessagingException
    {
        throw new UnsupportedOperationException("Method getInputStream not yet implemented");
    }

    public DataHandler getDataHandler() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getDataHandler not yet implemented");
    }

    public Object getContent() throws IOException, MessagingException
    {
        return content;
    }

    public void setDataHandler(DataHandler dataHandler) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setDataHandler not yet implemented");
    }

    public void setContent(Object o, String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setContent not yet implemented");
    }

    public void setText(String s) throws MessagingException
    {
        this.content = s;
    }

    public void setContent(Multipart multipart) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setContent not yet implemented");
    }

    public void writeTo(OutputStream outputStream) throws IOException, MessagingException
    {
        throw new UnsupportedOperationException("Method writeTo not yet implemented");
    }

    public String[] getHeader(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method getHeader not yet implemented");
    }

    public void setHeader(String s, String s1) throws MessagingException
    {
        throw new UnsupportedOperationException("Method setHeader not yet implemented");
    }

    public void addHeader(String s, String s1) throws MessagingException
    {
        throw new UnsupportedOperationException("Method addHeader not yet implemented");
    }

    public void removeHeader(String s) throws MessagingException
    {
        throw new UnsupportedOperationException("Method removeHeader not yet implemented");
    }

    public Enumeration getAllHeaders() throws MessagingException
    {
        throw new UnsupportedOperationException("Method getAllHeaders not yet implemented");
    }

    public Enumeration getMatchingHeaders(String[] strings) throws MessagingException
    {
        throw new UnsupportedOperationException("Method getMatchingHeaders not yet implemented");
    }

    public Enumeration getNonMatchingHeaders(String[] strings) throws MessagingException
    {
        throw new UnsupportedOperationException("Method getNonMatchingHeaders not yet implemented");
    }
}
