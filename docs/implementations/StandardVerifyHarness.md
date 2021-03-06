# Introduction #

The simple parameter name/value comparison of a run response. 
This deviates with the `contains` configuration, which is a list of elements named `contains`. 
Any text in these is checked against the response to see if the response contains that value. 
This is NOT in a set of parameters to avoid collisions with parameters named `contains`.

# Configuration #
Configurations are:
  * `contains` = a list of string values that the page should contain, for checking varied content
  * `validate_spaces` = true if you wish to validate the response with spaces

# Parameter #
Parameters are iterated over and their values are compared to the values of the run response implementation of the 
previous run harness. 
Anything not matching is reported as an error and added to the error list.

# Sample Usage #
```
        <Run type="org.mash.harness.http.HttpRunHarness" name="login">
            <Configuration name="url" property="my.url"/>
            <Configuration name="type">POST</Configuration>
            <Parameter name="username">testuser</Parameter>
            <Parameter name="password">pass</Parameter>
        </Run>
        <Verify type="org.mash.harness.StandardVerifyHarness">
            <Configuration name="contains">some text</Configuration>
            <Configuration name="contains">Another set of text</Configuration>
            <Parameter name="username">testuser</Parameter>
        </Verify>
```

Here the verify looks at the response from the HttpRunHarness named `login`. 
It just checks that the username is returned in the response, and that the response page contains the text `some text` 
and `Another set of text`