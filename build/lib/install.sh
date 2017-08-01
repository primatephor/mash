#!/bin/bash

mvn install::install-file -Dfile=jboss-aop-client.jar -DgroupId=jboss -DartifactId=jboss-aop-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-common-core.jar -DgroupId=jboss -DartifactId=jboss-common-core -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-javaee.jar -DgroupId=jboss -DartifactId=jboss-javaee -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-logging-spi.jar -DgroupId=jboss -DartifactId=jboss-logging-spi -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-mdr.jar -DgroupId=jboss -DartifactId=jboss-mdr -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-messaging-client.jar -DgroupId=jboss -DartifactId=jboss-messaging-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-remoting.jar -DgroupId=jboss -DartifactId=jboss-remoting -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jboss-serialization.jar -DgroupId=jboss -DartifactId=jboss-serialization -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
mvn install::install-file -Dfile=jbossall-client.jar -DgroupId=jboss -DartifactId=jbossall-client -Dversion=0.1 -Dpackaging=jar -DcreateChecksum=true
