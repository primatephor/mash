#!/bin/bash -e

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

## BUILD IT ALL

pushd ../modules
mvn -am -pl org.mash:Mash clean install -Dproject.version=$RELEASE_NAME -Dmaven.test.skip=true
mvn -pl org.mash:Mash -Dversion=$RELEASE_NAME assembly:single
popd

cd target/Mash-$RELEASE_NAME/Mash-$RELEASE_NAME
mkdir dist
mv lib/mash-*.jar dist

FILES=dist/*
for f in $FILES
do
    mv "$f" "${f/-Latest.jar}"-$RELEASE_NAME.jar
done

cd ..

cp $BUILD_DIR/setup/install.sh Mash-$RELEASE_NAME/dist

tar -czf Mash-$RELEASE_NAME.tgz Mash-$RELEASE_NAME

mv Mash-$RELEASE_NAME.tgz $BUILD_DIR/target

