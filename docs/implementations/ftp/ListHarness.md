# Introduction #

Run an ftp `ls` operation, retrieving a file list from the specified path.

# Configuration #
Configuration values:
  * `url` = url of the resource
  * `user` = user to login as
  * `password` = password of user

# Parameter #
These parameters should be grouped:
  * `path` = directory to list contents

To retrieve list of files:
```
    <Run type="org.mash.harness.ftp.GetHarness" name="list">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="path"><Value>path/to/file</Value></Parameter>
    </Run>
    <Verify type="org.mash.harness.StandardVerifyHarness">
        <Parameter name="filename.txt"><Value>filename.txt</Value></Parameter>        
    </Verify>
```
After listing the file, just verify that the expected file is present.