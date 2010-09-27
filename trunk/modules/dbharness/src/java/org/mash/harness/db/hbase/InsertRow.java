package org.mash.harness.db.hbase;

import org.mash.harness.SetupHarness;
import org.mash.loader.HarnessParameter;
import org.mash.loader.HarnessConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 * @author: teastlack
 * @since: Sep 26, 2010
 */
public class InsertRow extends HBaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(InsertRow.class.getName());
    private String key;
    private String family = "";
    private String column = "";
    private String value = "";

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
                        byte[] family = Bytes.toBytes(this.family);
                        byte[] column = Bytes.toBytes(this.column);
                        put.add(family, column, Bytes.toBytes(value));
                        log.debug("Inserting " + key + ", " + this.family +
                                  ":" + this.column + ", " + value);
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
        catch (Exception e)
        {
            addError("Problem inserting row into table " + getTableName(), e);
        }
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

    @HarnessParameter(name = "family")
    public void setFamily(String family)
    {
        this.family = family;
    }

    @HarnessParameter(name = "column")
    public void setColumn(String column)
    {
        this.column = column;
    }

    @HarnessParameter(name = "value")
    public void setValue(String value)
    {
        this.value = value;
    }
}
