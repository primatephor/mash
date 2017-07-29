# Introduction #

The `DBRunHarness` really just runs a "select `*` from table where column\_name=column\_value". 
When combined with a standard verify harness, you can turn the table to a name / value pair and verify a row.


# Configuration #
Configuration parameters:
  * `url` = url of the database
  * `user` = user to log into the db
  * `password` = user's password
  * `driver` = jdbc driver class

# Parameter #
  * `table_name` = name of the table to run a select on
  * `column_name` = column to perform `where` clause on
  * `column_value` = value of the `where` clause

# Sample Usage #
```
    <Setup type="org.mash.harness.db.DBRunHarness">
        <Configuration name="url" property="jdbc.url"/>
        <Configuration name="user" property="jdbc.user"/>
        <Configuration name="password" property="jdbc.pass"/>
        <Configuration name="driver" property="jdbc.driver"/>
        <Parameter name="table_name">
            <Value>my_table</Value>
        </Parameter>
        <Parameter name="column_name">
            <Value>last_name</Value>
        </Parameter>
        <Parameter name="column_value">
            <Value>'squarepants'</Value>
        </Parameter>
    </Setup>

    <Verify type="org.mash.harness.StandardVerifyHarness" name="verify_data">
        <Parameter name="last_name"><Value>squarepants</Value></Parameter>
        <Parameter name="first_name"><Value>bob</Value></Parameter>
    </Verify>
```

Here the select run is: `select * from my_table where last_name = 'squarepants'`.
Notice we needed to include the ' character for the select statement.
The verify just takes the mapped results and does a name/value check.