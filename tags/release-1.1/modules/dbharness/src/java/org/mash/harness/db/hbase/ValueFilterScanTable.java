package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.mash.loader.HarnessParameter;

/**
 * Scan an HBase table.
 * <p/>
 * Necessary configurations are
 * <ul>
 * <li>site_config (relative path to config file(s), like hbase-site.xml).  Multiple site configs
 * are allowed, just add more than one site config and all will be added as a resource</li>
 * <li>table (name of table to insert into)</li>
 * </ul>
 * <p/>
 * <p/>
 * Parameters are one of:
 * <ul>
 * <li>filter (column entry).  Format 'family:qualifier=value'</li>
 * <li>compare_operation (how to compare the filter). Valid values:</li>
 * <ul>
 * <li>LESS (all values less than specified value).  Default</li>
 * <li>LESS_OR_EQUAL (all values less or equal to specified value)</li>
 * <li>EQUAL (all values equal to specified value)</li>
 * <li>NOT_EQUAL (all values not equal to specified value)</li>
 * <li>GREATER_OR_EQUAL (all values greater or equal to specified value)</li>
 * <li>GREATER (all values greater than specified value)</li>
 * </ul>
 * <li>column (column and qualifier to retrieve).  Specified columns that match filter</li>
 * </ul>
 * <p/>
 * Sample:
 * <pre>
 * <Parameter name="filter">
 *      <Value>data:zip=40000</Value>
 * </Parameter>
 * <Parameter name="compare_operation">
 *      <Value>LESS</Value>
 * </Parameter>
 * <Parameter name="column">
 *      <Value>data</Value>
 * </Parameter>
 * </pre>
 * Here every family of 'data' with a 'data:zip' less than 40000 will be returned
 *
 * @author: teastlack
 * @since: Dec 13, 2010
 */
public class ValueFilterScanTable extends ScanTable
{
    private static final Logger log = Logger.getLogger(ValueFilterScanTable.class.getName());
    private String filter;
    private String compareOp;

    private enum CompareOpMap
    {
        LESS(CompareFilter.CompareOp.LESS),
        LESS_OR_EQUAL(CompareFilter.CompareOp.LESS_OR_EQUAL),
        EQUAL(CompareFilter.CompareOp.EQUAL),
        NOT_EQUAL(CompareFilter.CompareOp.NOT_EQUAL),
        GREATER_OR_EQUAL(CompareFilter.CompareOp.GREATER_OR_EQUAL),
        GREATER(CompareFilter.CompareOp.GREATER);

        private CompareFilter.CompareOp op;

        private CompareOpMap(CompareFilter.CompareOp op)
        {
            this.op = op;
        }

        public CompareFilter.CompareOp getOp()
        {
            return op;
        }
    }

    protected Scan createScan()
    {
        Scan result = super.createScan();
        if (filter != null)
        {
            CompareOpMap compareOpMap = CompareOpMap.EQUAL;
            if (compareOp != null)
            {
                compareOpMap = CompareOpMap.valueOf(compareOp);
            }

            if (compareOpMap == null)
            {
                addError("Invalid comparison operation " + compareOp,
                         "Must supply an appropriate comparison operation");
            }
            else
            {
                Entry entry = new Entry(filter);
                SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(entry.getFamily()),
                                                                             Bytes.toBytes(entry.getColumn()),
                                                                             compareOpMap.getOp(),
                                                                             Bytes.toBytes(entry.getValue()));
                result.setFilter(filter);
            }

        }
        else
        {
            log.info("Not filtering results");
        }
        return result;
    }

    @HarnessParameter(name = "filter")
    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    @HarnessParameter(name = "compare_operation")
    public void setCompareOp(String compareOp)
    {
        this.compareOp = compareOp;
    }
}
