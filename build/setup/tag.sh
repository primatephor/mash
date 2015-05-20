#!/bin/bash

if [ -z $1 ]
then
  echo "please supply a release number"
  exit
fi

TAG_NAME=release-"$1"

svn copy https://mash.googlecode.com/svn/trunk --username blah https://mash.googlecode.com/svn/tags/$TAG_NAME -m "tagging for release"
