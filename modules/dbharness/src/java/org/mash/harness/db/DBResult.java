package org.mash.harness.db;

import org.mash.harness.RunResponse;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB results analyze the result set of the db invocation.  This only works with supplied column names.
 *
 * @author teastlack
 * @since Jan 8, 2010 3:24:50 PM
 *
 */
public class DBResult implements RunResponse
{
    private static final Logger log = Logger.getLogger(DBResult.class.getName());
    private Map<String, String> resultSetData;

    public DBResult(ResultSet results)
    {
        resultSetData = new HashMap<String, String>();
        try
        {
            int size = results.getMetaData().getColumnCount();
            for (int i = 1; i <= size; i++)
            {
                String name = "";
                try
                {
                    name = results.getMetaData().getColumnName(i);
                    Object value = results.getObject(i);
                    if (value != null)
                    {
                        log.debug("Adding " + name + " to result as " + value);
                        resultSetData.put(name, value.toString());
                    }
                }
                catch (SQLException e)
                {
                    log.error("Unexpected error adding " + name, e);
                }
            }
        }
        catch (SQLException e)
        {
            log.error("Unexpected error retrieving data", e);
        }
    }

    public Map<String, String> getResultSetData()
    {
        return resultSetData;
    }

    public String getValue(String name)
    {
        return resultSetData.get(name);
    }

    public Collection<String> getValues(String name)
    {
        return Arrays.asList(getValue(name));
    }

    public Collection<String> getValues()
    {
        return resultSetData.values();
    }

    public String getString()
    {
        StringBuffer response = new StringBuffer();
        Iterator<String> iter = getValues().iterator();
        while (iter.hasNext())
        {
            response.append(iter.next());
            if (iter.hasNext())
            {
                response.append(", ");
            }
        }
        return response.toString();
    }
}
