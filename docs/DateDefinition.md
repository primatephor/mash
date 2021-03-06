# Introduction #

Date is used in configuration and parameter elements, and has some special settings for formatting and modifying date 
values


# Details #
Date has several attributes for configuration.  If no value is specified for the element, the current date (now) is used. 
Attributes are:
  * `format`: format of either the supplied date string, or what the date to be used should look like
  * `secOffset`: number seconds offset (+/-) from specified date
  * `minOffset`: number minutes offset (+/-) from specified date
  * `hourOffset`: number hours offset (+/-) from specified date
  * `dayOffset`: number days offset (+/-) from specified date
  * `monthOffset`: number months offset (+/-) from specified date
  * `yearOffset`: number years offset (+/-) from specified date
  * `zone`: timezone of the specified date

If a date is specified, it is read using the format attribute. 
If no format attribute is specified, the Java default date format is used. 
Offset values are then applied to adjust the time. 
Date for use by other values has the format applied (with zone).

Here's an example using date:
```
<Parameter name="loadfile" file="sample_data/db_load.sql">
    <Replace search="{replace.date}">
        <Date format="yyyy-MM-dd kk:mm:ss.S" dayOffset="-6"/>
    </Replace>
</Parameter>
```
Here the sample loaded sql file will replace the string `{replace.date}` with the current date minus 6 days, formatted 
like `yyyy-MM-dd kk:mm:ss.S`