# Introduction #

Each harness has it's own specific interface to implement (setup, run, verify, teardown).  However there are common themes for developing a harness that simplify the plumbing.

Harnesses must implement one of the interfaces:
  * SetupHarness
  * RunHarness
  * VerifyHarness
  * TeardownHarness


# `BaseHarness` #

Extending the `BaseHarness` implements many of the methods in the `Harness` interface.  It is advised to use this class to simplify your harness, however if there is special functionality you need you can implement these methods on your own.

# Name #
When using the `type` attribute you can either specify a fully qualified class name, or a shorter name that you specify on your harness.

To specify a name use the `@HarnessName` annotation on the class.

Example:
```
   package com.my.harnesses;

   @HarnessName(name="my_harness")
   public class MyHarness extends BaseHarness implements RunHarness
   {
      ...
   }
```
Normally you use the class name in type:
```
   <Run type="com.my.harnesses.MyHarness" name="run my harness">
   ...
   </Run>
```

with the `HarnessName`, your XML may now use this name instead of the class name:
```
   <Run type="my_harness" name="run my harness">
   ...
   </Run>
```


# Configuration #
There are two ways of getting configurations into your harness.  One is to parse the `Configuration` list manually and selectively apply them to your harness.

Another (simpler) way of getting configuration data is to use the `@HarnessConfiguration` annotation.  Add this annotation to the method you want invoked with the appropriate `<Configuration>` element.  The 'name' parameter on the `@HarnessConfiguration` annotation is the name of the `<Configuration>` element.

Example:
```
   @HarnessConfiguration(name="url")
   public void setUrl(String url)
   {
      this.url = url;
   }
```
Now your harness will accept a `<Configuration>` element with the name 'url'


# Parameter #
Parameters are configured just like configurations, so you may either parse the parameter list or use the `@HarnessParameter` annotation.  Again, with the `@HarnessParameter` the 'name' parameter is the name of the `<Parameter>` element.

Example:
```
   @HarnessParameter(name="port")
   public void setRequestPort(String requestPort)
   {
      this.port = Integer.valueOf(requestPort);
   }
```
Now your harness will accept a `<Parameter>` element with the name 'port'