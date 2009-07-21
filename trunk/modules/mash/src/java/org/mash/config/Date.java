package org.mash.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author teastlack
 * @since Jul 9, 2009 10:59:40 AM
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Date", propOrder = {
        "value"
        }, namespace = "http://www.mash.org/schema/V1")
public class Date
{
    @XmlValue
    protected String value;
    @XmlAttribute
    protected String format;
    @XmlAttribute
    protected Integer secOffset;
    @XmlAttribute
    protected Integer minOffset;
    @XmlAttribute
    protected Integer hourOffset;
    @XmlAttribute
    protected Integer dayOffset;
    @XmlAttribute
    protected Integer monthOffset;
    @XmlAttribute
    protected Integer yearOffset;
    @XmlAttribute
    protected String zone;

    protected Calendar theDate;
    private DateFormat formatter;

    public java.util.Date asDate() throws ConfigurationException
    {
        if (theDate == null)
        {
            theDate = Calendar.getInstance();
            if (value != null &&
                value.length() > 0)
            {
                try
                {
                    java.util.Date date = getFormatter().parse(value);
                    theDate.setTime(date);
                }
                catch (ParseException e)
                {
                    throw new ConfigurationException("Problem parsing the date '" + value + "'", e);
                }
            }
        }

        if (this.secOffset != null)
        {
            theDate.add(Calendar.SECOND, this.secOffset);
        }
        if (this.minOffset != null)
        {
            theDate.add(Calendar.MINUTE, this.minOffset);
        }
        if (this.hourOffset != null)
        {
            theDate.add(Calendar.HOUR_OF_DAY, this.hourOffset);
        }
        if (this.dayOffset != null)
        {
            theDate.add(Calendar.DAY_OF_MONTH, this.dayOffset);
        }
        if (this.monthOffset != null)
        {
            theDate.add(Calendar.MONTH, this.monthOffset);
        }
        if (this.yearOffset != null)
        {
            theDate.add(Calendar.YEAR, this.yearOffset);
        }

        return theDate.getTime();
    }

    public String asFormat() throws ConfigurationException
    {
        java.util.Date date = asDate();
        DateFormat formatter = getFormatter();
        if (this.zone != null)
        {
            formatter.setTimeZone(TimeZone.getTimeZone(this.zone));
        }
        return formatter.format(date);
    }

    private DateFormat getFormatter()
    {
        if (formatter == null)
        {
            if (format != null)
            {
                formatter = new SimpleDateFormat(this.format);
            }
            else
            {
                formatter = new SimpleDateFormat();
            }
        }
        return formatter;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public Integer getYearOffset()
    {
        return yearOffset;
    }

    public void setYearOffset(Integer yearOffset)
    {
        this.yearOffset = yearOffset;
    }

    public Integer getMonthOffset()
    {
        return monthOffset;
    }

    public void setMonthOffset(Integer monthOffset)
    {
        this.monthOffset = monthOffset;
    }

    public Integer getDayOffset()
    {
        return dayOffset;
    }

    public void setDayOffset(Integer dayOffset)
    {
        this.dayOffset = dayOffset;
    }

    public Integer getHourOffset()
    {
        return hourOffset;
    }

    public void setHourOffset(Integer hourOffset)
    {
        this.hourOffset = hourOffset;
    }

    public Integer getMinOffset()
    {
        return minOffset;
    }

    public void setMinOffset(Integer minOffset)
    {
        this.minOffset = minOffset;
    }

    public Integer getSecOffset()
    {
        return secOffset;
    }

    public void setSecOffset(Integer secOffset)
    {
        this.secOffset = secOffset;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getZone()
    {
        return zone;
    }

    public void setZone(String zone)
    {
        this.zone = zone;
    }
}
