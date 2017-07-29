# Introduction #

Clean up a JMS queue by reading all messages (which pulls everything off it)

# Configuration #
Configuration values:
  * 'provider\_url' = url of the JMS provider
  * 'queue' = the queue to connect to for send / receive

# Parameter #
These parameters should be grouped:
  * 'action' = CLEAN (only one right now)

To receive a message:
```
    <Run type="org.mash.harness.message.jms.JMSSetupHarness" name="clean">
        <Configuration name="url"><Value>jnp://some.server.com:1099</Value></Configuration>
        <Configuration name="queue"><Value>queue/MyQueue</Value></Configuration>
        <Configuration name="action"><Value>CLEAN</Value></Configuration>
    </Run>
```

Hook up to the queue, and read everything off of it.  This empties the queue.