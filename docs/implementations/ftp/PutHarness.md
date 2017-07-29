# Introduction #

Run an ftp 'put' operation, placing the specified file in the specified directory.

# Configuration #
Configuration values:
  * 'url' = url of the resource
  * 'user' = user to login as
  * 'password' = password of user

# Parameter #
These parameters should be grouped:
  * 'output\_path' = directory to put file to
  * 'file\_name' = path and name of the file to put.

To put a file:
```
    <Run type="org.mash.harness.ftp.PutHarness" name="first_put">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="file_name"><Value>filename.txt</Value></Parameter>
        <Parameter name="output_path"><Value>data/directory/to/dump/to</Value></Parameter>
    </Run>

    <Verify type="org.mash.harness.StandardVerifyHarness">
        <Parameter name="filename.txt"><Value>filename.txt</Value></Parameter>        
    </Verify>
```

This puts the file onto, then verifies that the file is placed there.  The verify runs a simple list operation, so you can check that the file made it.