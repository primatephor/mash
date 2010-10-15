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
        HTable table = getTable();
        try
        {
            if (!hasErrors())
            {
                if (admin.tableExists(table.getTableName()))
                {
                    if (key != null)
                    {
                        Put put = new Put(Bytes.toBytes(key));
                        for (Entry entry : entries)
                        {
                            byte[] family = Bytes.toBytes(entry.getFamily());
                            byte[] column = Bytes.toBytes(entry.getColumn());
                            put.add(family, column, Bytes.toBytes(entry.getValue()));
                            log.debug("Inserting " + key + ", " + entry);
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

    public class Entry
    {
        private String family;
        private String column;
        private String value;

        private Entry(String entry)
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

        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Family=").append(getFamily()).append(",");
            buffer.append("Column=").append(getColumn()).append(",");
            buffer.append("Value=").append(getValue());
            return buffer.toString();
        }
    }
}
