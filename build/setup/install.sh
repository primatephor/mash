#!/bin/bash -e


FILES=./*.jar
for f in $FILES
do
    echo "FILE: $f"

    arr=(${f//-/ })
    len=${#arr[@]}

    version_jar=${arr[$len-1]}

    versionArr=(${version_jar//./ })
    version=${versionArr[0]}

    artifact_file=${f/-$version_jar}
    artifact="${artifact_file:2}"

    echo "file:$f, artifact:$artifact, version: $version"
    mvn install::install-file -Dfile="$f" -DgroupId=org.mash -DartifactId="$artifact" -Dversion=$version -Dpackaging=jar -DcreateChecksum=true
done

