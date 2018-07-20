@echo off
REM   Startup script for compiling ESRI MapObjects applications

REM   Usage:
REM	MOJ_COMPILE [source-code] {optional-output-folder}

REM   -------------------------------------------------------------------------
REM   ---      start of customization section

REM   the following two environment variables should be
REM   modified appropriately for your system

set   JDKHOME=C:\jdk1.8.0_101
set   MOJ20=C:\esri\moj20

REM   ---      end of customization section
REM   -------------------------------------------------------------------------

REM   Check parameters

if '%1' == '' GOTO Usage
set APP=%1
set OUT=.
if '%2' == '' GOTO NoOutput
set OUT=%2
:NoOutput

REM   Set environment variables

set JARS=%MOJ20%\lib

set MOJ_JAR=%JARS%\esri_mo20.jar;%JARS%\esri_mo20res.jar
set SSL_JAR=%JARS%\jnet.jar;%JARS%\jsse.jar;%JARS%\jcert.jar
set SDE_JAR=%JARS%\jsde83_sdk.jar;%JARS%\jsde83_sdkres.jar;
set JAI_JAR=%JARS%\jai_codec.jar;%JARS%\jai_core.jar;%JARS%\mlibwrapper_jai.jar
set IMG_JAR=%JARS%\esri_mo20img.jar
set AXL_JAR=%JARS%\esri_xmlkit.jar
set CAD_JAR=%JARS%\esri_mo20cad.jar
set MIL_JAR=%JARS%\esri_mo20vpf.jar;%JARS%\esri_mo20rpf.jar 

set CPATH=%MOJ_JAR%;%SSL_JAR%;%SDE_JAR%;%JAI_JAR%;%IMG_JAR%;%AXL_JAR%;%CAD_JAR%;%MIL_JAR%

REM   Run the application

%JDKHOME%\bin\javac -d %OUT% -classpath %CPATH% %APP%
GOTO FinishUp

REM display usage

:Usage
ECHO Usage: MOJ_COMPILE [source-code] {optional-output-folder}

REM   Pause to allow display to remain in window

:FinishUp
pause

