# Introduction #

Invoke an ftp request against an ftp server.

# Configuration #
Configuration values:
  * `url` = url of the resource
  * `user` = user to login as
  * `password` = password of user

# Parameter #
Parameters for ftping:
  * `ftp\_params` = Any string to be applied to the corresponding ftp command.  For a `put` or `get`, it'd be filename, 
  for an `ls` or `delete`, a path.

# Sample Usage #
```
    <Run type="org.mash.harness.ftp.FTPRunHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="ftp_params"><Value>get file.txt</Value></Parameter>
    </Run>
    <Run type="org.mash.harness.ftp.StandardVerifyHarness">
        <Configuration name="contains"><Value>some file content</Value></Configuration>
    </Run>
```

Here a file named `file.txt` is downloaded by user `theuser`.  It's contents are then verified to contain `some file 
content`.