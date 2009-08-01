package org.mash.config;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 11:17:13 AM
 *
 */
public class TestDate extends TestCase
{
    public void testDefault() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date();
        Date current = new Date();
        assertTrue("new date too old", now.getTime() <= current.asDate().getTime());
    }

    //1247170000000  =  2009/07/09 13:06:40
    public void testDefaultFormatting() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        assertEquals("2009/07/09 13:06:40", configDate.asFormat());
    }

    public void testSetDate() throws ConfigurationException
    {
        Date configDate = new Date();
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setValue("2009/07/09 13:06:40");
        assertEquals(1247170000000l, configDate.asDate().getTime());
    }

    public void testSeconds() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setSecOffset(5);
        assertEquals("2009/07/09 13:06:45", configDate.asFormat());
    }

    public void testMinutes() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setMinOffset(5);
        assertEquals("2009/07/09 13:11:40", configDate.asFormat());
    }

    public void testHours() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setHourOffset(5);
        assertEquals("2009/07/09 18:06:40", configDate.asFormat());
    }

    public void testDay() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setDayOffset(5);
        assertEquals("2009/07/14 13:06:40", configDate.asFormat());
    }

    public void testMonth() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setMonthOffset(5);
        assertEquals("2009/12/09 13:06:40", configDate.asFormat());
    }

    public void testYear() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setYearOffset(5);
        assertEquals("2014/07/09 13:06:40", configDate.asFormat());
    }

    public void testZone() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy/MM/dd HH:mm:ss");
        configDate.setZone("EST");
        assertEquals("2009/07/09 16:06:40", configDate.asFormat());
        configDate.setZone("PST");
        assertEquals("2009/07/09 13:06:40", configDate.asFormat());
    }

    public void testFormatAndOffest() throws ConfigurationException
    {
        java.util.Date now = new java.util.Date(1247170000000l);
        Date configDate = new MyDate(now);
        configDate.setFormat("yyyy-MM-dd kk:mm:ss.S");
        configDate.setDayOffset(-6);
        assertEquals("2009-07-03 13:06:40.0", configDate.asFormat());
    }

    private class MyDate extends Date
    {
        private MyDate(java.util.Date toSet)
        {
            super.theDate = Calendar.getInstance();
            super.theDate.setTime(toSet);
        }
    }
}
