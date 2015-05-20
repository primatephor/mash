package org.mash.harness.db.hbase;

import org.apache.log4j.Logger;

/**
 * Column families, qualifiers, and values are needed enough to warrant breaking out a class to
 * handle that work.  Follows the format 'family:qualifier=value'
 *
 * @author:
 * @since: Dec 5, 2010
 */
public class Entry
{
    private static final Logger log = Logger.getLogger(Entry.class.getName());
    private String family;
    private String column;
    private String value;

    public Entry(String entry)
    {
        int familyEndIndex = entry.indexOf(':');
        if (familyEndIndex >= 0)
        {
            family = entry.substring(0, familyEndIndex);
            entry = entry.substring(familyEndIndex + 1);
        }
        int columnEndIndex = entry.indexOf('=');
        if (columnEndIndex >= 0)
        {
            column = entry.substring(0, columnEndIndex);
            value = entry.substring(columnEndIndex + 1);
        }
        else
        {
            column = entry;
        }
    }

    public String getFamily()
    {
        return family;
    }

    public String getColumn()
    {
        return column;
    }

    public String getValue()
    {
        return value;
    }

    public boolean isValid()
    {
        boolean result = true;
        if(family == null)
        {
            result = false;
            log.debug("Invalid entry, no family");
        }
        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Family=").append(getFamily()).append(",");
        buffer.append("Column=").append(getColumn()).append(",");
        buffer.append("Value=").append(getValue());
        return buffer.toString();
    }
}
