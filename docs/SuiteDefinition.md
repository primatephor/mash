# Introduction #

A collection of scripts are tied together in a suite. 
For a system test context, this would be the centralized invocation point for all the sub scripts.


# Details #
A description of a suite:
```
<ns1:Suite name="The Suite" xmlns:ns1="http://www.mash.org/schema/V1">
```
The name of the suite `The Suite` is supplied here, right now mainly for pretty reporting
The version 1 of the namespace is included

```
    <Script file="baseRun.xml"/>
```
  * Define a specific Script file to be run

```
    <Script dir="suite"/>
```
This specifies a directory that will be parsed looking for Scripts. 
All scripts found in the directory are part of this suite, any non Script files are ignored.

```
    <Parallel>
        <Script dir="suite"/>
    </Parallel>
</ns1:Suite>
```
While not yet working, the parallel element will define Scripts that are run with in parallel.