# Introduction #

Parameters are used by harnesses to pass along information used in the harness run. 

Configurations are used by harnesses to pass along information specific to the functioning of a harness. 
From scripting point of view, Configurations and Parameters are identical.

A good example of the difference between a Configuration and a Parameter is considering an ftp url versus am ftp file 
to get. 
The url to connect to is a Configuration, whereas the file to get is a Parameter.

Each harness uses Parameters and Configurations differently. 
Verify harnesses will look for Parameters in responses, but run harnesses will send them as data.

# Details #
Parameters accept the following data:
  * `Value` (just set a value)
  * `Property` (system property attribute)
  * `Date` (see [DateDefinition](DateDefinition.md))
  * `Response` (data from a previous setup)
  * `File` (file contents)
  * `Replace` (search and replace information)

Most of these can be used together. 
For example, you could use value and replace together, or reponse, replace, and date together. 
This allows you to build interesting values by layering the information.

Parameter values are calculated based on some precedence, since they may be layered (you could make a parameter some 
file contents and replace certain values with a date). 
The value of the Parameter is calculated in the following order
  * The value is used
  * The system property is used.  If a value is specified, it is appended after the system property.
  * The [DateDefinition](DateDefinition.md) is used, any previous value is overridden
  * The response from a previous run is used, any previous value is overridden
  * The file contents are used, any previous value is overridden
  * The content [ReplaceDefinition](ReplaceDefinition.md) is run

# Samples #
This is a basic parameter:
```
    <Parameter name="status"><Value>FAILURE</Value></Parameter>
```
it is setting a parameter `status` to `FAILURE`

---

This configuration uses a system property:
```
    <Configuration name="provider_url" property="my.provider.url"/>
```
uses environment variable named `my.provider.url` to set the value of `provider_url`

---

This is a file load:
```
    <Parameter name="sql" file="test_01_data/db_load.sql"/>
```
retrieves the text from directory `test_01_data/db_load.sql` and puts it into parameter named `sql`

---

Here is a date:
```
    <Parameter name="dateString"><Date dayOffset="-6"/></Parameter>
```
This retrieves the current date and subtracts 6 days from it.
See [DateDefinition](DateDefinition.md) for more information

---

Here is a complex replacement with dates:
```
   <Parameter name="myString">
       <Value>replace {accepted.date} and {another.date} and {accepted.date} again</Value>
       <Replace search="{accepted.date}">
           <Date format="yyyy-MM-dd kk:mm:ss.S" dayOffset="-6"/>
       </Replace>
       <Replace search="{another.date}">
           <Date format="yyyy-MM-dd kk:mm:ss.S" dayOffset="-5"/>
       </Replace>
    </Parameter>
```
This replaces values in the defined string value with dates of a specified format. 
The first date is 6 days ago and the second is 5 days ago.

---

Here is a parameter using a response:
```
    <Parameter name="first_name">
       <Response name="get_user">/User/Contact/FirstName</Response>
    </Parameter>
```
This response references a previously run harness with the name `get_user`, it happened to return XML so we use xpath 
to retrieve from that xml the `first_name` value. 
If this were an http response, we'd just put in the parameter named on the page.


---

Both Parameter and Configurations may also have names that are calculated using the same rules above. 
There may be instances where the name of the parameter is dynamic, based on the current state of the test.

A sample:
```
    <Parameter>
       <ParamName>
          <Value>user_{user.name}</Value>
          <Replace search="{user.name}">
             <Response name="get_user">/User/UserName</Response>
          </Replace>
       </ParamName>
       <Response name="get_user">/User/Contact/FirstName</Response>
    </Parameter>
```
Here, if the xml with a user name of "bsquarepants" is supplied with a contact first name "Bob", the resulting 
parameter has a name: `user_bsquarepants` and value: `Bob`.