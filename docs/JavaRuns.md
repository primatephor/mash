#Running with Java

# Introduction #

Invoking scripts or suites of scripts can be done from the command line, with the right parameters of course.


# Details #

To run a single script, use the `org.mash.harness.StandardScriptRunner`.  To run a suite of scripts, use the `org.mash.main.SuiteRunner`.

### IMPORTANT ###
Both of these classes use the current directory as a base path to access any subscripts defined in the invoked script / suite.  As a rule, run these from the base source directory of your suite / scripts.

Both classes accept one argument, the name of the file to be invoked. A sample execution:
```
java org.mash.harness.StandardScriptRunner path/to/my/script.xml

java org.mash.main.SuiteRunner suite.xml
```

Here are some properties that might be used while running java
```
-Dlog4j.configuration=file:///home/user/dev/log4j.properties
-Djdbc.url=jdbc:jtds:sybase://localhost:5100
-Dmy.first.test.property=192.168.1.100
-Dmy.second.test.property=abdcefg
```
The log4j configuration is (obviously) where the log4j properties configuration file lives.  All the other properties are specifically for my scripts.  You could reference these in the scripts using parameter and configuration 'property' attributes.