# Introduction #

Verify email information retrieved by [GetEmail](GetIMAPEmail.md)

# Configuration #
Configuration values:
  * `mail_server` = url of the email server
  * `user` = user (usually an admin user) to log into server with
  * `password` = password of user
  * `folder` = optional folder to specify (default is INBOX)
  * `protocol` = imap by default (smtp, pop, imap).  see [java Session api](http://docs.oracle.com/javaee/5/api/javax/mail/Session.html)

# Parameter #
  * `subject` = verify subject of the message
  * `recipient` = verify recipient
  * `sender` = verify sender
  * `count` = verify number of messages

# Sample Usage #
```
    <Verify type="org.mash.harness.mail.EmailVerifyHarness" name="verify email">
       <Configuration name="email_server" property="mail.server"/>
       <Configuration name="user" property="mail.user"/>
       <Configuration name="password" property="mail.password"/>
       <Parameter name="subject"><Value>Re: something</Value></Parameter>
       <Parameter name="recipient"><Value>developer@somewhere.com</Value></Parameter>
   </Verify>
```

Here the default `INBOX` is cleaned out of emails that are sent to `developer@somewhere.com`. 
If no address is specified, all emails are deleted on the server INBOX.