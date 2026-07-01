@REM Maven Wrapper para Windows
@echo off
setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set "MAVEN_WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

if "%JAVA_HOME%"=="" (
  set "JAVACMD=java"
) else (
  set "JAVACMD=%JAVA_HOME%\bin\java"
)

if not exist "%MAVEN_WRAPPER_JAR%" (
  echo Baixando Maven Wrapper...
  powershell -Command "& { $props = Get-Content '%MAVEN_WRAPPER_PROPERTIES%'; $url = ($props | Select-String 'wrapperUrl=(.+)').Matches.Groups[1].Value; Invoke-WebRequest -Uri $url -OutFile '%MAVEN_WRAPPER_JAR%' }"
)

"%JAVACMD%" -classpath "%MAVEN_WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*