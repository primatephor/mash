package org.mash.harness.db.hbase;

import org.mash.harness.RunHarness;
import org.mash.harness.SetupHarness;
import org.mash.harness.RunResponse;
import org.mash.loader.HarnessConfiguration;
import org.mash.loader.HarnessParameter;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Scan an HBase table.
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>site_config (relative path to config file(s), like hbase-site.xml).  Multiple site configs
 *     are allowed, just add more than one site config and all will be added as a resource</li>
 * <li>table (name of table to insert into)</li>
 * </ul>
 * <p/>
 *
 * Parameters are one of:
 * <ul>
 * <li>key (this is the row key).  Basically a unique id.</li>
 * <li>column (family and/or qualifier to retrieve).  Format 'family:qualifier'</li>
 * </ul>
 *
 * @author: teastlack
 * @since: Dec 5, 2010
 */
public class ScanTable extends HBaseHarness implements RunHarness
{
    private static final Logger log = Logger.getLogger(ScanTable.class.getName());
    //retrieve by key
    private String key;

    //or by family:qualifier
    private List<String> columns;

    private RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        HBaseAdmin admin = getAdmin();
        ResultScanner scanner = null;
        try
        {
            if (!hasErrors())
            {
                if (admin.tableExists(getTableName()))
                {
                    HTable table = getTable();
                    if (key != null)
                    {
                        Get get = new Get(Bytes.toBytes(key));
                        Result result = table.get(get);
                        response = new HBaseResult(result);
                    }
                    else if (columns != null && columns.size() > 0)
                    {
                        Scan s = createScan();
                        for (String column : columns)
                        {
                            log.info("Searching for " + column);
                            s.addColumn(Bytes.toBytes(column));
                        }
                        scanner = table.getScanner(s);
                        try
                        {
                            List<Result> scannedResults = new ArrayList<Result>();
                            for (Result scanResult : scanner)
                            {
                                log.debug("Found row:" + scanResult);
                                scannedResults.add(scanResult);
                            }
                            response = new HBaseResult(scannedResults);
                        }
                        finally
                        {
                            scanner.close();
                        }
                    }
                    else
                    {
                        addError("Null Parameter", "Need 'key' or 'family' and 'qualifier' to scan db");
                    }
                }
                else
                {
                    addError("Insert error", getTableName() + " does not exist");
                }
            }
        }
        catch (IOException e)
        {
            addError("Problem inserting row into table " + getTableName(), e);
        }
        finally
        {
            if (scanner != null)
            {
                scanner.close();
            }
        }
    }

    protected Scan createScan()
    {
        return new Scan();
    }

    public RunResponse getResponse()
    {
        return response;
    }

    @HarnessConfiguration(name = "table")
    public void setTable(String table)
    {
        super.setTable(table);
    }

    @HarnessParameter(name = "key")
    public void setKey(String key)
    {
        this.key = key;
    }

    @HarnessParameter(name = "column")
    public void setFamily(String column)
    {
        if (this.columns == null)
        {
            this.columns = new ArrayList<String>();
        }
        this.columns.add(column);
    }
}
