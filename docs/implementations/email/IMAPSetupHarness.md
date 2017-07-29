# Introduction #

Invoke an email client for cleaning out a folder on an email server.

# Configuration #
Configuration values:
  * 'mail\_server' = url of the email server
  * 'user' = user (usually an admin user) to log into server with
  * 'password' = password of user
  * 'folder' = optional folder to specify (default is INBOX)
  * 'protocol' = imap by default (smtp, pop, imap).  see [java Session api](http://docs.oracle.com/javaee/5/api/javax/mail/Session.html)

# Parameter #
  * 'action' = Is the action to perform (only 'CLEAN' right now)
  * 'address' = The recipient of the email to clean out (optional)

# Sample Usage #
```
    <Setup type="org.mash.harness.mail.EmailSetupHarness" name="clean email">
       <Configuration name="email_server" property="mail.server"/>
       <Configuration name="user" property="mail.user"/>
       <Configuration name="password" property="mail.password"/>
       <Parameter name="action"><Value>CLEAN</Value></Parameter>
       <Parameter name="address"><Value>developer@somewhere.com</Value></Parameter>
   </Setup>
```

Here the default 'INBOX' is cleaned out of emails that are sent to 'developer@somewhere.com'.  If no address is specified, all emails are deleted on the server INBOX.