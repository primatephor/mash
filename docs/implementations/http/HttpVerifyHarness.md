# Introduction #

Extends the functionality of the [StandardVerifyHarness](../StandardVerifyHarness.md), and also checks for ancillary 
http specific content.


# Configuration #
Configurations are:
  * `title` = the title of the page
  * `status` = the integer status code of the response
  * `contains` = a list of string values that the page should contain, for checking varied content

# Paramteter #
see StandardVerifyHarness

Parameters names are searched for within the response html in the following order:
  * an element `name` if it matches
  * an element `id` if it matches
  * an xpath expression if it matches

This uses the `HtmlUnit` for xpath parsing.  If you use this make sure you have a well formed html.  Logging in debug 
mode is handy for tracking errors.

# Sample Usage #
```
    <Run type="org.mash.harness.HttpRunHarness" name="login">
        <Configuration name="url" property="my.url"/>
        <Configuration name="type"><Value>POST</Value></Configuration>
        <Parameter name="username"><Value>testuser</Value></Parameter>
        <Parameter name="password"><Value>pass</Value></Parameter>
    </Run>
    <Verify type="org.mash.harness.http.HttpVerifyHarness">
        <Configuration name="status"><Value>200</Value></Configuration>
        <Configuration name="title"><Value>My Page Title</Value></Configuration>
        <Configuration name="contains"><Value>Enter your order number</Value></Configuration>
        <Parameter name="do_search"><Value>1</Value></Parameter>
        <Parameter name="//table[@class=`my_table`]/tr[1]/td[1]"><Value>some value!</Value></Parameter>
    </Verify>
```

Here the login run is performed, and the response is verified that the title is `My Page Title`, the status is `200`, 
and there contains text `Enter your order number`. 
Also, the parameter `do_search` is returned, and the verify will explicitly check that.

Finally, the first `<td>` is validated in the first table containing an attribute `class='my_table'`