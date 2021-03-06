# Introduction #
Copy a file from one location to another. 
The user can either pass the name of the original file to be copied or the desired content of the new file.

# Configuration #
Configuration values:


# Parameter #
Parameters for Copying:
  * `sourceFileName` = the name of the original file that should be copied.  Not used if `targetFileContent` is provided.
  * `argetFileNameBaseDir` = the name of a directory that should be prepended to `targetFileName`.  Optional.
  * `targetFileName` = the desired name of the newly created file.
  * `targetFileContent` = the desired content that should be saved in the newly created file.

# Sample Usages #
## Case One ##
```
    <Run type="org.mash.harness.file.FileCopyHarness" name="copy_properties">        
        <Parameter name="sourceFileName">
            <Value>/usr/local/myProject/config/mySetup.prop</Value>
        </Parameter>
        <Parameter name="targetFileName">
            <Value>/usr/local/testProject/config/testSetup.prop</Value>
        </Parameter>        
    </Run>
```
Mash will create a new file named `/usr/local/testProject/config/testSetup.prop` and copy the content from 
`/usr/local/myProject/config/mySetup.prop` into the new file.

## Case Two ##

```
    <Run type="org.mash.harness.file.FileCopyHarness" name="copy_properties">        
        <Parameter name="targetFileContent" file="/usr/local/myProject/config/mySetup.prop"/>
        <Parameter name="targetFileName">
            <Value>/usr/local/testProject/config/testSetup.prop</Value>
        </Parameter>        
    </Run>
```
This will read the contents of the file `/usr/local/myProject/config/mySetup.prop`, send the content to the 
`FileCopyHarness`, and save the content in a file named `/usr/local/testProject/config/testSetup.prop`