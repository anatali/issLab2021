@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  it.unibo.qakradar2021 startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and IT_UNIBO_QAKRADAR2021_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\it.unibo.qakradar2021-1.0.jar;%APP_HOME%\lib\kotlinx-coroutines-core-1.1.0.jar;%APP_HOME%\lib\kotlinx-coroutines-core-common-1.1.0.jar;%APP_HOME%\lib\jssc-2.8.0.jar;%APP_HOME%\lib\org.eclipse.paho.client.mqttv3-1.2.1.jar;%APP_HOME%\lib\trident-1.3.jar;%APP_HOME%\lib\android-2.3.3.jar;%APP_HOME%\lib\json-20160810.jar;%APP_HOME%\lib\californium-proxy-2.0.0-M12.jar;%APP_HOME%\lib\californium-core-2.0.0-M12.jar;%APP_HOME%\lib\slf4j-log4j12-1.7.25.jar;%APP_HOME%\lib\uniboInterfaces.jar;%APP_HOME%\lib\2p301.jar;%APP_HOME%\lib\it.unibo.qakactor-2.4.jar;%APP_HOME%\lib\unibonoawtsupports.jar;%APP_HOME%\lib\IssActorKotlinRobotSupport-2.0.jar;%APP_HOME%\lib\radarPojo.jar;%APP_HOME%\lib\it.unibo.planner20-1.0.jar;%APP_HOME%\lib\aima-core-3.0.0.jar;%APP_HOME%\lib\kolor-1.0.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.4.32.jar;%APP_HOME%\lib\okhttp-4.9.0.jar;%APP_HOME%\lib\spring-web-5.3.6.jar;%APP_HOME%\lib\httpasyncclient-4.1.2.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.4.32.jar;%APP_HOME%\lib\okio-jvm-2.8.0.jar;%APP_HOME%\lib\kotlin-stdlib-1.4.32.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.4.32.jar;%APP_HOME%\lib\element-connector-2.0.0-M12.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\guava-15.0.jar;%APP_HOME%\lib\httpcore-nio-4.4.5.jar;%APP_HOME%\lib\httpcore-4.4.5.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\x86_64-3.3.0-v3346.jar;%APP_HOME%\lib\x86-3.3.0-v3346.jar;%APP_HOME%\lib\spring-beans-5.3.6.jar;%APP_HOME%\lib\spring-core-5.3.6.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\opengl-api-gl1.1-android-2.1_r1.jar;%APP_HOME%\lib\xmlParserAPIs-2.6.2.jar;%APP_HOME%\lib\xpp3-1.1.4c.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\spring-jcl-5.3.6.jar


@rem Execute it.unibo.qakradar2021
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %IT_UNIBO_QAKRADAR2021_OPTS%  -classpath "%CLASSPATH%" it.unibo.ctxradargui.MainCtxradarguiKt %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable IT_UNIBO_QAKRADAR2021_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%IT_UNIBO_QAKRADAR2021_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
