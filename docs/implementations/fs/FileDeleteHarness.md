# Introduction #
Delete a file or folder from disk.

# Configuration #
Configuration values:
  * `fileName` = the name of the original file that should be deleted.
  * `folderName` = the name of the original folder that should be deleted.
  * `justDeleteContent` = boolean value. If true, then only the file/folder's content is deleted. 
  The file/folder will remain on disk. Default value is `false`.

# Parameter #

# Sample Usages #
## Case One ##
```
   <Run type="org.mash.harness.file.FileDeleteHarness" name="empty_outbound_dir">
        <Configuration name="fileName">
            <Value>/usr/local/myProject/outputresult1.txt</Value>
        </Configuration>
        <Configuration name="justDeleteContent">
            <Value>true</Value>
        </Configuration>
    </Run> 
```
Mash will delete the content of file `/usr/local/myProject/output/result1.txt`. 
The file itself will remain and have a size of 0 bytes.

## Case Two ##
```
   <Run type="org.mash.harness.file.FileDeleteHarness" name="empty_outbound_dir">
        <Configuration name="fileName">
            <Value>/usr/local/myProject/outputresult1.txt</Value>
        </Configuration>
    </Run> 
```
Mash will delete the file `/usr/local/myProject/output/result1.txt`.
## Case Three ##
```
   <Run type="org.mash.harness.file.FileDeleteHarness" name="empty_outbound_dir">
        <Configuration name="folderName">
            <Value>/usr/local/myProject/output</Value>
        </Configuration>
        <Configuration name="justDeleteContent">
            <Value>true</Value>
        </Configuration>
    </Run> 
```
Mash will recursively delete everything contained in `/usr/local/myProject/output`, but the directory itself will 
remain