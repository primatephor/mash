# Introduction #

The `DBWaitHarness` runs any sql against a database, and if there are no results will continue to do so until the wait 
either times out or the results are found. 
Polling times are defined in the configuration.


# Configuration #
Configuration parameters:
  * `url` = url of the database
  * `user` = user to log into the db
  * `password` = user's password
  * `driver` = jdbc driver class
  * `polltime` = time in milliseconds between running sql (default every 5 seconds)
  * `timeout` = time in milliseconds when polling should stop (default 1 minute)

# Parameter #
  * `sql` = the sql to run
  * `size` = number of rows expected (default 1)

# Sample Usage #
```
    <Setup type="org.mash.harness.db.sql.DBWaitHarness">
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

The wait harness runs the above select against the users table every 5 seconds (up to 1 minute) until a result is found. 
You may then verify the results in a normal way.