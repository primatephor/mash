# Introduction #

Run an ftp 'get' operation, retrieving a file and validating or putting it somewhere.

# Configuration #
Configuration values:
  * 'url' = url of the resource
  * 'user' = user to login as
  * 'password' = password of user

# Parameter #
These parameters should be grouped:
  * 'path' = directory to look for contents or an index
    * 'file\_contents' = if path is given, search for a file containing these contents (EXPENSIVE)
    * 'file\_index'= if path is given, index will sort the file list by data and get the file at the given index.

  * 'file\_name' = path and name of the file to get.  If used, 'path' will be ignored.

  * 'output\_file' = path and filename to store retrieved file to.
  * 'transfer\_mode' = mode of transfer to perform, default is ASCII. Values:
    * BINARY
    * ASCII

To retrieve the file with a get and save it locally:
```
    <Run type="org.mash.harness.ftp.GetHarness" name="first_get">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="file_name"><Value>file.txt</Value></Parameter>
        <Parameter name="output_file"><Value>/tmp/my_local_file.txt</Value></Parameter>
    </Run>

    <Run type="org.mash.harness.ftp.GetHarness" name="second_get">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="path"><Value>path/to/file</Value></Parameter>
        <Parameter name="file_index"><Value>0</Value></Parameter>
        <Parameter name="output_file"><Value>/tmp/my_local_file2.txt</Value></Parameter>
    </Run>
```

The 'first\_get' retrieves file 'file.txt', and it's saved to a file in /tmp called 'my\_local\_file.txt'.

The 'second\_get' retrieves the first file by data in the directory 'path/to/file'.