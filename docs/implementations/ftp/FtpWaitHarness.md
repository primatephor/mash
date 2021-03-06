# Introduction #

Invoke an ftp LS request looking for files to be dumped onto specified path.  Default polling is every 5 seconds for 
60 seconds.

# Configuration #
Configuration values:
  * `url` = url of the resource
  * `user` = user to login as
  * `password` = password of user

# Parameter #
Parameters for ftping:
  * `path` = for listing what file or directory is there
  * `size` = number of files to wait for, default is 1

# Sample Usage #
```
    <Run type="org.mash.harness.ftp.FTPWaitHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="path"><Value>mydirectory</Value></Parameter>
        <Parameter name="size"><Value>3</Value></Parameter>
    </Run>
```

Here the wait harness polls the directory using the default settings (every 5 seconds for 60 seconds), until there are 
3 files present.

To change the path to a file, and change the default polling settings:
```
    <Run type="org.mash.harness.ftp.FTPWaitHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Configuration name="polltime"><Value>2</Value></Configuration>
        <Configuration name="timeout"><Value>30</Value></Configuration>
        <Parameter name="path"><Value>mydirectory/somefile.xml</Value></Parameter>
    </Run>
```

Here we check to see if a specific file is present, and poll every 2 seconds for 30 seconds.

Another option would be to get all files and poll until you find one with some specific content:
```
    <Run type="org.mash.harness.ftp.FTPWaitHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Configuration name="polltime"><Value>2</Value></Configuration>
        <Configuration name="timeout"><Value>30</Value></Configuration>
        <Parameter name="file_contents"><Value>Some Expected Text</Value></Parameter>
        <Parameter name="file_contents"><Value>More Expected Text</Value></Parameter>
    </Run>
```
Each file is parsed looking for the contents `Some Expected Text` and `More Expected Text`.  Both must be present.

The wait harness will hold the specific file if it's been found, so you can perform parsing / verification on it later.