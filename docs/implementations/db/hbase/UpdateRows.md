# Introduction #

Extends the ValueFilterScanTable to find a row, then updates it with the supplied entry

# Configuration #
Configuration parameters:
  * 'site\_config' = path to file of site config(s)
  * 'table' = name of the table to insert into

# Parameter #
  * 'filter' = the column to filter, with the format 'family:qualifier=value'
  * 'compare\_operation' = operation to compare value in filter with.  For instance, if value in 'filter' is 40000, and 'compare\_operation' is 'LESS', then every value less than 40000 will be retrieved.  Valid values:
    * LESS
    * LESS\_OR\_EQUAL
    * EQUAL
    * NOT\_EQUAL
    * GREATER\_OR\_EQUAL
    * GREATER
    * 'entry' = family:qualifier=value format

# Sample Usage #
```
    <!--
    Show getting all data columns, but specifically from the matching zip
    -->
    <Run type="hbase_update" name="update filtered">
        <Configuration name="site_config">
            <Value>hbase/hbase-site.xml</Value>
        </Configuration>
        <Configuration name="site_config">
            <Value>hbase/core-site.xml</Value>
        </Configuration>
        <Configuration name="site_config">
            <Value>hbase/hdfs-site.xml</Value>
        </Configuration>
        <Configuration name="table">
            <Value>dataset</Value>
        </Configuration>
        <Parameter name="filter">
            <Value>address:zip=12345</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:zip=54321</Value>
        </Parameter>
    </Run>
    <Verify type="list" name="verify index">
        <Configuration name="expected_size"><Value>1</Value></Configuration>
        <Configuration name="element_number"><Value>0</Value></Configuration>
        <Parameter name="address:state"><Value>CA</Value></Parameter>
        <Parameter name="address:zip"><Value>54321</Value></Parameter>
        <Parameter name="address:firstname"><Value>bob</Value></Parameter>
    </Verify>
```

The filter scans the table, and only retrieves values where address:zip=12345 and updates the zip to 54321