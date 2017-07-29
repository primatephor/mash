# Introduction #

The TeardownHarness should clean up anything left open or invalid during the run process.

See DevelopingHarnesses for information on harness development

# Details #

The teardown harness interface:

  * `void teardown(List<SetupHarness> setups)`

As with all other harnesses, it accepts
  * Configurations (see ConfigurationDefinition)
  * Parameters (see ParameterDefinition)
  * has a list off errors (see HarnessError)

Extending the supplied base harness will provide most of this functionality, and the developer only needs to implement the teardown() method.