# Introduction #

The SetupHarness is used to prepare a script for running. 
Multiple setup harness invocations are possible with each script.

See [DevelopingHarnesses](DevelopingHarnesses.md) for information on harness development

# Details #

The setup harness interface is simple:

  * void setup() throws Exception

As with all other harnesses, it 
  * accepts Configurations (see [ParameterDefinition](../ParameterDefinition.md))
  * accepts Parameters (see [ParameterDefinition](../ParameterDefinition.md))
  * has a list off errors (see [HarnessError](../HarnessError.md))

Extending the supplied base harness will provide most of this functionality, and the developer only needs to implement 
the setup() method.