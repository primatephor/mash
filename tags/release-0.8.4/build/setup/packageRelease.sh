#!/bin/bash

if [ -z $1 ]
then
  echo "please supply a release number"
  exit
fi

RELEASE_NAME=mash-"$1"

cd ..
BUILD_DIR=`pwd`
rm -rf $RELEASE_NAME

mkdir $BUILD_DIR/$RELEASE_NAME

## BUILD IT ALL
cd ../modules
ant clean
ant build
ant docs

cd $BUILD_DIR
cp -R target/* $RELEASE_NAME

## Move the readme
cp setup/readme.txt $RELEASE_NAME/readme.txt

## Copy source
mkdir $RELEASE_NAME/src
mkdir $RELEASE_NAME/src/mash
mkdir $RELEASE_NAME/src/junitrunner
mkdir $RELEASE_NAME/src/dbharness
mkdir $RELEASE_NAME/src/httpharness
cp -R ../modules/mash/src/* $RELEASE_NAME/src/mash
cp -R ../modules/junitrunner/src/* $RELEASE_NAME/src/junitrunner
cp -R ../modules/dbharness/src/* $RELEASE_NAME/src/dbharness
cp -R ../modules/httpharness/src/* $RELEASE_NAME/src/httpharness

## Clean it up
cd $RELEASE_NAME
find . -name ".svn" -exec rm -rf {} \;

## Zip it all up
cd $BUILD_DIR
zip -r $RELEASE_NAME.zip $RELEASE_NAME

