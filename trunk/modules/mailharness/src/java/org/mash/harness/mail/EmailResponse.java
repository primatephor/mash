package org.mash.harness.mail;

import org.mash.harness.StringResponse;

import javax.mail.Message;
import javax.mail.Address;
import java.util.List;
import java.util.ArrayList;

/**
 * @author teastlack
 * @since Jul 26, 2010 1:57:54 PM
 */
public class EmailResponse extends StringResponse
{
    private String subject;
    private List<String> recipients;
    private List<String> sender;
    private int count;

    public EmailResponse(Message email, int messageCount) throws Exception
    {
        super(stringValue(email.getContent()));
        this.count = messageCount;
        this.subject = email.getSubject();
        recipients = new ArrayList<String>();
        for (Address address : email.getAllRecipients())
        {
            recipients.add(address.toString());
        }
        sender = new ArrayList<String>();
        for (Address address : email.getFrom())
        {
            sender.add(address.toString());
        }
    }

    private static String stringValue(Object content)
    {
        String result = "";
        if(content != null)
        {
            result = content.toString();
        }
        return result;
    }

    public String getSubject()
    {
        return subject;
    }

    public List<String> getRecipients()
    {
        return recipients;
    }

    public List<String> getSender()
    {
        return sender;
    }

    public int getCount()
    {
        return count;
    }
}
