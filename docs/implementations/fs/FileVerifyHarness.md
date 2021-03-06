# Introduction #
Compare the contents of two files and verify they match. 
Line separation characters are ignored (CR+LF / NL). 
An optional list of allowed differences can be provided.

For instance if the expected file content is `I have two pet cats` and the actual output is `I have two pet dogs`, this 
would not be considered a match; however, you could define an allowed exception of `cat` and this would now be 
considered a match.

# Configuration #
Configuration values:


# Parameter #
  * `epxected` = name of the file whose contents are expected to be in the found file
  * all others =  any other parameter will be treated as an allowed difference between files.

# Sample Usages #
## Case One ##
```
    <Run type="org.mash.harness.file.FileListHarness" name="list_configurations">        
        <Parameter name="path">
            <Value>/usr/local/myProject/config/</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.file.FileVerifyHarness" name="verify_found_file">
        <Parameter name="expected" file="epxected_files/my_config.txt"/> 
    </Verify>
```
Mash will get a list of files in, and then loop through the list and ensure that at least one file matches the content 
of `my_config.txt`.

## Case Two ##
```
    <Run type="org.mash.harness.file.FileListHarness" name="list_configurations">        
        <Parameter name="path">
            <Value>/usr/local/myProject/config/</Value>
        </Parameter>
    </Run>
    <Verify type="org.mash.harness.file.FileVerifyHarness" name="verify_found_file">
        <Parameter name="expected" file="ExpectedOutput/myConfig.txt"/>
        <Parameter name="allowedYear">
            <Value>YYYY</Value>
        </Parameter>        
        <Parameter name="allowedMonth">
            <Value>MM</Value>
        </Parameter>
    </Verify>
```
Mash will get a list of files in, and then loop through the list and ensure that at least one file matches the content 
of `my_config.txt`. 
When the files are compared, a list of differences will be created. 
If the differences are `YYYY` or `MM`, then the difference will be ignored and the files will match.