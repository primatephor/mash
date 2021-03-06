# Introduction #

The `SQLRunHarness` runs any sql against a database, and if there are results will supply them for verification (column 
names are the parameters).


# Configuration #
Configuration parameters:
  * `url` = url of the database
  * `user` = user to log into the db
  * `password` = user's password
  * `driver` = jdbc driver class

# Parameter #
  * `sql` = the sql to run

# Sample Usage #
```
    <Setup type="org.mash.harness.db.sql.SQLRunHarness">
        <Configuration name="url" property="jdbc.url"/>
        <Configuration name="user" property="jdbc.user"/>
        <Configuration name="password" property="jdbc.pass"/>
        <Configuration name="driver" property="jdbc.driver"/>
        <Parameter name="sql">
            <Value>select * from users where first_name='bob'</Value>
        </Parameter>
    </Setup>

    <Verify type="org.mash.harness.StandardVerifyHarness" name="verify_data">
        <Parameter name="last_name"><Value>squarepants</Value></Parameter>
        <Parameter name="first_name"><Value>bob</Value></Parameter>
    </Verify>
```

Here the select run is: `select * from my_table where first_name = 'bob'`. 
The verify just takes the mapped results and does a name/value check. 
You can supply any column name in the verify to check the results.

Any sql should be valid. 
Use the [ListVerifyHarness](../../ListVerifyHarness.md) to validate when multiple results are found.