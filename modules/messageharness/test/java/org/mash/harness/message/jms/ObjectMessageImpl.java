package org.mash.harness.message.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @author teastlack
 * @since 8/1/17 3:02 PM
 */
public class ObjectMessageImpl extends MessageImpl implements ObjectMessage
{
    private Serializable theObject;

    @Override
    public void setObject(Serializable serializable) throws JMSException
    {
        theObject = serializable;
    }

    @Override
    public Serializable getObject() throws JMSException
    {
        return theObject;
    }
}
