# Introduction #

Send and receive and text message on a jms queue.

# Configuration #
Configuration values:
  * `provider_url` = url of the JMS provider
  * `queue` = the queue to connect to for send / receive

# Parameter #
These parameters should be grouped:
  * `action` = SEND or RECEIVE
  * `message` = to be sent
  * `property` = name / value pair to set as message property (format: name=value)

To send a message:
```
    <Run type="org.mash.harness.message.jms.XmlJMSHarness" name="send">
        <Configuration name="url"><Value>jnp://some.server.com:1099</Value></Configuration>
        <Configuration name="queue"><Value>queue/MyQueue</Value></Configuration>
        <Configuration name="action"><Value>SEND</Value></Configuration>
        <Parameter name="message" file="data/toSend.txt"/>
        <Parameter name="property"><Value>name=value</Value></Parameter>
    </Run>
```

Sending the xml in `data/toSend.txt` to the jms queue (a text message) on the server `some.server.com:1099`.

To receive a message:
```
    <Run type="org.mash.harness.message.jms.XmlJMSHarness" name="read">
        <Configuration name="url"><Value>jnp://some.server.com:1099</Value></Configuration>
        <Configuration name="queue"><Value>queue/MyQueue</Value></Configuration>
        <Configuration name="action"><Value>RECEIVE</Value></Configuration>
    </Run>
    <Verify type="org.mash.harness.StandardVerifyHarness">
        <Configuration name="contains"><Value>some text in the file</Value></Configuration>
        <Parameter name="name"><Value>value</Value></Parameter>
    </Verify>
```

This will pick the first message off the queue, and verify it has the property `name` and contains the text 
`some text in the file`