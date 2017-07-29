# Introduction #

This is the list of supplied harnesses.  To build your own, check out DevelopingHarnesses.


# Relational DB #
  * `mash-dbharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [DBSetupHarness](docs/implementations/db/DBSetupHarness.md) | db                 | uses dbunit, runs sql |
| [DBWaitHarness](docs/implementations/db/DBWaitHarness.md) | db\_wait           | wait for a particular state in the db |
| [RowRunHarness](docs/implementations/db/RowRunHarness.md) | db\_row            | runs a 'select `*`' on a table |
| [SQLRunHarness](docs/implementations/db/SQLRunHarness.md) | sql                | run any sql     |

# HBase DB #
  * `mash-dbharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [CreateTable](CreateTable.md) | hbase\_create      | creates hbase table |
| [InsertRow](InsertRow.md) | hbase\_insert      | insert hbase row |
| [DeleteTable](DeleteTable.md) | hbase\_delete      | remove hbase table |
| [ScanTable](ScanTable.md) | hbase\_scan        | scan an hbase table for relevant rows / columns |
| [ValueFilterScanTable](ValueFilterScanTable.md) | hbase\_value\_scan | filter a scan result |
| [UpdateRows](UpdateRows.md) | hbase\_update      | scan table for row, apply update |

# File #
  * `mash-fileharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [FileGetHarness](FileGetHarness.md) | get\_file          | retrieve a file |
| [FileListHarness](FileListHarness.md) | list\_file         | list files      |
| [FileDeleteHarness](FileDeleteHarness.md) | delete\_file       | remove file     |
| [FileCopyHarness](FileCopyHarness.md) | copy\_file         | copy a file from source to target |
| [FileWaitHarness](FileWaitHarness.md) | file\_wait         | wait for a file to get written |
| [FileVerifyHarness](FileVerifyHarness.md) | file               | compare contents of a file |

# Ftp #
  * `mash_ftpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [FTPRunHarness](FtpRunHarness.md) | ftp                | invokes ftp requests |
| GetHarness  | get\_ftp           | gets a file     |
| ListHarness | list\_ftp          | lists files     |
| PutHarness  | put\_ftp           | puts a file on a server |
| DeleteHarness | delete\_ftp        | removes a directory or file |
| [FTPVerifyListHarness](FtpVerifyListHarness.md) | list\_ftp          | used by ListHarness, verifies files are present |
| [FTPWaitHarness](FtpWaitHarness.md) | wait\_ftp          | wait for a file to be there |

# Http #
  * `mash_httpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| HttpRunHarness | http               | invokes http requests |
| HttpVerifyHarness | http               | verify response (title, etc) |

# Email #
  * `mash_mailharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [GetEmail](GetIMAPEmail.md) | email              | email client for retrieving messages |
| [EmailSetupHarness](IMAPSetupHarness.md) | email              | cleaning out an inbox before testing |
| [EmailVerifyHarness](EmailVerifyHarness.md) | email              | verify email (subject, count, recipient, etc) |

# REST #
  * `mash_httpharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| RestRunHarness | rest               | invokes http requests in a restful manner.  Uses RestResponse for xpath |

# Messaging #
  * `mash_messageharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| [XmlJMSHarness](XmlJMSHarness.md) | xml\_jms           | send and receive xml files on JMS queue |
| [TextJMSHarness](TextJMSHarness.md) | text\_jms          | send and receive text files on JMS queue |
| [ObjectJMSHarness](ObjectJMSHarness.md) | object\_jms        | receive Java objects from JMS queue |
| [JMSSetupHarness](JMSSetupHarness.md) | jms                | clean a queue   |

# Core #
  * `mash_core.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| StandardVerifyHarness | standard           | standard response validation |
| ListVerifyHarness | list               | verify lists, retrieve specific elements to verify |

# Utilities #
  * `mash_utilityharness.jar`
  
| **Harness** | **type attribute** | **Description** |
| --- | --- | --- |
| TimedWaitRunHarness | wait               | wait some amount of time |
| CommandExecutorHarness | cli                | execute a command on command line |
| CommandExecuterVerifyHarness | cli                | verify the output of a command |

# Teardown #
no teardown implementations yet