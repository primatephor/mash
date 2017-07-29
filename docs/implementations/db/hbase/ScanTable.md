# Introduction #

Scan a table to retrieve all columns / qualifiers

# Configuration #
Configuration parameters:
  * `site_config` = path to file of site config(s)
  * `table` = name of the table to insert into

# Parameter #
  * `key` = unique id of the row
  * `column` =  column to retrieve, with the format `family:qualifier`

# Sample Usage #
```
    <!--
    get all columns by rowid
    -->
    <Run type="org.mash.harness.db.hbase.ScanTable" name="get by key">
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
        <Parameter name="key">
            <Value>001</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.ListVerifyHarness" name="get by key">
        <Configuration name="expected_size"><Value>1</Value></Configuration>
        <Configuration name="element_number"><Value>0</Value></Configuration>
        <Parameter name="address:firstname"><Value>bob</Value></Parameter>
    </Verify>


    <!--
    get all address columns
    -->
    <Run type="org.mash.harness.db.hbase.ScanTable" name="get multiple">
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
        <Parameter name="column">
            <Value>address</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.ListVerifyHarness" name="verify first">
        <Configuration name="expected_size"><Value>2</Value></Configuration>
        <Configuration name="element_number"><Value>0</Value></Configuration>
        <Parameter name="address:firstname"><Value>bob</Value></Parameter>
    </Verify>
    <Verify type="org.mash.harness.ListVerifyHarness" name="verify second">
        <Configuration name="expected_size"><Value>2</Value></Configuration>
        <Configuration name="element_number"><Value>1</Value></Configuration>
        <Parameter name="address:firstname"><Value>patrick</Value></Parameter>
    </Verify>
```

In the first scan, retrieval of a specific row is retrieved. 
Verification shows a single result of multiple entries. 
The second scan retrieves all `address` family columns, getting both rows inserted in this particular table.