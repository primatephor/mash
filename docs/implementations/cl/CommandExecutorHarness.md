# Introduction #
Executes a terminal command

# Configuration #

# Parameter #
  * `command` = any string that will be run as a command.
  * `commandHomeDir` = execution path where the command can be found.  Optional.
  * `currentWorkingDir` = directory against which the command should be run.  Optional.

# Sample Usages #
## Case One ##
```
    <Run type="org.mash.util.CommandExecutorHarness" name="run_command">        
        <Parameter name="command">
            <Value>myScript.sh</Value>
        </Parameter>
        <Parameter name="commandHomeDir">
            <Value>/usr/local/bin</Value>
        </Parameter>        
    </Run>
```

Mash will execute `myScript.sh` that is found in `/usr/local/bin/`.