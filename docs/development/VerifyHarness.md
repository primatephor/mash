# Introduction #

The VerifyHarness verifies the state of invoked runs and setups.

See [DevelopingHarnesses](DevelopingHarnesses.md) for information on harness development

# Details #

The verify harness interface:

  * `void verify(RunHarness run, List<SetupHarness> setup)`

As with all other harnesses, it accepts
  * accepts Configurations (see [ParameterDefinition](../ParameterDefinition.md))
  * accepts Parameters (see [ParameterDefinition](../ParameterDefinition.md))
  * has a list off errors (see [HarnessError](../HarnessError.md))

Extending the supplied base harness will provide most of this functionality, and the developer only needs to implement 
the verify() method.

The verification should retrieve the response from the run, and pull together any necessary data from the harness or 
setup list to verify that the run is correct.