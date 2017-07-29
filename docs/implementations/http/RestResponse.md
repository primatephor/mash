# Details #
The RestResponse differs from the standard response in that the calls for retrieving the value is done with an XPath. 
This means that the when verifying the results, use XPath expressions instead of simple name/value pairings.

# Sample Usage #
```
    <Run type="org.mash.harness.rest.RestRunHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="type">POST</Configuration>
        <Parameter name="body" file="some_file.xml"/>
    </Run>
    <Verify type="org.mash.harness.http.HttpVerifyHarness">
        <Configuration name="status">200</Configuration>
        <Parameter name="/SomeEntity/Firstname">Joe</Parameter>
    </Verify>
```

Here the HttpVerifyHarness is used to validate a resource status.  The parameter is named very differently here, as an 
XPath expression. 
This particular path works for the following xml returned by this resource:
```
<SomeEntity>
      <Firstname>Joe</Firstname>
      <Lastname>Somebody</Lastname>
      <Address>
            <Street1>Street1</Street1>
            <Street2>Street2</Street2>
      </Address>
</SomeEntity>
```