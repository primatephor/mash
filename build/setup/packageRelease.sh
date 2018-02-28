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
mvn -am -pl org.mash:mash clean install -Dproject.version=$RELEASE_NAME -Dmaven.test.skip=true
mvn -pl org.mash:mash -Dversion=$RELEASE_NAME assembly:single
popd

cd target/mash-$RELEASE_NAME/mash-$RELEASE_NAME
mkdir dist
mv lib/mash-*.jar dist

FILES=dist/*
for f in $FILES
do
    mv "$f" "${f/-Latest.jar}"-$RELEASE_NAME.jar
done

cd ..

cp $BUILD_DIR/setup/install.sh mash-$RELEASE_NAME/dist
chmod +x mash-$RELEASE_NAME/dist/install.sh

tar -czf mash-$RELEASE_NAME.tgz mash-$RELEASE_NAME

mv mash-$RELEASE_NAME.tgz $BUILD_DIR/target

