package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;

/**
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public class DeleteTable extends HBaseTableSetup
{
    private static final Logger log = Logger.getLogger(DeleteTable.class.getName());

    protected void setup(HBaseAdmin admin, HTable table) throws Exception
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
