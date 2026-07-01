#!/bin/sh
if [ -z "$JAVA_HOME" ]; then
  JAVACMD="java"
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

DOWNLOAD_URL=$(grep "wrapperUrl" "$MAVEN_WRAPPER_PROPERTIES" | cut -d= -f2)

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
  echo "Baixando Maven Wrapper..."
  if command -v curl > /dev/null; then
    curl -o "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL" -s
  elif command -v wget > /dev/null; then
    wget -q -O "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL"
  fi
fi

exec "$JAVACMD" \
  -classpath "$MAVEN_WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain \
  "$@"