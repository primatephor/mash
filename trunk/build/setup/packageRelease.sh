#!/bin/bash

if [ -z $1 ]
then
  echo "please supply a release number"
  exit
fi

RELEASE_NAME=$1

cd ..
BUILD_DIR=`pwd`
rm -rf $RELEASE_NAME
rm -rf target

#mkdir $BUILD_DIR/$RELEASE_NAME

## BUILD IT ALL

mvn -am -pl org.mash:Mash clean install -Dmaven.test.skip=true -Dversion=$RELEASE_NAME assembly:assembly
#mvn -am -pl org.mash:Mash clean install -Dmaven.test.skip=true
#mvn -pl org.mash:Mash -Dversion=$RELEASE_NAME assembly:assembly

