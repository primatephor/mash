# Introduction #
Verify already run command

# Configuration #

# Configuration #
  * `output` = expected results of command

# Sample Usages #
```
    <Verify type="org.mash.util.CommandExecutorVerifyHarness" name="verify command">        
        <Configuration name="output">
            <Value>some text</Value>
        </Configuration>
    </Verify>
```

This verifies that the result of, for example, `cat somefile.txt` with contents `some text` matches results.