package org.mash.harness.db.hbase;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

/**
 * @author: teastlack
 * @since: Sep 11, 2010
 */
public interface HBaseProcessor
{
    /**
     * Perform functionality on the HBase depending on the processor in question.
     *
     * @param admin for hbase
     * @param table to work on
     * @throws Exception
     */
    void process(HBaseAdmin admin, HTable table) throws Exception;
}
