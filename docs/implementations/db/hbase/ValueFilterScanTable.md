# Introduction #

Scan a table, but apply a filter to bound the results

# Configuration #
Configuration parameters:
  * `site_config` = path to file of site config(s)
  * `table` = name of the table to insert into

# Parameter #
  * `filter` = the column to filter, with the format `family:qualifier=value`
  * `compare_operation` = operation to compare value in filter with.  For instance, if value in `filter` is 40000, 
  and `compare_operation` is `LESS`, then every value less than 40000 will be retrieved.  Valid values:
    * LESS
    * LESS\_OR\_EQUAL
    * EQUAL
    * NOT\_EQUAL
    * GREATER\_OR\_EQUAL
    * GREATER
  * `column` =  column to retrieve, with the format `family:qualifier`

# Sample Usage #
```
    <!--
    Show getting all data columns, but specifically from the matching zip
    -->
    <Run type="org.mash.harness.db.hbase.ValueFilterScanTable" name="get filtered">
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
        <Parameter name="column">
            <Value>address</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.ListVerifyHarness" name="verify index">
        <Configuration name="expected_size"><Value>1</Value></Configuration>
        <Configuration name="element_number"><Value>0</Value></Configuration>
        <Parameter name="address:state"><Value>CA</Value></Parameter>
        <Parameter name="address:zip"><Value>12345</Value></Parameter>
        <Parameter name="address:firstname"><Value>bob</Value></Parameter>
    </Verify>

    <!--
    Show filtering with comparison operations
    -->
    <Run type="org.mash.harness.db.hbase.ValueFilterScanTable" name="get filtered with less than operation">
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
            <Value>address:zip=40000</Value>
        </Parameter>
        <Parameter name="compare_operation">
            <Value>LESS</Value>
        </Parameter>
        <Parameter name="column">
            <Value>address</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.ListVerifyHarness" name="verify index">
        <Configuration name="expected_size"><Value>1</Value></Configuration>
        <Configuration name="element_number"><Value>0</Value></Configuration>
        <Parameter name="address:state"><Value>CA</Value></Parameter>
        <Parameter name="address:zip"><Value>12345</Value></Parameter>
        <Parameter name="address:firstname"><Value>bob</Value></Parameter>
    </Verify>
```

The first filter scans the table, and only retrieves values where `address:zip=12345`. 
The second filter gets all where zip is less than `40000`.