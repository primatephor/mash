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

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * @author: teastlack
 * @since: Dec 5, 2010
 */
public class ScanTable extends HBaseHarness implements RunHarness
{
    //retrieve by key
    private String key;

    //or by family:qualifier
    private String family;
    private String qualifier;

    private RunResponse response;

    public void run(List<RunHarness> previous, List<SetupHarness> setups)
    {
        HBaseAdmin admin = getAdmin();
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
                    else if (family != null && qualifier != null)
                    {
                        Scan s = new Scan();
                        s.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                        ResultScanner scanner = table.getScanner(s);
                        try
                        {
                            List<Result> scannedResults = new ArrayList<Result>();
                            for (Result scanResult : scanner)
                            {
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

    @HarnessParameter(name = "family")
    public void setFamily(String family)
    {
        this.family = family;
    }

    @HarnessParameter(name = "qualifier")
    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }
}
