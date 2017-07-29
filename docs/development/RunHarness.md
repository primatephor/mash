# Introduction #

The RunHarness is used to run the script.

See [DevelopingHarnesses](DevelopingHarnesses.md) for information on harness development

# Details #

The run harness interface:

  * `void run(List<RunHarness> previous, List<SetupHarness> setups)`
  * `RunResponse getResponse()`

As with all other harnesses, it
  * accepts Configurations (see [ParameterDefinition](../ParameterDefinition.md))
  * accepts Parameters (see [ParameterDefinition](../ParameterDefinition.md))
  * has a list off errors (see [HarnessError](../HarnessError.md))

Extending the supplied base harness will provide most of this functionality, and the developer only needs to implement 
the run() and getResponse() method.

The run response interface is implemented as a means of accessing the response data in a standard way that's easy for 
the framework itself to verify with standard verify harness. 
Other implementations of verification may need implementations of special responses. 
For example, you may need to verify that a file got updated, so a file run response may have an access time and the 
file verifier may use that to check.