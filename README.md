# Overview #
MASH started as a system testing framework, and packaged with the code is all of the required libraries for running system tests with ant.

However MASH could also be used more as a general client invocation infrastructure.  This is because aspects of invoking a client interface are encapsulated into a harness.  The mash framework simply invokes the harness interfaces.

Much of the documentation here will focus on the system testing functionality supplied with the mash build, as this was the original intent and primary usage.  The downloadable `SystemTestDesign.pdf` is the original (and probably out of date) design document for this system.  This is highly recommended reading.

# ANNOUNCEMENT #
See the [ChangeList](docs/ChangeList.md) for what's new.

# Quick Intro #
There are four main harnesses for client invocation:
  * [SetupHarness](docs/development/SetupHarness.md) (setting up a run)
  * [RunHarness](docs/development/RunHarness.md) (perform some work)
  * [VerifyHarness](docs/development/VerifyHarness.md) (prove that work proceeded as expected)
  * [TeardownHarness](docs/development/TeardownHarness.md) (cleanup after running)

For more information on the current harness implementations, see the [HarnessImplementations](docs/implementations/HarnessImplementations.md) page

For more information on developing your own harnesses, see [DevelopingHarnesses](docs/development/DevelopingHarnesses.md)

Harness runs (Scripts) are arranged together via a Suite.  Each Script has a series of tags to make running only specific tagged Scripts easy.  This means that if you only want to run scripts with a specific tag, you need only supply that tag and MASH will filter all other scripts.

Here's a sample Script as system test:
```
<ns1:Script name="Base Test" xmlns:ns1="http://code.google.com/p/mash/schema/V1">
    <Tag>login</Tag>
    <Tag>website</Tag>
    <Tag>myapp</Tag>
    <Setup type="db">
        <Configuration name="clean"><Value>true</Value></Configuration>
        <Parameter name="loadfile" file="db_load.xml">
            <Replace search="VARIABLE"><Value>sometext</Value></Replace>
        </Parameter>
    </Setup>
    <Script file="../login/user_add.xml">
         <Parameter name="username"><Value>testuser</Value></Parameter>
    </Script>
    <Run type="http" name="login">
        <Configuration name="url" property="my.url"/>
        <Configuration name="type"><Value>POST</Value></Configuration>
        <Parameter name="username"><Value>testuser</Value></Parameter>
        <Parameter name="password"><Value>pass</Value></Parameter>
    </Run>
    <Verify type="http">
        <Configuration name="status"><Value>200</Value></Configuration>
        <Configuration name="title"><Value>My Page Title</Value></Configuration>
        <Configuration name="contains"><Value>Enter your order number</Value></Configuration>
        <Parameter name="do_search"><Value>1</Value></Parameter>
    </Verify>
    <Run type="http">
        <Configuration name="url" property="my.url"><Value>/index.html</Value></Configuration>
        <Configuration name="type"><Value>GET</Value></Configuration>
        <Parameter name="session"><Response name="login">session</Response></Parameter>
    </Run>
</ns1:Script>

```
For more information on Scripts, see the [ScriptDefinition](docs/ScriptDefinition.md) page.


This Script would get called through a Suite definition that lists all Scripts or directories containing scripts:
```
<ns1:Suite name="The Suite" xmlns:ns1="http://code.google.com/p/mash/schema/V1">
    <Script file="BaseTest.xml"/>
    <Script dir="some_directory"/>
</ns1:Suite>
```
For more information on Suites, see the [SuiteDefinition](docs/SuiteDefinition.md) page

You can run these scripts through various means:
  * Junit through ANT.  See [JunitRunner](docs/JunitRunner.md)
  * java command line invocation (or within an IDE).  See [JavaRuns](docs/JavaRuns.md)
