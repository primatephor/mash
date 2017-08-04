# Introduction #

Invoke a restful http request against a web resource. 
Based on the [HttpRunHarness](HttpRunHarness.md).

# Configuration #
Configuration values:
  * `url` = url of the resource
  * `type` = POST, PUT, GET, DELETE
  * `clean` = true/false, resets the web conversation accordingly (like closing browser)
  * `content_type` = optional type of content to submit (default is application/xml)
  * `accept_type` = optional type of content to accept from resource (default is application/xml)

# Parameter #
Parameters are any form or query string values to be passed along with the web request, as well as the special values:
  * `body` = Resource body to be submitted

# Sample Usage #
```
    <Run type="org.mash.harness.rest.RestRunHarness">
        <Configuration name="url"><Value>http://my.test.system/resource</Value></Configuration>
        <Configuration name="type"><Value>POST</Value></Configuration>
        <Configuration name="content-type"><Value>application/xml</Value></Configuration>
        <Configuration name="accept-type"><Value>application/xml</Value></Configuration>
        <Parameter name="body" file="some_submission.xml"/>
    </Run>
    <Verify type="org.mash.harness.StandardVerifyHarness">
        <Parameter name="/User/Name/LastName"><Value>squarepants</Value></Parameter>        
    </Verify>
```

The file `some_submission.xml` is an xml as specified by the resource at the url `http://my.test.system/resource`. 
This will perform a POST of the data to the url. 
The response is verified to contain the last name in the appropriate element.