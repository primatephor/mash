# Introduction #

Watch the content of a specified path for files. 
Default polling interval is every 5 seconds for a duration of 60 seconds.

# Configuration #

# Parameter #
  * `path` = full path of the directory to watch.
  * `size` = number of files to wait for.  Default value is 1
  * `file_came` = name of the file to wait for.
  * `file_contents` = list of strings that the found file must contain.
  * `file_size` = The harness will wait until the combined size of all files in the path reaches this amount.

# Sample Usage #
## Case One ##
```
    <Run type="org.mash.harness.ftp.FileWaitHarness">        
        <Parameter name="path">
            <Value>mydirectory</Value>
        </Parameter>
        <Parameter name="size">
            <Value>3</Value>
        </Parameter>
    </Run>
```

Here the wait harness polls the directory using the default settings (every 5 seconds for 60 seconds), until there are 
3 files present.