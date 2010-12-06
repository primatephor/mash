package org.mash.harness.db.hbase;

import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;
import org.mash.loader.HarnessConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author: teastlack
 * @since: Sep 26, 2010
 */
public class InsertRow extends HBaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(InsertRow.class.getName());
    private String key;
    private List<Entry> entries;

    public void setup()
    {
        HBaseAdmin admin = getAdmin();
        try
        {
            if (!hasErrors())
            {
                if (admin.tableExists(getTableName()))
                {
                    HTable table = getTable();
                    if (key != null)
                    {
                        Put put = new Put(Bytes.toBytes(key));
                        for (Entry entry : entries)
                        {
                            byte[] family = Bytes.toBytes(entry.getFamily());
                            byte[] column = Bytes.toBytes(entry.getColumn());

                            log.debug("Inserting " + key + ", " + entry);
                            byte[] value = new byte[]{};
                            if (entry.getValue() != null)
                            {
                                value = Bytes.toBytes(entry.getValue());
                            }
                            put.add(family, column, value);
                        }
                        table.put(put);
                    }
                    else
                    {
                        addError("Null Parameter", "Need 'key' to insert row");
                    }
                }
                else
                {
                    addError("Insert error", getTableName() + " does not exist");
                }
            }
        }
        catch (IOException e)
        {
            addError("Problem inserting row into table " + getTableName(), e);
        }
    }

    public List<Entry> getEntries()
    {
        if (entries == null)
        {
            entries = new ArrayList<Entry>();
        }
        return entries;
    }

    @HarnessConfiguration(name = "table")
    public void setTable(String table)
    {
        super.setTable(table);
    }

    @HarnessParameter(name = "key")
    public void setKey(String key)
    {
        this.key = key;
    }

    @HarnessParameter(name = "entry")
    public void setEntries(String entry)
    {
        getEntries().add(new Entry(entry));
    }
}
