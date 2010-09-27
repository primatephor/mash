package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessParameter;
import org.mash.harness.SetupHarness;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public class CreateTable extends HBaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(CreateTable.class.getName());
    private List<String> families;

    public CreateTable()
    {
        families = new ArrayList<String>();
    }

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
                    log.info("Table " + new String(table.getTableName()) + "already exists");
                    if (!admin.isTableEnabled(table.getTableName()))
                    {
                        log.info("Enabling table " + new String(table.getTableName()));
                        admin.enableTable(table.getTableName());
                    }
                }
                else
                {
                    HTableDescriptor desc = new HTableDescriptor(table.getTableName());
                    for (String family : families)
                    {
                        log.info("Adding family " + family);
                        HColumnDescriptor familyDescriptor = new HColumnDescriptor(family);
                        desc.addFamily(new HColumnDescriptor(familyDescriptor));
                    }
                    admin.createTable(desc);
                }
            }
        }
        catch (IOException e)
        {
            addError("Problem creating table " + getTableName(), e);
        }
    }

    @HarnessParameter(name = "table")
    public void setTable(String table)
    {
        super.setTable(table);
    }


    @HarnessParameter(name = "family")
    public void addFamily(String family)
    {
        families.add(family);
    }
}