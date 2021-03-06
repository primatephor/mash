# Introduction #

Receive a Java object from the JMS queue.  This harness does not send an object message.

# Configuration #
Configuration values:
  * `provider_url` = url of the JMS provider
  * `queue` = the queue to connect to for send / receive

# Parameter #
These parameters should be grouped:
  * `action` = SEND
  * `property` = name / value pair to set as message property (format: name=value)

To receive a message:
```
    <Run type="org.mash.harness.message.jms.ObjectJMSHarness" name="read">
        <Configuration name="url"><Value>jnp://some.server.com:1099</Value></Configuration>
        <Configuration name="queue"><Value>queue/MyQueue</Value></Configuration>
        <Configuration name="action"><Value>RECEIVE</Value></Configuration>
        <Parameter name="property"><Value>name=value</Value></Parameter>
    </Run>
    <Verify type="org.mash.harness.StandardVerifyHarness">
        <Parameter name="user.address.street"><Value>the street addr</Value></Parameter>
        <Parameter name="name"><Value>value</Value></Parameter>
    </Verify>
```

This pulls the object message off the queue, and does an OGNL lookup. 
This particular object has a user object with an address object with a street attribute. 
This also verifies the property `name` on the message is set to `value`