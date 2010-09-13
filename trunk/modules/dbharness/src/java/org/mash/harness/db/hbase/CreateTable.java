package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessParameter;

import java.util.List;
import java.util.ArrayList;

/**
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public class CreateTable extends HBaseTableSetup
{
    private static final Logger log = Logger.getLogger(CreateTable.class.getName());
    private List<String> families;

    protected void setup(HBaseAdmin admin, HTable table) throws Exception
    {
        if (admin.tableExists(table.getTableName()))
        {
            log.info("Table " + new String(table.getTableName()) + "already exists");
            if(!admin.isTableEnabled(table.getTableName()))
            {
                log.info("Enabling table "+ new String(table.getTableName()));
                admin.enableTable(table.getTableName());
            }
        }
        else
        {
            HTableDescriptor desc = new HTableDescriptor(table.getTableName());
            for (String family : families)
            {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
                desc.addFamily(new HColumnDescriptor(columnDescriptor));
            }
            admin.createTable(desc);
        }
    }

    @HarnessParameter(name = "family")
    public void addFamily(String family)
    {
        if(families == null)
        {
            families = new ArrayList<String>();
        }
        families.add(family);
    }
}