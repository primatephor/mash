package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.log4j.Logger;
import org.mash.harness.SetupHarness;

import java.io.IOException;

/**
 * Delete an HBase table.  Tabe is disabled first, then deleted.
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>site_config (relative path to config file(s), like hbase-site.xml).  Multiple site configs
 *     are allowed, just add more than one site config and all will be added as a resource</li>
 * </ul>
 * <p/>
 *
 * Parameters are:
 * <ul>
 * <li>table (name of table to delete)</li>
 * </ul>
 *
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public class DeleteTable extends HBaseHarness implements SetupHarness
{
    private static final Logger log = Logger.getLogger(DeleteTable.class.getName());

    public void setup()
    {

        HBaseAdmin admin = getAdmin();
        if (admin != null)
        {
            try
            {
                if (!hasErrors())
                {
                    if (admin.tableExists(getTableName()))
                    {
                        if (admin.isTableEnabled(getTableName()))
                        {
                            log.info("Disabling table");
                            admin.disableTable(getTableName());
                        }
                        log.info("Deleting table");
                        admin.deleteTable(getTableName());
                    }
                    else
                    {
                        log.info("No table named " + getTableName() + " exists");
                    }
                }
            }
            catch (TableNotFoundException e)
            {
                log.info("Table already deleted");
            }
            catch (IOException e)
            {
                addError("Problem deleting table " + getTableName(), e);
            }
        }
    }
}