package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
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
        try
        {
            if (!hasErrors())
            {
                if (admin.tableExists(getTableName()))
                {
                    log.info("Table " + getTableName() + "already exists");
                    if (!admin.isTableEnabled(getTableName()))
                    {
                        log.info("Enabling table " + getTableName());
                        admin.enableTable(getTableName());
                    }
                }
                else
                {
                    HTableDescriptor desc = new HTableDescriptor(getTableName());
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

    @HarnessParameter(name = "family")
    public void addFamily(String family)
    {
        families.add(family);
    }
}