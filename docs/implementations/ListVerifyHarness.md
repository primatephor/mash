# Introduction #

Just like the StandardVerifyHarness, This verifies the  simple parameter name/value comparison of a run response. 
This deviates with the `contains` configuration, which is a list of elements named `contains`. 
Any text in these is checked against the response to see if the response contains that value. 
This is NOT in a set of parameters to avoid collisions with parameters named `contains`.

This allows for multiple responses in a list type format.  The best usage is for something like selecting multiple rows 
from the database, and you can validate the rows one at a time.

# Configuration #
Configurations are:
  * `contains` = a list of string values that the page should contain, for checking varied content
  * `validate_spaces` = true if you wish to validate the response with spaces
  * `element_number` = is the position in the array to validate (starting at 0)
  * `expected_size` = validate the total number of results

# Parameter #
Parameters are iterated over and their values are compared to the values of the run response implementation of the 
previous run harness. 
Anything not matching is reported as an error and added to the error list.

# Sample Usage #
```
        <Run type="org.mash.harness.db.DBRunHarness" name="get_user">
            <Configuration name="url" property="jdbc.url"/>
            <Configuration name="user" property="jdbc.user"/>
            <Configuration name="password" property="jdbc.password"/>
            <Configuration name="driver">
                      <Value>net.sourceforge.jtds.jdbc.Driver</Value>
            </Configuration>
            <Parameter name="sql"><Value>select * from USER order by username</Value></Parameter>
       </Run>
       <Verify type="org.mash.harness.ListVerifyHarness" name="verify_user_data1">
             <Configuration name="element_number"><Value>5</Value></Configuration>
             <Configuration name="expected_size"><Value>10</Value></Configuration>
             <Parameter name="last_name"><Value>squarepants</Value></Parameter>
       </Verify>
       <Verify type="org.mash.harness.ListVerifyHarness" name="verify_user_data2">
             <Configuration name="element_number"><Value>6</Value></Configuration>
             <Configuration name="expected_size"><Value>10</Value></Configuration>
             <Parameter name="last_name"><Value>starfish</Value></Parameter>
       </Verify>
```

Here the verify looks at the response from the DBRunHarness named `get_user`. 
The first verify looks for the 5th user last name is `squarepants` and the number of rows is 10. 
The second verify looks for the 6th user last name is `starfish` and the number of rows is 10.