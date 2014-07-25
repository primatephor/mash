package org.mash.harness.db;

import junit.framework.TestCase;
import org.mash.harness.db.hbase.InsertRow;

/**
 * @author: teastlack
 * @since: Sep 30, 2010
 */
public class TestHBaseInsert extends TestCase
{
    public void testEntries()
    {
        InsertRow row = new InsertRow();
        row.setEntries("fam:col=val");
        row.setEntries("fam:col");
        row.setEntries("col=val");

        assertEquals(3, row.getEntries().size());
        assertEquals("fam", row.getEntries().get(0).getFamily());
        assertEquals("col", row.getEntries().get(0).getColumn());
        assertEquals("val", row.getEntries().get(0).getValue());

        assertEquals("fam", row.getEntries().get(1).getFamily());
        assertEquals("col", row.getEntries().get(1).getColumn());

        assertEquals("col", row.getEntries().get(2).getColumn());
        assertEquals("val", row.getEntries().get(2).getValue());
    }
}
