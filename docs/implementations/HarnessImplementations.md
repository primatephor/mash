# Introduction #

This is the list of supplied harnesses.  To build your own, check out DevelopingHarnesses.


# Relational DB #
  * `mash-dbharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [DBSetupHarness](db/sql/DBSetupHarness.md) | db                 | uses dbunit, runs sql |
| [DBWaitHarness](db/sql/DBWaitHarness.md) | db\_wait           | wait for a particular state in the db |
| [RowRunHarness](db/sql/RowRunHarness.md) | db\_row            | runs a 'select `*`' on a table |
| [SQLRunHarness](db/sql/SQLRunHarness.md) | sql                | run any sql     |

# HBase DB #
  * `mash-dbharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [CreateTable](db/hbase/CreateTable.md) | hbase\_create      | creates hbase table |
| [InsertRow](db/hbase/InsertRow.md) | hbase\_insert      | insert hbase row |
| [DeleteTable](db/hbase/DeleteTable.md) | hbase\_delete      | remove hbase table |
| [ScanTable](db/hbase/ScanTable.md) | hbase\_scan        | scan an hbase table for relevant rows / columns |
| [ValueFilterScanTable](db/hbase/ValueFilterScanTable.md) | hbase\_value\_scan | filter a scan result |
| [UpdateRows](db/hbase/UpdateRows.md) | hbase\_update      | scan table for row, apply update |

# File #
  * `mash-fileharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [FileGetHarness](fs/FileGetHarness.md) | get\_file          | retrieve a file |
| [FileListHarness](fs/FileListHarness.md) | list\_file         | list files      |
| [FileDeleteHarness](fs/FileDeleteHarness.md) | delete\_file       | remove file     |
| [FileCopyHarness](fs/FileCopyHarness.md) | copy\_file         | copy a file from source to target |
| [FileWaitHarness](fs/FileWaitHarness.md) | file\_wait         | wait for a file to get written |
| [FileVerifyHarness](fs/FileVerifyHarness.md) | file               | compare contents of a file |

# Ftp #
  * `mash_ftpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [FTPRunHarness](ftp/FtpRunHarness.md) | ftp                | invokes ftp requests |
| [GetHarness](ftp/GetHarness.md)  | get\_ftp           | gets a file     |
| [ListHarness](ftp/ListHarness.md) | list\_ftp          | lists files     |
| [PutHarness](ftp/PutHarness.md)  | put\_ftp           | puts a file on a server |
| [DeleteHarness](ftp/DeleteHarness.md) | delete\_ftp        | removes a directory or file |
| [FTPVerifyListHarness](ftp/FtpVerifyListHarness.md) | list\_ftp          | used by ListHarness, verifies files are present |
| [FTPWaitHarness](ftp/FtpWaitHarness.md) | wait\_ftp          | wait for a file to be there |

# Http #
  * `mash_httpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [HttpRunHarness](http/HttpRunHarness.md) | http               | invokes http requests |
| [HttpVerifyHarness](http/HttpRunHarness.md) | http               | verify response (title, etc) |

# Email #
  * `mash_mailharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [GetEmail](email/GetIMAPEmail.md) | email              | email client for retrieving messages |
| [EmailSetupHarness](email/IMAPSetupHarness.md) | email              | cleaning out an inbox before testing |
| [EmailVerifyHarness](email/EmailVerifyHarness.md) | email              | verify email (subject, count, recipient, etc) |

# REST #
  * `mash_httpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [RestRunHarness](http/RestRunHarness.md) | rest               | invokes http requests in a restful manner.  Uses [RestResponse](http/RestResponse.md) for xpath |

# Messaging #
  * `mash_messageharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [XmlJMSHarness](jms/XmlJMSHarness.md) | xml\_jms           | send and receive xml files on JMS queue |
| [TextJMSHarness](jms/TextJMSHarness.md) | text\_jms          | send and receive text files on JMS queue |
| [ObjectJMSHarness](jms/ObjectJMSHarness.md) | object\_jms        | receive Java objects from JMS queue |
| [JMSSetupHarness](jms/JMSSetupHarness.md) | jms                | clean a queue   |

# Core #
  * `mash_core.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [StandardVerifyHarness](StandardVerifyHarness.md) | standard           | standard response validation |
| [ListVerifyHarness](ListVerifyHarness.md) | list               | verify lists, retrieve specific elements to verify |

# Utilities #
  * `mash_utilityharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [TimedWaitRunHarness](TimedWaitRunHarness.md) | wait               | wait some amount of time |
| [CommandExecutorHarness](cl/CommandExecutorHarness.md) | cli                | execute a command on command line |
| [CommandExecuterVerifyHarness](cl/CommandExecuterVerifyHarness.md) | cli                | verify the output of a command |

# Teardown #
no teardown implementations yet