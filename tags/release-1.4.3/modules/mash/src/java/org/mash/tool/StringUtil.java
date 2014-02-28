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
        StringBuilder result = new StringBuilder();
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

    public static String leftPad(String toPad,
                          int length,
                          char character)
    {
        StringBuilder result = new StringBuilder(padString(toPad, length, character));
        toPad = cleanNull(toPad);
        return result.append(toPad).toString();
    }

    public static String rightPad(String toPad,
                           int length,
                           char character)
    {
        StringBuilder result = new StringBuilder(cleanNull(toPad));
        return result.append(padString(toPad, length, character)).toString();
    }

    private static String padString(String toPad,
                            int length,
                            char character)
    {
        toPad = cleanNull(toPad);
        StringBuilder result = new StringBuilder();
        while ((result.length() + toPad.length()) < length)
        {
            result.append(character);
        }
        return result.toString();
    }

    public static String nullSafeTrim(String str)
    {
        return str == null ? null : str.trim();
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.trim().isEmpty();
    }

    public static String truncate(String str,
                           int length)
    {
        if (null != str && str.length() > length)
        {
            str = str.substring(0, length);
        }
        return str;
    }

}
