# Introduction #

Verify a file and some simple attributes on a server

# Configuration #
Configuration values:
  * `file_name` = name of file to verify

# Parameter #
  * `size` = size of file on server

# Sample Usage #
```
    <Run type="org.mash.harness.ftp.FTPRunHarness">
        <Configuration name="url" property="my.url"/>
        <Configuration name="user"><Value>theuser</Value></Configuration>
        <Configuration name="password"><Value>password</Value></Configuration>
        <Parameter name="operation"><Value>GET</Value></Parameter>
        <Parameter name="ftp_params"><Value>file.txt</Value></Parameter>
        <Parameter name="file_path"><Value>/tmp/my_local_file.txt</Value></Parameter>
    </Run>
    <Verify type="org.mash.harness.ftp.FTPVerifyListHarness">
        <Configuration name="file_name"><Value>file.txt</Value></Configuration>
        <Parameter name="size"><Value>1024</Value></Parameter>
    </Verify>
```

Here a file named `file.txt` attributes are retrieved and it`s size is checked to be 1024.