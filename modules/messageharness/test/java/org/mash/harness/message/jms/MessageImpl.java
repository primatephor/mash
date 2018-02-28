package org.mash.harness.message.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author teastlack
 * @since 8/1/17 3:00 PM
 */
public class MessageImpl implements Message
{
    private String JMSMessageID;
    private long JMSTimestamp;
    private Properties properties = new Properties();

    @Override
    public String getJMSMessageID() throws JMSException
    {
        return JMSMessageID;
    }

    @Override
    public void setJMSMessageID(String s) throws JMSException
    {
        JMSMessageID = s;
    }

    @Override
    public long getJMSTimestamp() throws JMSException
    {
        return JMSTimestamp;
    }

    @Override
    public void setJMSTimestamp(long l) throws JMSException
    {
        JMSTimestamp = l;
    }

    @Override
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException
    {
        return new byte[0];
    }

    @Override
    public void setJMSCorrelationIDAsBytes(byte[] bytes) throws JMSException
    {

    }

    @Override
    public void setJMSCorrelationID(String s) throws JMSException
    {

    }

    @Override
    public String getJMSCorrelationID() throws JMSException
    {
        return null;
    }

    @Override
    public Destination getJMSReplyTo() throws JMSException
    {
        return null;
    }

    @Override
    public void setJMSReplyTo(Destination destination) throws JMSException
    {

    }

    @Override
    public Destination getJMSDestination() throws JMSException
    {
        return null;
    }

    @Override
    public void setJMSDestination(Destination destination) throws JMSException
    {

    }

    @Override
    public int getJMSDeliveryMode() throws JMSException
    {
        return 0;
    }

    @Override
    public void setJMSDeliveryMode(int i) throws JMSException
    {

    }

    @Override
    public boolean getJMSRedelivered() throws JMSException
    {
        return false;
    }

    @Override
    public void setJMSRedelivered(boolean b) throws JMSException
    {

    }

    @Override
    public String getJMSType() throws JMSException
    {
        return null;
    }

    @Override
    public void setJMSType(String s) throws JMSException
    {

    }

    @Override
    public long getJMSExpiration() throws JMSException
    {
        return 0;
    }

    @Override
    public void setJMSExpiration(long l) throws JMSException
    {

    }

    @Override
    public long getJMSDeliveryTime() throws JMSException
    {
        return 0;
    }

    @Override
    public void setJMSDeliveryTime(long l) throws JMSException
    {

    }

    @Override
    public int getJMSPriority() throws JMSException
    {
        return 0;
    }

    @Override
    public void setJMSPriority(int i) throws JMSException
    {

    }

    @Override
    public void clearProperties() throws JMSException
    {

    }

    @Override
    public boolean propertyExists(String s) throws JMSException
    {
        return properties.contains(s);
    }

    @Override
    public boolean getBooleanProperty(String s) throws JMSException
    {
        return Boolean.valueOf(properties.getProperty(s));
    }

    @Override
    public byte getByteProperty(String s) throws JMSException
    {
        return Byte.valueOf(properties.getProperty(s));
    }

    @Override
    public short getShortProperty(String s) throws JMSException
    {
        return Short.valueOf(properties.getProperty(s));
    }

    @Override
    public int getIntProperty(String s) throws JMSException
    {
        return Integer.valueOf(properties.getProperty(s));
    }

    @Override
    public long getLongProperty(String s) throws JMSException
    {
        return Long.valueOf(properties.getProperty(s));
    }

    @Override
    public float getFloatProperty(String s) throws JMSException
    {
        return Float.valueOf(properties.getProperty(s));
    }

    @Override
    public double getDoubleProperty(String s) throws JMSException
    {
        return Double.valueOf(properties.getProperty(s));
    }

    @Override
    public String getStringProperty(String s) throws JMSException
    {
        return String.valueOf(properties.getProperty(s));
    }

    @Override
    public Object getObjectProperty(String s) throws JMSException
    {
        return properties.getProperty(s);
    }

    @Override
    public Enumeration getPropertyNames() throws JMSException
    {
        return properties.propertyNames();
    }

    @Override
    public void setBooleanProperty(String s, boolean b) throws JMSException
    {
        properties.setProperty(s, Boolean.valueOf(b).toString());
    }

    @Override
    public void setByteProperty(String s, byte b) throws JMSException
    {
        properties.setProperty(s, Byte.valueOf(b).toString());
    }

    @Override
    public void setShortProperty(String s, short i) throws JMSException
    {
        properties.setProperty(s, Short.valueOf(i).toString());

    }

    @Override
    public void setIntProperty(String s, int i) throws JMSException
    {
        properties.setProperty(s, Integer.valueOf(i).toString());

    }

    @Override
    public void setLongProperty(String s, long l) throws JMSException
    {
        properties.setProperty(s, Long.valueOf(l).toString());

    }

    @Override
    public void setFloatProperty(String s, float v) throws JMSException
    {
        properties.setProperty(s, Float.valueOf(v).toString());

    }

    @Override
    public void setDoubleProperty(String s, double v) throws JMSException
    {
        properties.setProperty(s, Double.valueOf(v).toString());

    }

    @Override
    public void setStringProperty(String s, String s1) throws JMSException
    {
        properties.setProperty(s, s1);

    }

    @Override
    public void setObjectProperty(String s, Object o) throws JMSException
    {
        properties.setProperty(s, String.valueOf(o));

    }

    @Override
    public void acknowledge() throws JMSException
    {

    }

    @Override
    public void clearBody() throws JMSException
    {

    }

    @Override
    public <T> T getBody(Class<T> aClass) throws JMSException
    {
        return null;
    }

    @Override
    public boolean isBodyAssignableTo(Class aClass) throws JMSException
    {
        return false;
    }
}
