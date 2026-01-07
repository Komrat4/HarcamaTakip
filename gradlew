#!/usr/bin/env sh

APP_HOME=$(cd "$(dirname "$0")" && pwd)
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo "$*"
    exit 1
}

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ -n "$JAVA_HOME" ] ; then
    JAVA_EXEC="$JAVA_HOME/bin/java"
else
    JAVA_EXEC="java"
fi

if [ ! -x "$JAVA_EXEC" ] ; then
    die "ERROR: JAVA_HOME is not set and no 'java' command could be found."
fi

if [ "$MAX_FD" != "maximum" ] ; then
    ulimit -n "$MAX_FD"
fi

exec "$JAVA_EXEC" -Dorg.gradle.appname="$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
