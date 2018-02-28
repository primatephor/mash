package org.mash.harness.message.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @author teastlack
 * @since 8/1/17 2:58 PM
 */
public class TextMessageImpl extends MessageImpl implements TextMessage
{
    private String text;

    @Override
    public void setText(String s) throws JMSException
    {
        this.text = s;
    }

    @Override
    public String getText() throws JMSException
    {
        return text;
    }

}
