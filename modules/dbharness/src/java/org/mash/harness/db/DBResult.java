package org.mash.harness.db;

import org.apache.log4j.Logger;
import org.mash.harness.ListRunResponse;
import org.mash.tool.StringUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DB results analyze the result set of the db invocation.  This only works with supplied column names.
 *
 * @author
 * @since Jan 8, 2010 3:24:50 PM
 *
 */
public class DBResult implements ListRunResponse
{
    private static final Logger log = Logger.getLogger(DBResult.class.getName());
    private List<ResultSetData> resultSetData;
    private int rowNumber = 0;

    public DBResult(ResultSet results) throws Exception
    {
        int count = 0;
        log.debug("Retrieving row:" + count);
        getResultSetData().add(addRow(results));
        while (results.next())
        {
            log.debug("Retrieving row:" + count);
            count++;
            getResultSetData().add(addRow(results));
        }
    }

    private ResultSetData addRow(ResultSet results)
    {
        ResultSetData result = null;
        try
        {
            result = new ResultSetData();
            int size = results.getMetaData().getColumnCount();
            for (int i = 1; i <= size; i++)
            {
                String name = "";
                try
                {
                    name = results.getMetaData().getColumnName(i);
                    Object value = results.getObject(i);
                    //this exists because I tested this on a flakey db driver, sometimes worked...
                    if (value == null)
                    {
                        log.debug("No value, checking stream");
                        InputStream input = results.getAsciiStream(i);
                        if (input != null)
                        {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                            value = reader.readLine();
                            input.close();
                        }
                    }
                    if (value != null)
                    {
                        log.trace("Adding " + name + " to result as " + value);
                        result.put(name, value.toString());
                    }
                }
                catch (Exception e)
                {
                    log.error("Unexpected error adding " + name, e);
                }
            }
        }
        catch (SQLException e)
        {
            log.error("Unexpected error retrieving data", e);
        }
        return result;
    }

    public void setElementNumber(int elementNumber)
    {
        this.rowNumber = elementNumber;
    }

    public int getSize()
    {
        return this.getResultSetData().size();
    }

    public Map<String, String> getResultSet(int row)
    {
        return getResultSetData().get(row).getData();
    }

    public List<ResultSetData> getResultSetData()
    {
        if (resultSetData == null)
        {
            resultSetData = new ArrayList<ResultSetData>();
        }
        return resultSetData;
    }

    public String getValue(String name)
    {
        return getResultSetData().get(rowNumber).getData().get(name);
    }

    public Collection<String> getValues(String name)
    {
        return Arrays.asList(getValue(name));
    }

    public Collection<String> getValues()
    {
        return getResultSetData().get(rowNumber).getData().values();
    }

    public String getString()
    {
        Iterator<String> iter = getValues().iterator();
        return StringUtil.toString(iter);
    }

    public int getRowNumber()
    {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    private class ResultSetData
    {
        private Map<String, String> resultSetData;

        public Map<String, String> getData()
        {
            if (resultSetData == null)
            {
                resultSetData = new HashMap<String, String>();
            }
            return resultSetData;
        }

        public void put(String name, String s)
        {
            getData().put(name, s);
        }
    }

    @Override
    public String toString()
    {
        return getString();
    }
}
