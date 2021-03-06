# Introduction #

Script definitions define all the work performed by a single script run (or in a test, this would be a single system 
test).


# Details #
A break down of a sample script:
```
<ns1:Script name="Base Test" xmlns:ns1="http://www.mash.org/schema/V1" order="0">
```
  * `xmlns:ns1="http://www.mash.org/schema/V1"` is the version 1 namespace of the config. 
  There will likely be other versions in the future.
  * `name` is the name of this run. If not supplied, the name is the name of the file.
  * `order` is an optional element. 
  If not supplied, the test name will be the order. 
  All ordered elements come before any named elements in the overall sort.

# Tags #
Tags are used to filter which scripts are to be run.
```
    <Tag>login</Tag>
    <Tag>website</Tag>
    <Tag>myapp</Tag>
```
If this were being run as a system test and `login` was passed as a parameter, then this particular script would be run 
as part of that tagged selection.

# Scripts #
You can run other scripts from within a script.
```
    <Script file="BaseTest.xml"/>
    <Script dir="some_directory"/>
```
  * Here a specified test `BaseTest.xml`
  * Or you can run an entire directory of scripts

# Setup #
You may specify a fully qualified class:
```
    <Setup type="org.mash.harness.DBSetupHarness">
```

Or the type attribute specified in [HarnessImplementations](implementations/HarnessImplementations.md):
```
    <Setup type="db">
```
This specifies an implementation of a [SetupHarness](development/SetupHarness.md), and the class specified by the type 
attribute will be loaded to run this harness.

# Configuration #
Configurations are parameters that are required for a harness to be setup properly.
```
        <Configuration name="clean">
            <Value>true</Value>
        </Configuration>
```
Configurations are passed to harnesses as to be used during initialization. 
In this instance, the [DBSetupHarness](implementations/db/sql/DBSetupHarness.md) will run a dbunit cleaning when 
loading a specified file.  

See [ParameterDefinition](ParameterDefinition.md) for more

# Parameters #
Parameters are variables used by the harness that will dictate how / what is run.
```
        <Parameter name="loadfile" file="db_load.xml">
            <Replace search="VARIABLE">
                <Value>sometext</Value>
            </Replace>
        </Parameter>
```
By specifying a file attribute for the parameter, this file will get loaded and it's contents are passed to the harness 
with the name `loadfile` above.

The replace tag will search the specified text (in this instance, the file contents) and perform a regular expression 
search and replace. 
In this case, every instance of VARIABLE is replaced with the string `sometext`.

You CAN do a replace on a `<Value>` string, which is a good way to insert data from previous runs. 
For example:
```
        <Parameter name="someParam">
            <Value>this text has VARIABLE</Value>
            <Replace search="VARIABLE">
                <Value>sometext</Value>
            </Replace>
        </Parameter>
```
Now the text will get replaced to `this text has sometext`

Common usage of this case is replacing user id's in a URL, or changing properties.

See [ParameterDefinition](ParameterDefinition.md) for more

# Run #
Runs perform the core desired action.
```
    </Setup>
    <Run type="org.mash.harness.HttpRunHarness" name="login">
        <Configuration name="url" property="my.url"/>
        <Configuration name="type">
            <Value>POST</Value>
        </Configuration>
        <Parameter name="username">
            <Value>testuser</Value>
        </Parameter>
        <Parameter name="password">
            <Value>pass</Value>
        </Parameter>
    </Run>
```
optionally, you could use the type attributes in [HarnessImplementations](implementations/HarnessImplementations.md)
```
     ...
    <Run type="http" name="login">
     ...   
```
The [HttpRunHarness](implementations/http/HttpRunHarness.md) here will invoke an http request.

The name of this harness is `login`, for use by later harnesses that need the response data.

The first configuration is the `url` parameter, and its value is the system property `my.url`.

The second configuration here is the `type` of http request (post, get, etc).  See 
[HttpRunHarness](implementations/http/HttpRunHarness.md) for more information.

Parameters passed for this harness are dependent on the the valid parameters for a particular page invocation.

# Verify #
Verify harnesses look at the previous run and perform some validation. 
The [StandardVerifyHarness](implementations/StandardVerifyHarness.md) is a common way of associating parameter names 
to values, and should fit for quite a few run harnesses.
```
    <Verify type="verify">
        <Configuration name="status">
            <Value>200</Value>
        </Configuration>
        <Configuration name="title">
            <Value>My Page Title</Value>
        </Configuration>
        <Configuration name="contains">
            <Value>Enter your order number</Value>
        </Configuration>
        <Parameter name="do_search">
            <Value>1</Value>
        </Parameter>
    </Verify>
```
The [HttpVerifyHarness](implementations/http/HttpVerifyHarness.md) will validate a web page response.

# Another Run #
Multiple runs produce more interesting script behavior.
```
    <Run type="http">
        <Configuration name="url" property="my.url">
            <Value>/index.html</Value>
        </Configuration>
        <Configuration name="type">
            <Value>GET</Value>
        </Configuration>
        <Parameter name="session">
            <Response name="login">session</Response>
        </Parameter>
    </Run>
</ns1:Script>
```
This [HttpRunHarness](implementations/http/HttpRunHarness.md) invokes another http request, this time appending to the 
configuration `url` the `/index.html`.
The parameter sets the session value retrieved from the `login` 
[HttpRunHarness](implementations/http/HttpRunHarness.md) retrieving the session and setting it in the current request.

See [ParameterDefinition](ParameterDefinition.md)