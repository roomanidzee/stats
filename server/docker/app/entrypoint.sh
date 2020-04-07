#!/usr/bin/env sh

set -o errexit
set -o nounset

cmd="$*"

if [ "$1" = 'launch-app' ]; then
    java "$JVM_OPTS" -jar target/scala-2.12/food-stats.jar
elif [ "$1" = 'launch-tests' ]; then
    sbt test
else
    exec "$cmd"
fi