# Introduction #

List all files in a directory.

# Configuration #

# Parameter #
These parameters should be grouped:
  * `path` = Full path to the directory whose contents should be listed

# Sample Usage #
## Case One ##
```
    <Run type="org.mash.harness.file.FileListHarness" name="copy_properties">        
        <Parameter name="path">
            <Value>/usr/local/myProject/config/</Value>
        </Parameter>
    </Run>
```
Mash will create a response that contains a list of all files/folders contained in `/usr/local/myProject/config/`.