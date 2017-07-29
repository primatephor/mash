# Introduction #

The TeardownHarness should clean up anything left open or invalid during the run process.

See [DevelopingHarnesses](DevelopingHarnesses.md) for information on harness development

# Details #

The teardown harness interface:

  * `void teardown(List<SetupHarness> setups)`

As with all other harnesses, it accepts
  * accepts Configurations (see [ParameterDefinition](../ParameterDefinition.md))
  * accepts Parameters (see [ParameterDefinition](../ParameterDefinition.md))
  * has a list off errors (see [HarnessError](../HarnessError.md))

Extending the supplied base harness will provide most of this functionality, and the developer only needs to implement 
the teardown() method.