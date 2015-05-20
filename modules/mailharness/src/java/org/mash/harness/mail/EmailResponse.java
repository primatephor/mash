package org.mash.harness.mail;

import org.mash.harness.RawResponse;
import org.mash.tool.StringUtil;

import javax.mail.Address;
import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @since Jul 26, 2010 1:57:54 PM
 */
public class EmailResponse extends RawResponse
{
    private String subject;
    private List<String> recipients;
    private List<String> sender;
    private int count;
    private String content;

    public EmailResponse(Message email, int messageCount) throws Exception
    {
        super(stringValue(email.getContent()));
        this.content = email.getContent().toString();
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

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("To:").append(StringUtil.toString(this.recipients)).append("\n");
        buffer.append("From:").append(StringUtil.toString(this.sender)).append("\n");
        buffer.append("Subject:").append(this.subject).append("\n");
        buffer.append("Content:").append(this.content).append("\n");
        return buffer.toString();
    }
}
