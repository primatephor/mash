#!/bin/bash -e

VERSION=$1

FILES=./*.jar
for f in $FILES
do
    echo "FILE: $f"

    arr=(${f//-/ })
    len=${#arr[@]}

    version_jar=${arr[$len-1]}
    #echo "JAR: $version_jar"

    versionArr=(${version_jar//.jar/ })

    if [ -z $VERSION ]; then
        version=${versionArr[0]}
    else
        version=$VERSION
    fi

    artifact_file=${f/-$version_jar}
    artifact="${artifact_file:2}"

    echo "file:$f, artifact:$artifact, version: $version"
    mvn install::install-file -Dfile="$f" -DgroupId=org.mash -DartifactId="$artifact" -Dversion=$version -Dpackaging=jar -DcreateChecksum=true
done

