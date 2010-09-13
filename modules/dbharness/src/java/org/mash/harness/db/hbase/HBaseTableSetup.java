package org.mash.harness.db.hbase;

import org.mash.harness.SetupHarness;
import org.mash.harness.BaseHarness;
import org.mash.loader.HarnessParameter;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * This setup will deal with an Hadoop table.
 * <p/>
 * Parameters:<p/>
 * Table: name of the table to setup
 *
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public abstract class HBaseTableSetup extends BaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(HBaseTableSetup.class.getName());
    private String table;

    public void setup() throws Exception
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
                HBaseAdmin admin = new HBaseAdmin(config);
                HTable hTable = new HTable(config, table);
                setup(admin, hTable);
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

    protected abstract void setup(HBaseAdmin admin, HTable table) throws Exception;

    @HarnessParameter(name = "table")
    public void setTable(String table)
    {
        this.table = table;
    }
}
