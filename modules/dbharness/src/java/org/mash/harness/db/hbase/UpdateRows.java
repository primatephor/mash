package org.mash.harness.db.hbase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.loader.HarnessName;
import org.mash.loader.HarnessParameter;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Scan an HBase table and apply the specified update to the value.
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>site_config (relative path to config file(s), like hbase-site.xml).  Multiple site configs
 * are allowed, just add more than one site config and all will be added as a resource</li>
 * <li>table (name of table to insert into)</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are one of:
 * <ul>
 * <li>filter (column entry).  Format 'family:qualifier=value'</li>
 * <li>compare_operation (how to compare the filter). Valid values:</li>
 * <ul>
 * <li>LESS (all values less than specified value).  Default</li>
 * <li>LESS_OR_EQUAL (all values less or equal to specified value)</li>
 * <li>EQUAL (all values equal to specified value)</li>
 * <li>NOT_EQUAL (all values not equal to specified value)</li>
 * <li>GREATER_OR_EQUAL (all values greater or equal to specified value)</li>
 * <li>GREATER (all values greater than specified value)</li>
 * </ul>
 * <li>column (column and qualifier to retrieve).  Specified columns that match filter</li>
 * </ul>
 * And the following:
 * <li>entry (column entry).  Format 'family:qualifier=value'</li>
 * </ul>
 * <p/>
 * Sample:
 * <pre>
 * <Parameter name="filter">
 *      <Value>data:zip=40000</Value>
 * </Parameter>
 * <Parameter name="compare_operation">
 *      <Value>LESS</Value>
 * </Parameter>
 * <Parameter name="column">
 *      <Value>data</Value>
 * </Parameter>
 * <Parameter name="entry">
 *      <Value>data:zip=40001</Value>
 * </Parameter>
 * </pre>
 * Here every family of 'data' with a 'data:zip' less than 40000 will be returned, and updated to
 * 40001
 *
 * @author:
 * @since: Dec 28, 2010
 */
@HarnessName(name = "hbase_update")
public class UpdateRows extends ValueFilterScanTable
{
    private static final Logger log = LogManager.getLogger(UpdateRows.class.getName());
    private String entryString;
    private Entry entry;

    protected void applyResult(Result scanResult)
    {
        if (entry == null || !entry.isValid())
        {
            addError("Invalid entry " + entryString,
                     "Must have a family for entry, format: family:qualifier=value");
        }
        else
        {
            super.applyResult(scanResult);
            Put put = new Put(scanResult.getRow());
            put.add(Bytes.toBytes(entry.getFamily()),
                    Bytes.toBytes(entry.getColumn()),
                    Bytes.toBytes(entry.getValue()));
            log.debug("Adding " + entry.toString());
            try
            {
                getTable().put(put);
            }
            catch (IOException e)
            {
                addError("Problem inserting row into table " + getTableName(), e);
            }
        }
    }

    @HarnessParameter(name = "entry")
    public void setEntry(String entry)
    {
        this.entry = new Entry(entry);
        this.entryString = entry;
    }
}
