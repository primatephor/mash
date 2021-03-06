# Introduction #

Invoke an email client for retrieving messages from an IMAP server.

# Configuration #
Configuration values:
  * `mail_server` = url of the email server
  * `user` = user (usually an admin user) to log into server with
  * `password` = password of user
  * `folder` = optional folder to specify (default is INBOX)
  * `protocol` = imap by default.  see [java Session api](http://docs.oracle.com/javaee/5/api/javax/mail/Session.html)

# Parameter #
  * `message\_number` = The message to retrieve from the list on the server
  * `address` = The recipient of the email to retrieve
  * `subject` = subject of message(s) to retrieve

# Sample Usage #
```
    <Run type="org.mash.harness.mail.GetEmail" name="get email">
       <Configuration name="mail_server" property="mail.server"/>
       <Configuration name="user" property="mail.user"/>
       <Configuration name="password" property="mail.password"/>
       <Parameter name="message_number"><Value>1</Value></Parameter>
       <Parameter name="address"><Value>developer@somewhere.com</Value></Parameter>
   </Run>
   <Verify type="org.mash.harness.mail.EmailVerifyHarness" name="verify email">
       <Configuration name="count"><Value>10</Value></Configuration>
       <Configuration name="recipient"><Value>developer@ensenda.com</Value></Configuration>
       <Configuration name="subject"><Value>Some job</Value></Configuration>
       <Configuration name="contains"><Value>dev email data</Value></Configuration>
       <Configuration name="contains"><Value>Store 300 invoice</Value></Configuration>
   </Verify>
```

Here the first email for the recipient `developer@somewhere.com` is retrieved from the default `INBOX`. 
Validation looks to ensure that there are 10 total emails in the INBOX, and the message retrieved has a subject 
`Some job` and contains the text `dev email data` and `Store 300 invoice`.