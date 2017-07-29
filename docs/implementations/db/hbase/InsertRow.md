# Introduction #

Given the supplied configuration resources, add a row to the specified table.

# Configuration #
Configuration parameters:
  * 'site\_config' = path to file of site config(s)
  * 'table' = name of the table to insert into

# Parameter #
  * 'key' = unique id of the row
  * 'entry' =  data to enter, with the format 'family:qualifier=value'

# Sample Usage #    <Setup type="org.mash.harness.db.hbase.InsertRow" name="add data 1">
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
        <Parameter name="entry">
            <Value>address:firstname=bob</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:lastname=squarepants</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:street1=123 main</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:city=San Francisco</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:state=CA</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>address:zip=12345</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>i_zip:12345</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>auth:user=bobs</Value>
        </Parameter>
        <Parameter name="entry">
            <Value>client:android</Value>
        </Parameter>
    </Setup>}}}

This will insert a row into the table 'dataset' with key 01, and each entry above is a column family/qualifier/value combination.```