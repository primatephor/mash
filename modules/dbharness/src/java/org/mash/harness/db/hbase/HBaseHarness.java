package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mash.loader.HarnessParameter;
import org.mash.loader.HarnessConfiguration;
import org.mash.harness.BaseHarness;
import org.mash.file.FileLoader;
import org.mash.file.FileReaderException;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Common configurations and admin access for HBase is supplied with this entity.  Site comfigs are
 * supplied as a list, and each one added as a resource to the configuration.
 *
 * Extending classes then use this data to retrieve the relevant HBaseAdmin, allowing specific work
 * to take over.
 *
 * @author:
 * @since: Sep 26, 2010
 */
public class HBaseHarness extends BaseHarness
{
    private static final Logger log = LogManager.getLogger(HBaseHarness.class.getName());
    private String tableName;
    private List<String> siteConfigs;

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
            FileLoader loader = new FileLoader();
            try
            {
                for (String siteConfig : siteConfigs)
                {
                    File siteFileConfig = loader.findFile(siteConfig, getDefinition().getScriptDefinition().getPath());
                    log.info("Adding site config " + siteFileConfig.getAbsolutePath());
                    config.addResource(siteFileConfig.getAbsolutePath());
                }
            }
            catch (FileReaderException e)
            {
                addError("Unable to find site file " + siteConfigs, e);
            }
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

    public HTable getTable() throws IOException
    {
        if (table == null && !hasErrors())
        {
            if (tableName == null)
            {
                addError("'table' parameter not set", "You must specify a table to setup");
            }
            if (siteConfigs == null)
            {
                addError("'site_config' configuration not set", "You must specify a site config file (hbase-site.xml) to setup");
            }

            if (!hasErrors())
            {
                log.info("Running setup on " + tableName);
                table = new HTable(getConfig(), tableName);
            }
        }
        return table;
    }

    @HarnessParameter(name = "table")
    public void setTable(String table)
    {
        this.tableName = table;
    }

    @HarnessConfiguration(name = "site_config")
    public void setSiteConfigs(String siteConfigs)
    {
        if (this.siteConfigs == null)
        {
            this.siteConfigs = new ArrayList<String>();
        }
        this.siteConfigs.add(siteConfigs);
    }
}
