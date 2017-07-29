# Introduction #

The `DBSetupHarness` is used to work with flat xml (like dbunit) or sql to load a database.


# Configuration #
Configuration parameters:
  * 'url' = url of the database used by dbunit.  see dbunit docs on `JdbcDatabaseTester`
  * 'user' = user to log into the db
  * 'password' = user's password
  * 'driver' = jdbc driver class
  * 'type' = type of dbunit operation (only used for flat xml file loading)
    * CLEAN\_INSERT (delete and reinsert rows)
    * DELETE (only delete rows defined by xml)
    * DELETE\_ALL (delete all rows defined by tables in xml)
    * INSERT (insert data in xml)
    * REFRESH (insert new rows into db, update existing)
    * TRUNCATE (truncate tables)
    * UPDATE (update rows defined in xml)

# Parameter #
If a specified file parameter ends with 'sql', then the sql is executed against the database.  If the file parameter ends with 'xml', then dbunit is used to insert the flat file data into the db.

# Sample Usage #
```
    <Setup type="org.mash.harness.db.DBSetupHarness">
        <Configuration name="type"><Value>clean_insert</Value></Configuration>
        <Parameter name="loadfile" file="db_load.xml">
            <Replace search="VARIABLE">
                <Value>sometext</Value>
            </Replace>
        </Parameter>
    </Setup>
```

Here the db\_load.xml is loaded, and every instance of 'VARIABLE' is replaced with 'sometext'.  Then the data is loaded (first by removing the specified rows in the xml) per the DBUnit rules.  This DOES use DBUnit to run the load and only works with flat xml data sets.  This will also just run sql.