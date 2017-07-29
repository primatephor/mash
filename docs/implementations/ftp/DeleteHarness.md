# Introduction #

Run an ftp 'delete' operation on the directory or file.

# Configuration #
Configuration values:
  * 'url' = url of the resource
  * 'user' = user to login as
  * 'password' = password of user

# Parameter #
These parameters should be grouped:
  * 'path' = directory or file to remove

To delete:
```
    <Run type="org.mash.harness.ftp.DeleteHarness" name="first_delete">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="path"><Value>data/directory/to/delete</Value></Parameter>
    </Run>

    <Run type="org.mash.harness.ftp.DeleteHarness" name="second_delete">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="path"><Value>another/delete/file.txt</Value></Parameter>
    </Run>
```

The 'first\_delete' removes a directory, the 'second\_delete' removes the file 'file.txt'.  A list is run afterwards so verification is the same as with a ListHarness.