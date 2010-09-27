package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessParameter;
import org.mash.harness.BaseHarness;

import java.io.IOException;

/**
 * @author: teastlack
 * @since: Sep 26, 2010
 */
public class HBaseHarness extends BaseHarness
{
    private static final Logger log = Logger.getLogger(HBaseHarness.class.getName());
    private String tableName;
    private HBaseAdmin admin;
    private HTable table;

    public void setup()
    {
        if (table == null)
        {
            addError("'table' parameter not set", "You must specify a table to setup");
        }

        if (getErrors().size() == 0)
        {
            log.info("Running setup on " + table);
            HBaseConfiguration config = new HBaseConfiguration();
            try
            {
                admin = new HBaseAdmin(config);
                table = new HTable(config, tableName);
            }
            catch (MasterNotRunningException e)
            {
                addError("Unable to connect admin tool to HBase, master not running", e);
            }
            catch (IOException e)
            {
                addError("Problem connecting to table", e);
            }
        }
    }

    public String getTableName()
    {
        return tableName;
    }

    public HBaseAdmin getAdmin()
    {
        if (admin == null && !hasErrors())
        {
            setup();
        }
        return admin;
    }

    public HTable getTable()
    {
        if (table == null && !hasErrors())
        {
            setup();
        }
        return table;
    }

    @HarnessParameter(name = "table")
    public void setTable(String table)
    {
        this.tableName = table;
    }
}
