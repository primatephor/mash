package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;
import org.mash.harness.SetupHarness;

import java.io.IOException;

/**
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public class DeleteTable extends HBaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(DeleteTable.class.getName());

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
                    if (!admin.isTableEnabled(table.getTableName()))
                    {
                        log.info("Disabling table");
                        admin.disableTable(table.getTableName());
                    }
                    log.info("Deleting table");
                    admin.deleteTable(table.getTableName());
                }
                else
                {
                    log.info("No table named " + new String(table.getTableName()) + " exists");
                }
            }
        }
        catch (IOException e)
        {
            addError("Problem deleting table " + getTableName(), e);
        }

    }
}
