# List of changes #

## 2.1 ## 
  * Library upgrades 
  * Drop default polling time to 1 second
  * java 1.8 compile

## 2.0 ##
  * Upgrade to java 8

## 1.4.2 ##
  * Add snapshot view for metrics utility

## 1.4.1 ##
  * give subscripts all parent script invocations for parameters.
  * minor cleanup and refactoring
  * prioritize response retrieval from previous harnesses to get most recent first
    * if you have 2 harnesses with the same name, and want to get a response value, the most recent one will be retrieved
  * DST flag for date verification allows dates to change but still be verified
  * `ThreadLocal` bug fix to allow parallel system tests

## 1.4 ##
  * `HarnessName` annotation
    * use name like `http` to instead of `org.mash.harness.http.HttpRunHarness`
    * see HarnessImplementations document for name on harness
  * minor JSON support.  Still not working though.
  * File Harnesses
    * copy
    * delete
    * get
    * list
  * Command line executable harness.  Can now invoke command line actions.
  * Specify content in script for FTP (don't need a file)

## 1.3 ##
  * Pass parameters to scripts invoked within a script
    * The name of the parameter in the calling script should be the same as the attribute 'scriptParameter' in the called script
    * all transforms (replace, etc) are applicable
  * Code cleanup

## 1.2 ##
  * FTPWaitHarness improvements
    * Scan file for text contents
    * Keep file results for verification / access

## 1.0 ##
  * Runners for command line (not just junit from ant)
    * Run for a particular script
    * Run for an entire suite of scripts
  * DB harness cleanup
    * repackaging
    * dbunit separation
    * sql runs
  * Unified responses around `RawResponse`
    * Used for putting raw strings into a response.  Good for file contents, string output, etc.
    * Removed `StringResponse`
  * Beefed up `XmlAccessor` for xml responses
    * Handles multiple XML documents in a row by wrapping with a root `<documents>` element
    * should allow for transparently running xpath on these elements
    * strip out xml base tags starting with `<?` and ending with `?>`
  * Renamed libraries to `mash-*`
  * HBase db support
    * simple create, insert, delete
    * scanning and filtering tables


## 0.10 ##
  * Catch throwable, not exception in script runner, for better error reporting
  * IMAP email harness
    * Retrieve emails by message number or recipient
    * clean out emails for setup
    * verification of email properties (subject, recipient, from, content)


## 0.9.1 ##
  * Log errors found during validation to the log.
  * Calculate the name of the test when no test name is given.  This is done by looking at the file name and removing the path of the suite invoking the test.
  * Provide a clear break between tests in the logs.
