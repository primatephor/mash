# Introduction #

Given the supplied configuration resources, delete the table of the specified name

# Configuration #
Configuration parameters:
  * 'site\_config' = path to file of site config(s)

# Parameter #
  * 'table' = name of the table to delete

# Sample Usage #
```
    <Setup type="org.mash.harness.db.hbase.DeleteTable" name="clean hbase">
        <Configuration name="site_config">
            <Value>hbase/hbase-site.xml</Value>
        </Configuration>
        <Configuration name="site_config">
            <Value>hbase/core-site.xml</Value>
        </Configuration>
        <Configuration name="site_config">
            <Value>hbase/hdfs-site.xml</Value>
        </Configuration>
        <Parameter name="table">
            <Value>dataset</Value>
        </Parameter>
    </Setup>
```

If this table is enabled, it's disabled and then deleted.