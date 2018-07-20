@echo off
REM   Startup script for running ESRI MapObjects applications

REM   Usage:
REM	MOJ_RUN [application-class-name] [application classpath] {optional-startup-folder} {optional-JDKHOME} {optional-MOJ20-path}

REM   -------------------------------------------------------------------------
REM   ---      start of customization section

REM   the MOJ20 variable should be modified appropriately for your system.
REM   Otherwise, the current folder should be \moj20\Scripts

set   MOJ20=C:\esri\moj20

REM   the RUN_JAVA variable should be modified appropriately 
REM   if you want to use other JRE/JDK for your system. 

REM   RUN_JAVA=%MOJ20%\jre\bin\java
set   RUN_JAVA=C:\jdk1.8.0_101\jre\bin\java

REM   ---      end of customization section
REM   -------------------------------------------------------------------------

REM   Check parameters

if '%2' == '' GOTO Usage
set APP=%1
set APPJAR=%2

if '%3' == '' GOTO NoStartup
CD/D %3
:NoStartup

if '%4' == '' GOTO CONTINUE1
  set JAVA_HOME=%4
  set RUN_JAVA=%JAVA_HOME%\bin\java
:CONTINUE1

if '%5' == '' GOTO CONTINUE2
  set MOJ20=%5
:CONTINUE2

REM   Set environment variables

set JARS=%MOJ20%\lib

set MOJ_JAR=%JARS%\esri_mo20.jar;%JARS%\esri_mo20res.jar
set SSL_JAR=%JARS%\jnet.jar;%JARS%\jsse.jar;%JARS%\jcert.jar
set SDE_JAR=%JARS%\jsde83_sdk.jar;%JARS%\jsde83_sdkres.jar;
set JAI_JAR=%JARS%\jai_codec.jar;%JARS%\jai_core.jar;%JARS%\mlibwrapper_jai.jar
set IMG_JAR=%JARS%\esri_mo20img.jar
set AXL_JAR=%JARS%\esri_xmlkit.jar
REM set GCD_JAR=%JARS%\esri_mo20gcd.jar;%JARS%\esri_mo20gcdres.jar
set CAD_JAR=%JARS%\esri_mo20cad.jar
set MIL_JAR=%JARS%\esri_mo20vpf.jar;%JARS%\esri_mo20rpf.jar 

set CPATH=%APPJAR%;%MOJ_JAR%;%SSL_JAR%;%SDE_JAR%;%JAI_JAR%;%IMG_JAR%;%AXL_JAR%;%CAD_JAR%;%MIL_JAR%
set PATH=%JARS%;%PATH%

REM   Run the application

%RUN_JAVA% -Xmx256m -classpath %CPATH% %APP%
GOTO FinishUp

REM display usage

:Usage
ECHO Usage: MOJ_RUN [application-class-name] [application classpath] {optional-startup-folder} {optional-JDKHOME} {optional-MOJ20-path}

REM   Pause to allow display to remain in window

:FinishUp
pause

