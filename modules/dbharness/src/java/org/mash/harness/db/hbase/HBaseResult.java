package org.mash.harness.db.hbase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.harness.ListRunResponse;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.KeyValue;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Multiple rows returned from HBase are dealt with here, where the scan results are parsed one at a
 * time and values are compared to each one.
 * 
 * @author:
 * @since: Dec 5, 2010
 */
public class HBaseResult implements ListRunResponse
{
    private static final Logger log = LogManager.getLogger(HBaseResult.class.getName());
    private List<Result> scanResults;
    private Result currentResult;

    public HBaseResult(Result scanResult)
    {
        this(Arrays.asList(scanResult));
        currentResult = scanResult;
    }

    public HBaseResult(List<Result> scanResults)
    {
        this.scanResults = scanResults;
        this.currentResult = scanResults.get(0);
    }

    public void setElementNumber(int elementNumber)
    {
        currentResult = scanResults.get(elementNumber);
    }

    public int getSize()
    {
        return scanResults.size();
    }

    public String getValue(String name)
    {
        Entry entry = new Entry(name);
        byte[] value = currentResult.getValue(Bytes.toBytes(entry.getFamily()),
                                              Bytes.toBytes(entry.getColumn()));
        String result = null;
        if (value != null)
        {
            result = Bytes.toString(value);
        }
        log.debug("Retrieving key '" + name + "', found " + result);
        return result;
    }

    public Collection<String> getValues(String name)
    {
        Entry entry = new Entry(name);
        List<String> response = new ArrayList<String>();

        //should only be one
        if (entry.getColumn() != null)
        {
            response.add(getValue(name));
        }
        else
        {
            KeyValue[] values = currentResult.raw();
            for (KeyValue value : values)
            {
                String family = Bytes.toString(value.getFamily());
                if (name.equals(family))
                {
                    response.add(Bytes.toString(value.getValue()));
                }
            }
        }
        return response;
    }

    public Collection<String> getValues()
    {
        List<String> response = new ArrayList<String>();
        KeyValue[] values = currentResult.raw();
        for (KeyValue value : values)
        {
            response.add(Bytes.toString(value.getValue()));
        }
        return response;
    }

    public String getString()
    {
        return currentResult.toString();
    }
}
