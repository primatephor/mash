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
    HBaseConfiguration config;

    public String getTableName()
    {
        return tableName;
    }

    public HBaseConfiguration getConfig()
    {
        if (config == null)
        {
            config = new HBaseConfiguration();
        }
        return config;
    }

    public HBaseAdmin getAdmin()
    {
        if (admin == null && !hasErrors())
        {
            try
            {
                admin = new HBaseAdmin(getConfig());
            }
            catch (MasterNotRunningException e)
            {
                addError("Unable to connect admin tool to HBase, master not running", e);
            }
        }
        return admin;
    }

    public HTable getTable()
    {
        if (table == null && !hasErrors())
        {
            if (tableName == null)
            {
                addError("'table' parameter not set", "You must specify a table to setup");
            }

            if (!hasErrors())
            {
                log.info("Running setup on " + table);
                try
                {
                    table = new HTable(getConfig(), tableName);
                }
                catch (IOException e)
                {
                    addError("Problem creating table connection " + tableName, e);
                }
            }
        }
        return table;
    }

    @HarnessParameter(name = "table")
    public void setTable(String table)
    {
        this.tableName = table;
    }
}
