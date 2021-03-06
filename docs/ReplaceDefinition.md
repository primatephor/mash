# Introduction #

How configurations and parameters are replaced with search strings


# Details #
Replace is relatively simple. 
A search attribute is supplied with the element, and everywhere that attribute is found in the content, the value 
specified in the element is used.

Replace accepts the following data:
  * `Value` (just set a value)
  * `Property` (a system property)
  * `Date` (see DateDefinition)
  * `File` (file content)

Most of these can be used together. 
For example, you could use value and property together. 
This allows you to build interesting values by layering the information.

The value of the Replace is calculated in the following order
  * The value is used
  * The system property is used.  If a value is specified, it is appended after the system property.
  * The DateDefinition is used, any previous value is overridden
  * The file contents are used, any previous value is overridden