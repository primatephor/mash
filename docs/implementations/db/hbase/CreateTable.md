# Introduction #

Given the supplied configuration resources, create the table of the specified name with the given families.

# Configuration #
Configuration parameters:
  * 'site\_config' = path to file of site config(s)

# Parameter #
  * 'table' = name of the table to create
  * 'family' = family to add to the table

# Sample Usage #
```
    <Setup type="org.mash.harness.db.hbase.CreateTable" name="build dataset">
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
        <Parameter name="family">
            <Value>key</Value>
        </Parameter>
        <Parameter name="family">
            <Value>address</Value>
        </Parameter>
        <Parameter name="family">
            <Value>header</Value>
        </Parameter>
        <Parameter name="family">
            <Value>auth</Value>
        </Parameter>
        <Parameter name="family">
            <Value>client</Value>
        </Parameter>
    </Setup>
```

This will create an HBase table named 'dataset' with the families: key, address, header, auth, and client.