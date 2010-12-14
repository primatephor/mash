package org.mash.tool;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Some simple utilities
 *
 * @author teastlack
 * @since Sep 9, 2010 10:26:21 AM
 */
public class StringUtil
{
    public static String cleanNull(String value)
    {
        String result = "";
        if (value != null)
        {
            result = value;
        }
        return result;
    }

    public static String toString(Iterator<String> iter)
    {
        StringBuffer result = new StringBuffer();
        if (iter != null)
        {
            while (iter.hasNext())
            {
                result.append(iter.next());
                if (iter.hasNext())
                {
                    result.append(", ");
                }
            }
        }
        return result.toString();
    }

    public static String toString(Collection<String> array)
    {
        String result = null;
        if (array != null)
        {
            Iterator<String> iter = array.iterator();
            result = toString(iter);
        }
        return result;
    }

    public static String toString(String... array)
    {
        return toString(Arrays.asList(array));
    }
}
