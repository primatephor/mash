# Introduction #

Invoke an http request (currently only POST and GET are supported) against a web resource.

# Configuration #
Configuration values:
  * `url` = url of the resource
  * `type` = POST or GET
  * `clean` = true/false, resets the web conversation accordingly (like closing browser)

# Parameter #
  * `body` = Resource body to be submitted as input stream.  very optional, probably won`t use it regularly.

# Sample Usage #
```
    <Run type="org.mash.harness.http.HttpRunHarness" name="login">
        <Configuration name="url" property="my.url"/>
        <Configuration name="type"><Value>POST</Value></Configuration>
        <Parameter name="username"><Value>testuser</Value></Parameter>
        <Parameter name="password"><Value>pass</Value></Parameter>
    </Run>
    <Run type="org.mash.harness.http.HttpRunHarness">
        <Configuration name="url" property="my.url"><Value>/index.html</Value></Configuration>
        <Configuration name="type"><Value>GET</Value></Configuration>
        <Parameter name="session"><Response name="login">session</Response></Parameter>
    </Run>
```

Here a login is performed in the first call, then the second call goes to the `/index.html` page (with the `my.url` 
system property) with a parameter `session` (retrieved from the `login` run).