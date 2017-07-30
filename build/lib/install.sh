#!/bin/bash

mvn install::install-file -Dfile=annovention-1.7.jar -DgroupId=tv.cntt -DartifactId=annovention -Dversion=1.7 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=commons-net-ftp-2.0.jar -DgroupId=commons-net-ftp -DartifactId=commons-net-ftp -Dversion=2.0 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=dbunit-2.4.5.jar -DgroupId=dbunit -DartifactId=dbunit -Dversion=2.4.5 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=hadoop-common-2.7.3.jar -DgroupId=org.apache.hadoop -DartifactId=hadoop-common -Dversion=2.7.3 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=hbase-client-1.2.5.jar -DgroupId=org.apache.hbase -DartifactId=hbase-client -Dversion=1.2.5 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=htmlunit-2.6.jar -DgroupId=net.sourceforge.htmlunit -DartifactId=htmlunit -Dversion=2.6 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=htmlunit-core-js-2.6.jar -DgroupId=net.sourceforge.htmlunit -DartifactId=htmlunit-core-js -Dversion=2.6 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-aop-client.jar -DgroupId=jboss -DartifactId=jboss-aop-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-common-core.jar -DgroupId=jboss -DartifactId=jboss-common-core -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-javaee.jar -DgroupId=jboss -DartifactId=jboss-javaee -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-logging-spi.jar -DgroupId=jboss -DartifactId=jboss-logging-spi -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-mdr.jar -DgroupId=jboss -DartifactId=jboss-mdr -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-messaging-client.jar -DgroupId=jboss -DartifactId=jboss-messaging-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-remoting.jar -DgroupId=jboss -DartifactId=jboss-remoting -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-serialization.jar -DgroupId=jboss -DartifactId=jboss-serialization -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jbossall-client.jar -DgroupId=jboss -DartifactId=jbossall-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=slf4j-api-1.5.8.jar -DgroupId=slf4j -DartifactId=slf4j-api -Dversion=1.5.8 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=slf4j-log4j12-1.5.8.jar -DgroupId=slf4j -DartifactId=slf4j-log4j12 -Dversion=1.5.8 -Dpackaging=jar -DcreateChecksum=true
