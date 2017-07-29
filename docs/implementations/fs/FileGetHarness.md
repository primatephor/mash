# Introduction #

Retrieving a file and validating or saving it somewhere.

# Configuration #

# Parameter #
These parameters should be grouped:
  * 'path' = directory to look for contents or an index
  * 'file\_contents' = if path is given, search for a file containing these contents (EXPENSIVE)
    * 'file\_index'= if path is given, index will sort the file list by data and retrieve the file at the given index.

  * 'file\_name' = path and name of the file to retrieve.  If used, 'path' will be ignored.

  * 'output\_file' = path and filename to store retrieved file to.

# Sample Usages #
## Case Two ##

```
    <Run type="org.mash.harness.file.FileGetHarness" name="get_properties">        
        <Parameter name="file_name"><Value>setup.prop</Value></Parameter>
        <Parameter name="output_file"><Value>/tmp/my_setup.prop</Value></Parameter>
    </Run>

```
This retrieves file 'setup.prop', and it's saved to a file in /tmp called 'my\_setup.prop'.